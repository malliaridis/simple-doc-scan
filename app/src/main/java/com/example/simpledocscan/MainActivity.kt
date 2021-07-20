package com.example.simpledocscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.simpledocscan.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    /**
     * Main view-model that holds the app's state. Used in main activity currently
     * for displaying only the global progress.
     */
    private val viewModel: MainViewModel by viewModels()

    /**
     * Navigation host for initiating fragment transactions via navigation controller.
     */
    private lateinit var host: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        host = supportFragmentManager.findFragmentById(R.id.fc_main) as NavHostFragment? ?: return
    }
}