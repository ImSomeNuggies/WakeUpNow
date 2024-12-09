package com.example.myapplication.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*


class JavaMailAPITest {


    @Test
    fun `doInBackground should handle exception and return error message`() {
        // Arrange
        val email = "test@example.com"
        val subject = "Test Subject"
        val message = "Test Message"
        val emailFrom = "sender@example.com"
        val password = "password123"

        val mockMailSessionProvider = mock(MailSessionProvider::class.java)
        `when`(mockMailSessionProvider.createSession(emailFrom, password)).thenThrow(RuntimeException("SMTP error"))

        val javaMailAPI = JavaMailAPI(
            email = email,
            subject = subject,
            message = message,
            emailFrom = emailFrom,
            password = password,
            mailSessionProvider = mockMailSessionProvider
        )

        // Act
        val result = javaMailAPI.doInBackground()

        // Assert
        assertEquals("SMTP error", result)
    }
}
