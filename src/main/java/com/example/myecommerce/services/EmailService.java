package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final PasswordRecoveryService passwordRecoveryService;

    @Transactional
    @Async
    public void sendMail(String email) throws Exception {
//        System.out.println("EmailService looking for customer: " + email);
//        boolean userFound = userService.userExist(email);
//        if (userFound) {
//            String token = UUID.randomUUID().toString();
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(email);
//            message.setFrom("MyECommerce@MyEcommerce.com");
//            message.setSubject("Password Recovery");
//            message.setText("Here you will recover your password.");
//
//            System.out.println(message.toString());
//            mailSender.send(message);
//        }
    }

    public void sendHtmlMail(
            String email
    ) throws MessagingException {
        MimeMessage message= mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("MyECommerce@MyEcommerce.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("Password Recovery| HTML");

        String htmlContent ="Aqui debe ir mi html para el mail";

        message.setContent(htmlContent, "text/html; charset=utf-");

        mailSender.send(message);
    }

    @Transactional
    @Async
    public void sendHtmlTemplateMail(
            String email
    ) throws MessagingException, IOException {
        System.out.println("EmailService looking for customer: " + email);
        User userFound = userService.userExist(email);
        String token = UUID.randomUUID().toString();
        System.out.println("customer found and token generated");
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("MyECommerce@MyEcommerce.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, userFound.getEmail());
        message.setSubject("Password Recovery| HTML");
        System.out.println("looking for template");
        String template = getTemplate("password-recovery-mail");
        System.out.println("template found, replacing content");
        String content = template
                .replace("${name}", userFound.getName()+" "+userFound.getFirstName())
                .replace("${recovery_url}", "http://localhost:8081/reset_password?token="+token);
        System.out.println("template content replaced");
        message.setContent(content, "text/html; charset=utf-8");
        System.out.println("Sending message");
        mailSender.send(message);
        passwordRecoveryService.startRecovery(userFound, token);
    }

    public String getTemplate(String filePath) throws IOException {
        //Usar classpathResource dirige la busqueda a src/main/resources
        ClassPathResource resource = new ClassPathResource("/templates/"+filePath+".html");
        System.out.println("getTemplate function trying to get template");
        byte[] path = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(path, StandardCharsets.UTF_8);
    }
}
