package com.example.myapplication.model

import android.os.AsyncTask
import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class JavaMailAPI(
    private val email: String,
    private val subject: String,
    private val message: String,
    private val emailFrom: String,
    private val password: String,
    private val attachmentPath: String? = null,
    private val mailSessionProvider: MailSessionProvider = DefaultMailSessionProvider() // Usa la implementación por defecto
) : AsyncTask<Void, Void, String>() {

    public override fun doInBackground(vararg params: Void?): String? {
        try {
            val session = mailSessionProvider.createSession(emailFrom, password)
            val mimeMessage = MimeMessage(session).apply {
                setFrom(InternetAddress(emailFrom))
                addRecipient(Message.RecipientType.TO, InternetAddress(email))
                subject = this@JavaMailAPI.subject
            }

            val messageBodyPart = MimeBodyPart().apply { setText(message) }
            val multipart = MimeMultipart().apply { addBodyPart(messageBodyPart) }

            attachmentPath?.let {
                val attachmentPart = MimeBodyPart().apply {
                    dataHandler = DataHandler(FileDataSource(File(it)))
                    fileName = File(it).name
                }
                multipart.addBodyPart(attachmentPart)
            }

            mimeMessage.setContent(multipart)
            Transport.send(mimeMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message
        }
        return null
    }
}
