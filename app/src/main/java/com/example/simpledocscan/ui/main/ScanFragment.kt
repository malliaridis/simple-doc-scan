package com.example.simpledocscan.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.simpledocscan.databinding.ScanFragmentBinding
import com.example.simpledocscan.databinding.StartFragmentBinding

/**
 * This fragment represents the second step of the app, recognizing and processing the document.
 */
class ScanFragment : Fragment() {

    /**
     * Global view-model used for storing and retrieving global variables.
     */
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Nullable view binding variable used to inflate layout into it.
     */
    private var _binding: ScanFragmentBinding? = null

    /**
     * Not-null view binding variable used to reference views from layout.
     */
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScanFragmentBinding.inflate(inflater, container, false)

        // Set current progress
        viewModel.setProgress(CURRENT_PROGRESS)

        return binding.root
    }
}

/**
 * Value that represents the current progress of this fragment
 */
private const val CURRENT_PROGRESS = 2