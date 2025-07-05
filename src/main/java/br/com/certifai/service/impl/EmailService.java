package br.com.certifai.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private final SendGrid sendGrid;

    @Value("${sendgrid.sender-email}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    public EmailService(@Value("${sendgrid.api-key}") String sendGridApiKey) {
        this.sendGrid = new SendGrid(sendGridApiKey);
    }

    @Async
    public void sendVerificationEmail(String toEmail, String username, String token) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        String subject = "Verifique seu E-mail para Ativar sua Conta";
        String verificationLink = baseUrl + "/auth/verify?token=" + token;

        String htmlContent = String.format("""
            <p>Olá %s,</p>
            <p>Obrigado por se cadastrar em nossa aplicação! Por favor, clique no link abaixo para verificar seu endereço de e-mail e ativar sua conta:</p>
            <p><a href="%s">Verificar E-mail Agora</a></p>
            <p>Este link é válido por 24 horas.</p>
            <p>Se você não solicitou este e-mail, pode ignorá-lo com segurança.</p>
            <p>Atenciosamente,<br>Sua Equipe</p>
            """, username, verificationLink);

        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            System.out.println("E-mail de verificação enviado para " + toEmail + ". Status Code: " + response.getStatusCode());
            System.out.println("Corpo da Resposta: " + response.getBody());
        } catch (IOException ex) {
            System.err.println("Erro ao enviar e-mail de verificação via SendGrid para " + toEmail + ": " + ex.getMessage());
        }
    }
}
