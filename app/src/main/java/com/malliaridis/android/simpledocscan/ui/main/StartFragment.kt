package com.malliaridis.android.simpledocscan.ui.main

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.malliaridis.android.simpledocscan.R
import com.malliaridis.android.simpledocscan.databinding.StartFragmentBinding

/**
 * This fragment represents the first step of the app, providing an email address for sending the
 * final document.
 */
class StartFragment : Fragment() {

    /**
     * Global view-model used for storing and retrieving global variables.
     */
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Temporary email input field value used to store [onSaveInstanceState].
     * Used in cases of device rotation and could be extracted to viewModel.
     */
    private var temporaryEmail: String = ""

    /**
     * Nullable view binding variable used to inflate layout into it.
     */
    private var _binding: StartFragmentBinding? = null

    /**
     * Not-null view binding variable used to reference views from layout.
     */
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StartFragmentBinding.inflate(inflater, container, false)

        // Disable button by default if input field is empty
        updateButtonState()

        binding.etEmailAddress.doOnTextChanged { text, _, _, _ ->
            temporaryEmail = text.toString()
            updateButtonState()
        }

        binding.etEmailAddress.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validateAndContinue()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.btContinue.setOnClickListener {
            validateAndContinue()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Set current progress
        viewModel.setProgress(CURRENT_PROGRESS)
    }

    private fun validateAndContinue() {
        val email = binding.etEmailAddress.text.toString()

        if (isValidEmailAddress(email)) {
            viewModel.setEmailAddress(email)
            findNavController().navigate(R.id.action_start_fragment_to_doc_scan_fragment)
        } else {
            binding.etEmailAddress.error = getString(R.string.invalid_email_address)
        }
    }

    private fun isValidEmailAddress(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Updates the continue button state to enable / disable according to the current text value
     * of the email input field.
     */
    private fun updateButtonState() {
        binding.btContinue.isEnabled = !binding.etEmailAddress.text.isNullOrBlank()
    }

    /**
     * Use onSaveInstanceState to store current edit text value.
     * Alternatively use viewModel.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_EMAIL, binding.etEmailAddress.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        temporaryEmail = savedInstanceState?.getString(KEY_EMAIL) ?: ""
    }
}

/**
 * Value that represents the current progress of this fragment
 */
private const val CURRENT_PROGRESS = 0

/**
 * Key used for storing temporary email address in saved instance state.
 */
private const val KEY_EMAIL = "email"