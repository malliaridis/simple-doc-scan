package com.example.simpledocscan.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.simpledocscan.databinding.StartFragmentBinding

/**
 * This fragment represents the first step of the app, providing an email address for sending the
 * final document.
 */
class StartFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: StartFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}