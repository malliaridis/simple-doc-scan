package com.example.simpledocscan.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.simpledocscan.R
import com.example.simpledocscan.databinding.CompleteFragmentBinding

class CompleteFragment : Fragment() {

    /**
     * Global view-model used for storing and retrieving global variables.
     */
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Nullable view binding variable used to inflate layout into it.
     */
    private var _binding: CompleteFragmentBinding? = null

    /**
     * Not-null view binding variable used to reference views from layout.
     */
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CompleteFragmentBinding.inflate(inflater, container, false)

        // Set button actions
        binding.btRestart.setOnClickListener {
            resetProcess()
        }

        binding.btClose.setOnClickListener {
            // Terminate app
            activity?.finishAffinity()
        }

        return binding.root
    }

    /**
     * Resets view-model and return to start fragment
     */
    private fun resetProcess() {
        viewModel.reset()
        findNavController().navigate(R.id.action_complete_fragment_to_start_fragment)
    }

    override fun onResume() {
        super.onResume()
        // Set current progress
        viewModel.setProgress(CURRENT_PROGRESS)
    }
}

/**
 * Value that represents the current progress of this fragment
 */
private const val CURRENT_PROGRESS = 3