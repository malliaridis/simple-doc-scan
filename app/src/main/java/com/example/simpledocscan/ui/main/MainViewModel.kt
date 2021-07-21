package com.example.simpledocscan.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class MainViewModel : ViewModel() {

    /**
     * Mutable progress state. Represents the current progress.
     */
    private val _progress = MutableLiveData<Int>().apply { value = 1 }

    /**
     * Live data that represents the current progress state. Can be used to subscribe for
     * progress updates set via [setProgress].
     *
     * @see setProgress
     */
    val progressLive: LiveData<Int> = _progress

    /**
     * Mutable email address state. Represents the current email address provided.
     */
    private val _emailAddress = MutableLiveData<String>().apply { value = "" }

    /**
     * Live data that represents the current email address. Can be used to store the email
     * address globally and retrieve it when necessary.
     */
    val emailAddressLive: LiveData<String> = _emailAddress


    /**
     * Mutable document file state. Represents the scanned document as file.
     */
    private val _document = MutableLiveData<File>()

    /**
     * Live data that represents the document scanned. Can be used to store the document
     * globally and retrieve it when necessary.
     */
    val documentLive: LiveData<File> = _document

    /**
     * Function to update the global progress state.
     *
     * @param progress Value that represents the current progress. Possible values are
     * 1, 2 or 3. Other values are ignored.
     */
    fun setProgress(progress: Int) {
        // Ignore invalid progress states
        if (progress != 1 && progress != 2 && progress != 3) return

        _progress.value = progress
    }

    /**
     * Function to set the email address to use for sending the final document.
     * This function does not validate the input and expects valid email addresses.
     */
    fun setEmailAddress(email: String) {
        _emailAddress.value = email
    }

    /**
     * Function to store the scanned document.
     */
    fun setDocument(document: File) {
        _document.value = document
    }
}