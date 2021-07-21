package com.malliaridis.android.simpledocscan.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import java.io.File
import java.io.FileOutputStream

/**
 * Function that takes a bitmap and output file and compresses the bitmap. Format and quality are
 * optional and default to PNG 100% (best quality).
 *
 * @param bitmap the bitmap to compress
 * @param outputFile the output file the compressed bitmap is stored
 * @param format optional bitmap compression format, defaults to [CompressFormat.PNG]
 * @param quality optional compression quality, defaults to 100 (best quality)
 */
fun compressBitmap(
    bitmap: Bitmap,
    outputFile: File,
    format: CompressFormat = CompressFormat.PNG,
    quality: Int = 100
) {
    val out = FileOutputStream(outputFile)
    bitmap.compress(format, quality, out)
    out.close()
}
