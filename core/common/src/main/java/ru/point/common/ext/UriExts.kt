package ru.point.common.ext

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun Uri.uriToMultipart(context: Context): MultipartBody.Part {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(this)
    val fileName = getFileName(this, contentResolver) ?: "image.jpg"
    val tempFile = File.createTempFile("upload_", fileName, context.cacheDir).apply {
        outputStream().use { fileOut -> inputStream?.copyTo(fileOut) }
    }

    val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData("photos", fileName, requestBody)
}

private fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
    val cursor = contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        it.getString(nameIndex)
    }
}