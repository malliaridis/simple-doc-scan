package com.example.simpledocscan.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.simpledocscan.R
import com.example.simpledocscan.databinding.CompleteFragmentBinding
import com.example.simpledocscan.utils.generateEmailIntent
import java.util.*

/**
 * This fragment represents the final step of the app, displaying and sending the scanned
 * document via email.
 */
class CompleteFragment : Fragment() {

    /**
     * Global view-model used for storing and retrieving global variables.
     */
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Activity result launcher that handles activity responses of the email intent.
     */
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

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

        // Register for intent completion results
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // TODO navigate to completion screen
            }
        }

        // Observe scanned files and display to the user whenever they change
        viewModel.documentLive.observe(viewLifecycleOwner) { file ->
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            binding.ivResultImage.setImageBitmap(bitmap)
        }

        binding.btSend.setOnClickListener {
            sendImage()
        }

        return binding.root
    }

    /**
     * Collects all information from the fragments via the [MainViewModel], creates and executes
     * an email intent. In case any information is missing, a toast messages is displayed and
     * the process is aborted.
     */
    private fun sendImage() {
        val email = viewModel.emailAddressLive.value
        val image = viewModel.documentLive.value
        val subject = Calendar.getInstance().time.toString()

        if (email == null || image == null) {
            Toast.makeText(requireContext(), R.string.error_wrong, Toast.LENGTH_LONG).show()
        } else {

            val intent = generateEmailIntent(email, subject, image)
            startIntent(intent)
        }
    }

    /**
     * Starts the provided intent and continues to the completion fragment if successfully.
     */
    private fun startIntent(intent: Intent) {
        activityResultLauncher.launch(intent)
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