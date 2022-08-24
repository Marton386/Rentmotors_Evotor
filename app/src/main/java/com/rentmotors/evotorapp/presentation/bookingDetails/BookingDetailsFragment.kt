package com.rentmotors.evotorapp.presentation.bookingDetails

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rentmotors.evotorapp.presentation.utils.PriceFormatter
import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.databinding.FragmentBookingDetailsBinding
import com.rentmotors.evotorapp.databinding.ItemCheckPositionBinding
import com.rentmotors.evotorapp.domain.entities.BookReceipt
import com.rentmotors.evotorapp.presentation.searchBook.BookViewModel
import com.rentmotors.evotorapp.presentation.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.evotor.framework.core.IntegrationManagerFuture
import ru.evotor.framework.core.action.command.open_receipt_command.OpenSellReceiptCommand
import ru.evotor.framework.core.action.event.receipt.changes.position.PositionAdd
import ru.evotor.framework.kkt.FiscalTags
import ru.evotor.framework.navigation.NavigationApi
import ru.evotor.framework.receipt.Measure
import ru.evotor.framework.receipt.Position
import ru.evotor.framework.receipt.TaxNumber
import java.math.BigDecimal
import java.util.*

@AndroidEntryPoint
class BookingDetailsFragment : Fragment(R.layout.fragment_booking_details) {

    companion object {
        private const val TAG = "BookingDetailsFragment"
        private const val REQUEST_CODE = 111
    }

    private val viewModel: BookViewModel by activityViewModels()
    private val bookingDetailsViewModel: BookingDetailsViewModel by viewModels()
    private var _binding: FragmentBookingDetailsBinding? = null
    private val binding get() = _binding!!


    private val evotorActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                // Handle the Intent
                bookingDetailsViewModel.sendPaymentInfo(requireContext())
            } else {
                binding.lnToPay.isEnabled = true
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookingDetailsBinding.bind(view)
        binding.viewModel = bookingDetailsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getBook()?.let {
            setBookReceipt(it)
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
                        if(it == BookingDetailsViewModel.BookingDetailsState.Loading)
                            //binding.svItems.visibility = View.INVISIBLE
                            binding.pbLoad.visibility = View.VISIBLE
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

    //Обязательно исправить, вместо ScrollView поставить нормальное решение
    private fun setBookReceipt(receipt: BookReceipt) {
        binding.tvAmount.text =
            getString(
                R.string.rub,
                PriceFormatter.format(receipt.items.sumOf { it.amount })
            )

        binding.layoutItems.removeAllViews()
        receipt.items.forEach {
            val itemBinding = ItemCheckPositionBinding.inflate(
                layoutInflater,
                binding.layoutItems,
                false
            )

            itemBinding.tvName.text = it.name
            itemBinding.tvPrice.text = PriceFormatter.format(it.price)
            itemBinding.tvQuantity.text = getString(R.string.quantity, it.quantity)
            itemBinding.tvAmount.text = PriceFormatter.format(it.amount)

            binding.layoutItems.addView(itemBinding.root)
        }
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

        OpenSellReceiptCommand(positionsToAdd, null).process(requireActivity()) {
            try {
                val result = it.result
                if (result.type == IntegrationManagerFuture.Result.Type.OK) {
                    try {
                        evotorActivityResult.launch(NavigationApi.createIntentForSellReceiptPayment())
                    } catch (e: Exception) {
                        showErrorDialog(e.stackTraceToString())
                    }
                }
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

