package com.example.myapplication.model

import com.example.myapplication.model.MailSessionProvider
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import java.util.*

class DefaultMailSessionProvider : MailSessionProvider {
    override fun createSession(emailFrom: String, password: String): Session {
        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "465")
        }
        return Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(emailFrom, password)
            }
        })
    }
}
