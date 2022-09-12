package com.rentmotors.evotorapp.presentation.searchRefund

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rentmotors.evotorapp.BuildConfig
import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.databinding.FragmentSearchBookBinding
import com.rentmotors.evotorapp.presentation.searchBook.BookViewModel
import com.rentmotors.evotorapp.presentation.searchBook.SearchBookViewModel
import com.rentmotors.evotorapp.presentation.utils.showErrorDialog
import kotlinx.coroutines.flow.collectLatest

class SearchRefundFragment : Fragment(R.layout.fragment_search_book) {
    private val viewModel: BookViewModel by activityViewModels()
    private val searchBookViewModel: SearchBookViewModel by activityViewModels()
    private var _binding: FragmentSearchBookBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBookBinding.bind(view)
        binding.viewModel = searchBookViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        searchBookViewModel.clearResNumber()

        binding.viewModel = searchBookViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        searchBookViewModel.clearResNumber()

        binding.lnSearchBook.setOnClickListener {
            searchBookViewModel.getBook(requireContext())
        }

        binding.tvVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)

        subscribeUi()
    }

    private fun subscribeUi() {
        lifecycleScope.launchWhenStarted {
            searchBookViewModel.state.collectLatest {
                when (it) {
                    is SearchBookViewModel.SearchBookState.Success -> {
                        binding.pbLoad.visibility = View.INVISIBLE
                    }
                    is SearchBookViewModel.SearchBookState.Error -> {
                        binding.pbLoad.visibility = View.INVISIBLE
                        showErrorDialog(it.error)
                    }
                    is SearchBookViewModel.SearchBookState.Loading -> {
                        binding.pbLoad.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            searchBookViewModel.goToDetailScreenEvent.collect {
                viewModel.setBookReceipt(it)
                val action =
                    SearchRefundFragmentDirections.actionSearchRefundFragmentToRefundDetailsFragment()
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}