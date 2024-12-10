package com.example.myapplication.viewmodel

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.model.JavaMailAPI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.*

class QrGeneratorViewModel (application: Application) : AndroidViewModel(application) {

    fun saveBitmapToGallery(bitmap: Bitmap,context: Context) {
        val filename = "QR_${System.currentTimeMillis()}.png"
        val fos: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let {
                resolver.openOutputStream(it)
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            Toast.makeText(context, "QR guardado en la galería", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
    }

    fun sendEmailWithSmtp(bitmap: Bitmap, context: Context, recipientEmail: String, subject: String, message: String) {
        // Convertir el Bitmap en un archivo temporal
        val file = saveBitmapToTempFile(bitmap, context)

        if (file != null) {
            val smtpServer = "smtp.gmail.com"  // Cambia esto con el servidor SMTP (e.g., smtp.gmail.com)
            val smtpPort = "587"  // Cambia esto según el puerto que utilices (587 para TLS, 465 para SSL)
            val senderEmail = "wakemeupnowunizar@gmail.com"  // Tu correo electrónico
            val senderPassword = "qjmi oobs bbql lpbj"  // Tu contraseña InesJuanMiguelMateo

            // Configurar las propiedades del servidor SMTP
            val properties = Properties().apply {
                put("mail.smtp.host", smtpServer)
                put("mail.smtp.port", smtpPort)
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")  // Habilitar STARTTLS
            }

            // Autenticación en el servidor SMTP
            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPassword)
                }
            })

            try {
                val javaMailAPI = JavaMailAPI(
                    email = "miguellasaosaisac@gmail.com",
                    subject = subject,
                    message = message,
                    emailFrom = "wakemeupnowunizar@gmail.com",
                    password = "qjmi oobs bbql lpbj",
                    attachmentPath = file.absolutePath // Ruta del archivo temporal
                )
                javaMailAPI.execute()
                Toast.makeText(context, "Correo enviado", Toast.LENGTH_SHORT).show()

            } catch (e: MessagingException) {
                e.printStackTrace()
                Toast.makeText(context, "Error al enviar el correo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "No se pudo generar el archivo adjunto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveBitmapToTempFile(bitmap: Bitmap, context: Context): File? {
        return try {
            // Crear un archivo temporal
            val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.png")
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos) // Guardar el Bitmap en el archivo
            fos.flush()
            fos.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}