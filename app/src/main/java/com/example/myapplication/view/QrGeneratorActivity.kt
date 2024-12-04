package com.example.myapplication.view

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
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat

import com.example.myapplication.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import com.example.myapplication.viewmodel.QrGeneratorViewModel
import com.example.myapplication.viewmodel.QrGeneratorViewModelFactory


class QrGeneratorActivity : ComponentActivity() {

    private var qrBitmap: Bitmap? = null
    // Usamos el ViewModel con un ViewModelFactory
    private val viewModel: QrGeneratorViewModel by viewModels { QrGeneratorViewModelFactory(application) }

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
        val buttonSend: Button = findViewById(R.id.buttonSend)


        // Set a click listener on the button
        buttonQR.setOnClickListener {
            qrBitmap = generateQRCode(qrContent)
            qrImageView.setImageBitmap(qrBitmap)
            qrImageView.visibility = ImageView.VISIBLE
        }

        buttonDownload.setOnClickListener {
            qrBitmap?.let { bitmap ->
                viewModel.saveBitmapToGallery(bitmap,this);
            } ?: Toast.makeText(this, "QR no generado aún", Toast.LENGTH_SHORT).show()
        }

        buttonSend.setOnClickListener {
            qrBitmap?.let { bitmap ->
                viewModel.sendEmailWithSmtp(bitmap,this, "miguellasaosaisac@gmail.com","QR - ALARMA", "Qr Alarma");
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
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) ResourcesCompat.getColor(resources,
                        R.color.black, null) else ResourcesCompat.getColor(resources,
                        R.color.white, null))
                }
            }
            bmp
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

}

