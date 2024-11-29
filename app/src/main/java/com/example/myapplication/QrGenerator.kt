package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class QrGenerator : ComponentActivity() {

    private var qrBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_generator)

        // Get the string from strings.xml
        val qrContent = getString(R.string.qr_password)

        // Reference to the ImageView for displaying the QR code
        val qrImageView: ImageView = findViewById(R.id.idQRcode)

        // Reference to the Buttons
        val buttonQR: Button = findViewById(R.id.buttonGenerate)
        val buttonDownload: Button = findViewById(R.id.buttonDownload)

        // Set a click listener on the button
        buttonQR.setOnClickListener {
            qrBitmap = generateQRCode(qrContent)
            qrImageView.setImageBitmap(qrBitmap)
            qrImageView.visibility = ImageView.VISIBLE
        }

        buttonDownload.setOnClickListener {
            qrBitmap?.let { bitmap ->
                saveBitmapToGallery(bitmap)
            } ?: Toast.makeText(this, "QR no generado aún", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to generate a QR code as a Bitmap
    private fun generateQRCode(content: String): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        return try {
            val bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 500, 500)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) ResourcesCompat.getColor(resources, R.color.black, null) else ResourcesCompat.getColor(resources, R.color.white, null))
                }
            }
            bmp
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val filename = "QR_${System.currentTimeMillis()}.png"
        val fos: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            Toast.makeText(this, "QR guardado en la galería", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
    }
}

