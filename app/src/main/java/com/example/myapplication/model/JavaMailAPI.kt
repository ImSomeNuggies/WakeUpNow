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
    private val attachmentPath: String? = null // Ruta del archivo adjunto
) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String? {
        try {
            // Configuración del servidor SMTP
            val props = Properties()
            props["mail.smtp.host"] = "smtp.gmail.com"
            props["mail.smtp.socketFactory.port"] = "465"
            props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.port"] = "465"

            // Autenticación
            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(emailFrom, password)
                }
            })

            // Creación del correo
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(emailFrom))
            mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            mimeMessage.subject = subject

            // Crear el cuerpo del correo
            val messageBodyPart = MimeBodyPart()
            messageBodyPart.setText(message)

            // Manejo del adjunto si se proporciona
            val multipart = MimeMultipart()
            multipart.addBodyPart(messageBodyPart)

            if (attachmentPath != null) {
                val attachmentPart = MimeBodyPart()
                val source = FileDataSource(File(attachmentPath))
                attachmentPart.dataHandler = DataHandler(source)
                attachmentPart.fileName = File(attachmentPath).name
                multipart.addBodyPart(attachmentPart)
            }

            mimeMessage.setContent(multipart);

            // Enviar correo
            Transport.send(mimeMessage)

        } catch (e: Exception) {
            e.printStackTrace()
            return e.message
        }
        return null
    }
}
