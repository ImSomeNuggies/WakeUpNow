package com.example.myapplication.model

import javax.mail.Session

interface MailSessionProvider {
    fun createSession(emailFrom: String, password: String): Session
}