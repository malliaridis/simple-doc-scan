package com.example.simpledocscan.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.simpledocscan.databinding.CompleteFragmentBinding

/**
 * This fragment represents the final step of the app, displaying and sending the scanned
 * document via email.
 */
class CompleteFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: CompleteFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CompleteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}