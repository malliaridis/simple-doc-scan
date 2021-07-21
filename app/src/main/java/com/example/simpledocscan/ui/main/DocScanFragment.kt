package com.example.simpledocscan.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.simpledocscan.BuildConfig
import com.example.simpledocscan.R
import com.example.simpledocscan.databinding.DocScanFragmentBinding
import com.example.simpledocscan.utils.compressBitmap
import com.geniusscansdk.camera.CameraManager
import com.geniusscansdk.camera.ScanFragment
import com.geniusscansdk.camera.realtime.BorderDetector.BorderDetectorListener
import com.geniusscansdk.core.*
import java.io.File
import java.util.*


/**
 * This fragment represents the second step of the app, recognizing and processing the document.
 */
class DocScanFragment : Fragment(), ScanFragment.CameraCallbackProvider {

    private lateinit var quadrangle: Quadrangle

    private var permissionGranted: Boolean = false

    /**
     * Safe context that should be accessed only when set safely.
     */
    private lateinit var safeContext: Context

    private val scanFragment = ScanFragment()

    /**
     * Global view-model used for storing and retrieving global variables.
     */
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Nullable view binding variable used to inflate layout into it.
     */
    private var _binding: DocScanFragmentBinding? = null

    /**
     * Not-null view binding variable used to reference views from layout.
     */
    private val binding get() = _binding!!

    /**
     * Result launcher for permission requests.
     */
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DocScanFragmentBinding.inflate(inflater, container, false)

        // Set current progress
        viewModel.setProgress(CURRENT_PROGRESS)

        // Load child fragment for scanning documents
        childFragmentManager.beginTransaction().replace(R.id.fm_scan, scanFragment).commit()

        // Register for permission results
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                binding.tvMissingPermission.isVisible = false
                permissionGranted = true
            } else {
                binding.tvMissingPermission.isVisible = true
            }
        }

        checkCameraPermissions()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Retrieve safe context once fragment is attached
        safeContext = context
    }

    override fun onResume() {
        super.onResume()

        if (permissionGranted) {
            startCamera()
        }
    }

    /**
     * Function that launches the camera preview and image analysis.
     *
     * Requires camera permissions to be already granted.
     */
    private fun startCamera() {
        // TODO Uncomment if a license should be used
        // initializeGeniusScanSDK()

        // Configure scan fragment to display a preview, detect the document and take automatically
        // a snapshot
        scanFragment.setOverlayColorResource(R.color.design_default_color_on_primary)
        scanFragment.setPreviewAspectFill(false)
        scanFragment.setRealTimeDetectionEnabled(true)
        scanFragment.setFocusIndicator(binding.fiDefault)
        scanFragment.setAutoTriggerAnimationEnabled(true)
        scanFragment.setBorderDetectorListener(object : BorderDetectorListener {
            override fun onBorderDetectionResult(result: QuadStreamAnalyzer.Result) {
                // Update guidance message and take picture if ready
                updateUserGuidance(result)
                if (result.status == QuadStreamAnalyzer.Status.TRIGGER) {
                    takePicture(result)
                }
            }

            // Create an alert dialog that shows the error message in case border detection fails
            // TODO Check if error message is localized properly
            override fun onBorderDetectionFailure(e: Exception) {
                scanFragment.setPreviewEnabled(false)
                AlertDialog.Builder(safeContext)
                    .setMessage(e.message)
                    .setCancelable(false)
                    .setPositiveButton(
                        android.R.string.ok
                    ) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        })

        // Initialize / Start scan fragment
        scanFragment.initializeCamera()
    }

    /**
     * Updates the bottom user guidance message.
     */
    private fun updateUserGuidance(result: QuadStreamAnalyzer.Result) {
        binding.tvUserGuidance.text =
            if (result.status == QuadStreamAnalyzer.Status.NOT_FOUND || result.resultQuadrangle == null) {
                getString(R.string.detection_status_searching)
            } else if (result.status == QuadStreamAnalyzer.Status.SEARCHING) {
                getString(R.string.detection_status_found)
            } else if (result.status == QuadStreamAnalyzer.Status.ABOUT_TO_TRIGGER) {
                getString(R.string.hold_still)
            } else {
                ""
            }
    }

    /**
     * Takes a picture by triggering a snapshot with the [ScanFragment.takePicture] function
     * and stores the resulting quadrangle for later use.
     */
    private fun takePicture(result: QuadStreamAnalyzer.Result) {
        val file = File(safeContext.externalCacheDir, "original.jpg")
        scanFragment.takePicture(file)
        quadrangle = result.resultQuadrangle
    }

    /**
     * Function that initializes the GeniusScanSDK with a valid license provide by
     * the build configuration variable [BuildConfig.GENIUS_SDK_KEY].
     */
    private fun initializeGeniusScanSDK() {
        try {
            GeniusScanSDK.init(context, BuildConfig.GENIUS_SDK_KEY)
        } catch (e: LicenseException) {
            Log.e("ScanFragment", "License exception occurred", e)
            // TODO Handle properly license exceptions
        }
    }

    /**
     * Function that checks if camera permissions are granted.
     */
    private fun checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    /**
     * Implementation of [CameraManager] callbacks that provides implementation of callback
     * functions when [ScanFragment] completes a snapshot.
     */
    override fun getCameraManagerCallback(): CameraManager.Callback {
        return object : CameraManager.Callback {
            override fun onCameraReady() {}
            override fun onCameraFailure() {}
            override fun onShutterTriggered() {}
            override fun onPictureTaken(cameraOrientation: Int, outputFile: File) {

                // Set configuration for cropping and transforming image once it is saved
                val configuration = ScanProcessor.Configuration(
                    ScanProcessor.PerspectiveCorrection.withQuadrangle(quadrangle),
                    ScanProcessor.CurvatureCorrection.automatic(),
                    ScanProcessor.Enhancement.automatic()
                )

                // Read and process the image snapshot stored.
                val cropped = File(safeContext.externalCacheDir, "cropped.jpg")
                ScanProcessor(safeContext).process(
                    outputFile.absolutePath,
                    cropped.absolutePath,
                    configuration
                )

                // Decode jpg, compress bitmap and store in view model for further
                // TODO Check if file decode is safe and does not block main thread
                val bitmap = BitmapFactory.decodeFile(cropped.absolutePath)
                val pngFile =
                    File(safeContext.getExternalFilesDir(null), "${UUID.randomUUID()}.png")
                compressBitmap(bitmap, pngFile)

                // Store png file to view-model for later processing
                viewModel.setDocument(pngFile)

                // Navigate to next step fragment
                findNavController().navigate(R.id.action_doc_scan_fragment_to_complete_fragment)
            }

            override fun onPreviewFrame(bytes: ByteArray, width: Int, height: Int, format: Int) {}
        }
    }
}

/**
 * Value that represents the current progress of this fragment
 */
private const val CURRENT_PROGRESS = 2