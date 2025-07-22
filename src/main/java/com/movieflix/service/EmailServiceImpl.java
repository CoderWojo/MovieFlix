package com.movieflix.service;

import com.movieflix.dto.MailBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${project.mail.name}")
    private String from;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

//    TODO: może by sprawdzal czy dany mail faktycznie istnieje w bazie światowej
    public void sendSimpleMailMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setText(mailBody.text());
        message.setTo(mailBody.to());
        message.setSubject(mailBody.subject());

        javaMailSender.send(message);
    }
}
