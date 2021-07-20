package com.example.simpledocscan.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.simpledocscan.databinding.ScanFragmentBinding

/**
 * This fragment represents the second step of the app, recognizing and processing the document.
 */
class ScanFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: ScanFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScanFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}