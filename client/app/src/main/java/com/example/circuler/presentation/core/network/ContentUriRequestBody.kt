package com.example.circuler.presentation.core.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink

class ContentUriRequestBody(
    context: Context,
    private val uri: Uri?
) : RequestBody() {
    private val contentResolver = context.contentResolver

    private var fileName = ""
    private var size = -1L
    private var compressedImage: ByteArray? = null

    init {
        if (uri != null) {
            contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    size =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                    fileName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                }
            }

            compressBitmap()
        }
    }

    private fun compressBitmap() {
        if (uri != null) {
            var originalBitmap: Bitmap
            val exif: ExifInterface

            contentResolver.openInputStream(uri).use { inputStream ->
                if (inputStream == null) return
                val option = BitmapFactory.Options().apply {
                    inSampleSize = calculateInSampleSize(this, MAX_WIDTH, MAX_HEIGHT)
                }
                originalBitmap = BitmapFactory.decodeStream(inputStream, null, option) ?: return
                exif = ExifInterface(inputStream)
            }

            var orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> orientation = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> orientation = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> orientation = 270
            }

            if (orientation >= 90) {
                val matrix = Matrix().apply {
                    setRotate(orientation.toFloat())
                }

                val rotatedBitmap = Bitmap.createBitmap(
                    originalBitmap,
                    0,
                    0,
                    originalBitmap.width,
                    originalBitmap.height,
                    matrix,
                    true
                )
                originalBitmap.recycle()
                originalBitmap = rotatedBitmap
            }

            val outputStream = ByteArrayOutputStream()
            val imageSizeMb = size / (MAX_WIDTH * MAX_HEIGHT.toDouble())
            outputStream.use {
                val compressRate = ((IMAGE_SIZE_MB / imageSizeMb) * 100).toInt()
                originalBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    if (imageSizeMb >= IMAGE_SIZE_MB) compressRate else 100,
                    it
                )
            }
            compressedImage = outputStream.toByteArray()
            size = compressedImage?.size?.toLong() ?: -1L
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun getFileName() = fileName

    override fun contentLength(): Long = size

    override fun contentType(): MediaType? =
        uri?.let { contentResolver.getType(it)?.toMediaTypeOrNull() }

    override fun writeTo(sink: BufferedSink) {
        compressedImage?.let(sink::write)
    }

    fun toFormData(name: String) = MultipartBody.Part.createFormData(name, getFileName(), this)

    companion object {
        const val IMAGE_SIZE_MB = 1
        const val MAX_WIDTH = 1024
        const val MAX_HEIGHT = 1024
    }
}
