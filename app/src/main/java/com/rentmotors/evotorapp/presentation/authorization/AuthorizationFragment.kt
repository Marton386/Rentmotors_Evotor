package com.rentmotors.evotorapp.presentation.authorization

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rentmotors.evotorapp.BuildConfig
import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.databinding.FragmentAuthorizationBinding
import com.rentmotors.evotorapp.presentation.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {

    companion object {
        private const val TAG = "AuthorizationFragment"
    }

    private val authorizationViewModel: AuthorizationViewModel by viewModels()
    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAuthorizationBinding.bind(view)

        binding.viewModel = authorizationViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.tvVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)

        binding.lnDone.setOnClickListener {
            authorizationViewModel.checkDevice(requireContext())
        }

        subscribeUi()
    }

    private fun subscribeUi() {
        lifecycleScope.launchWhenStarted {
            authorizationViewModel.state.collectLatest {
                when (it) {
                    is AuthorizationViewModel.CheckDeviceState.Success -> {
                        binding.pbLoad.visibility = View.INVISIBLE
                    }
                    is AuthorizationViewModel.CheckDeviceState.Error -> {
                        binding.pbLoad.visibility = View.INVISIBLE
                        showErrorDialog(it.error)
                    }
                    is AuthorizationViewModel.CheckDeviceState.Loading -> {
                        binding.pbLoad.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            authorizationViewModel.isDevicePermittedEvent.collect {
                findNavController().navigateUp()
            }
        }
    }
}