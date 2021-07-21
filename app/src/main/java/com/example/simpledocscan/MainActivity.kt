package com.example.simpledocscan

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.simpledocscan.databinding.MainActivityBinding
import com.example.simpledocscan.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    /**
     * Main view-model that holds the app's state. Used in main activity currently
     * for displaying only the global progress.
     */
    private val viewModel: MainViewModel by viewModels()

    private var _binding: MainActivityBinding? = null
    private val binding get() = _binding!!

    /**
     * Navigation host for initiating fragment transactions via navigation controller.
     */
    private lateinit var host: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.progressLive.observe(this) { progress ->
            binding.pbGlobalProgress.progress = progress
        }

        host = supportFragmentManager.findFragmentById(R.id.fc_main) as NavHostFragment? ?: return
    }
}