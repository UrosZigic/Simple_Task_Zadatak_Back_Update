package com.example.ordinacija.service;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    public void posaljiEmail(String to, String naslov, String tekst) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("uros.zigic@gmail.com");
        message.setTo(to);
        message.setSubject(naslov);
        message.setText(tekst);

        mailSender.send(message);
    }
}
