package com.newfangled.flockbackend.global.infra.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendMail(String teamName, long id, String email, long memberId) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setSubject("[끼리끼리]");
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(email);

            Context context = new Context();
            context.setVariable("team", teamName);
            context.setVariable("id", id);
            context.setVariable("member_id", memberId);

            String html = templateEngine.process("emailTemplate", context);
            mimeMessageHelper.setText(html, true);

            javaMailSender.send(mimeMailMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            result = false;
        }

        return result;
    }
}
