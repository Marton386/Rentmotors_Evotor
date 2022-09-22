package com.rentmotors.evotorapp.presentation.refundDetails

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.databinding.FragmentRefundDetailsBinding
import com.rentmotors.evotorapp.domain.entities.BookReceipt
import com.rentmotors.evotorapp.presentation.adapters.RefundAdapter
import com.rentmotors.evotorapp.presentation.bookingDetails.BookingDetailsViewModel
import com.rentmotors.evotorapp.presentation.searchBook.BookViewModel
import com.rentmotors.evotorapp.presentation.utils.PriceFormatter
import com.rentmotors.evotorapp.presentation.utils.showErrorDialog
import kotlinx.coroutines.flow.collectLatest
import ru.evotor.framework.core.IntegrationException
import ru.evotor.framework.core.IntegrationManagerFuture
import ru.evotor.framework.core.action.command.open_receipt_command.OpenPaybackReceiptCommand
import ru.evotor.framework.core.action.event.receipt.changes.position.PositionAdd
import ru.evotor.framework.kkt.FiscalTags
import ru.evotor.framework.navigation.NavigationApi
import ru.evotor.framework.receipt.Measure
import ru.evotor.framework.receipt.Position
import ru.evotor.framework.receipt.TaxNumber
import java.math.BigDecimal
import java.util.*

class RefundDetailsFragment : Fragment(R.layout.fragment_refund_details) {
    private val viewModel: BookViewModel by activityViewModels()
    private val bookingDetailsViewModel: BookingDetailsViewModel by activityViewModels()
    private var _binding: FragmentRefundDetailsBinding? = null
    private val binding get() = _binding!!
    private val evotorActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                bookingDetailsViewModel.sendPaymentInfo(requireContext())
            } else {
                binding.lnToPay.isEnabled = true
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRefundDetailsBinding.bind(view)
        binding.progressBar.visibility = View.GONE
        viewModel.getBook()?.let {
            setRefundReceipt(it)
        }
        binding.lnToPay.setOnClickListener {
            binding.lnToPay.isEnabled = false
            val book = viewModel.getBook()
            if (book != null) {
                openReceipt(book)
            } else {
                showErrorDialog(getString(R.string.error))
            }
        }
        subscribeUi()
    }

    private fun subscribeUi() {
        lifecycleScope.launchWhenStarted {
            bookingDetailsViewModel.state.collectLatest {
                when (it) {
                    is BookingDetailsViewModel.BookingDetailsState.Success -> {}
                    is BookingDetailsViewModel.BookingDetailsState.Error -> {
                        showErrorDialog(it.error)
                    }
                    else -> {
                        if (it == BookingDetailsViewModel.BookingDetailsState.Loading) {
                            binding.recyclerView.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            bookingDetailsViewModel.goToSearchBookScreenEvent.collect {
                bookingDetailsViewModel.clearPayingInfo()
                findNavController().navigateUp()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            bookingDetailsViewModel.stateTryError.collect {
                when (it) {
                    is BookingDetailsViewModel.TryErrorState.TryAgain -> {
                        bookingDetailsViewModel.retrySendPaymentInfo(requireContext())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setRefundReceipt(receipt: BookReceipt) {
        binding.tvAmount.text =
            getString(
                R.string.rub,
                PriceFormatter.format(receipt.items.sumOf { it.amount })
            )
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = linearLayoutManager
        val adapter = RefundAdapter(requireContext(), receipt.items)
        binding.recyclerView.adapter = adapter
    }

    private fun openReceipt(bookReceipt: BookReceipt) {
        val positionsToAdd = bookReceipt.items.map { bookItem ->
            PositionAdd(
                Position.Builder.newInstance(
                    UUID.randomUUID().toString(),
                    null,
                    bookItem.name,
                    Measure("шт", 1, FiscalTags.MEASURE_CODE),
                    BigDecimal(bookItem.price),
                    BigDecimal(bookItem.quantity)
                ).setTaxNumber(getVat(bookItem.tax))
                    .toService()
                    .build()
            )
        }

        OpenPaybackReceiptCommand(positionsToAdd, null).process(requireActivity()) {
            try {
                val result = it.result
                if (result.type == IntegrationManagerFuture.Result.Type.OK) {
                    try {
                        evotorActivityResult.launch(NavigationApi.createIntentForPaybackReceiptPayment())
                    } catch (e: Exception) {
                        showErrorDialog(e.stackTraceToString())
                    }
                }
            } catch (e: IntegrationException) {
                e.printStackTrace()
                showErrorDialog("Something went wrong: ${e.stackTraceToString()}")
            } catch (e: Exception) {
                e.printStackTrace()
                showErrorDialog("Something went wrong: ${e.message}")
            }
        }
    }

    private fun getVat(vat: String): TaxNumber {
        return when (vat) {
            "vat20" -> TaxNumber.VAT_18
            else -> TaxNumber.NO_VAT
        }
    }
}