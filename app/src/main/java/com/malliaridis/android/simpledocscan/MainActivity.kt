package com.malliaridis.android.simpledocscan

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.malliaridis.android.simpledocscan.databinding.MainActivityBinding
import com.malliaridis.android.simpledocscan.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    /**
     * Main view-model that holds the app's state. Used in main activity currently
     * for displaying only the global progress.
     */
    private val viewModel: MainViewModel by viewModels()

    /**
     * Nullable view binding variable used to inflate layout into it.
     */
    private var _binding: MainActivityBinding? = null

    /**
     * Not-null view binding variable used to reference views from layout.
     */
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Update progress bar whenever progress is updated
        viewModel.progressLive.observe(this) { progress ->
            binding.pbGlobalProgress.progress = progress
        }
    }
}