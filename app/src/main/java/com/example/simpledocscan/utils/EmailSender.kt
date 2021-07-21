package com.example.simpledocscan.utils

import android.content.Intent
import android.net.Uri
import java.io.File

fun generateEmailIntent(receiver: String, subject: String, attachment: File): Intent {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(receiver))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    val imageUri = Uri.parse("file://${attachment.absolutePath}")
    intent.putExtra(Intent.EXTRA_STREAM, imageUri)
    return intent
}