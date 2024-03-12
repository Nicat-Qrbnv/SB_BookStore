package org.ltclab.sb_bookstore.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailingService {

    private final JavaMailSender jms;

    public void sendEmail () {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("test@mail.com");
        msg.setTo("nicat.qurbanov97@gmail.com");
        msg.setSubject("Testing");
        msg.setText("It is a test");
        jms.send(msg);
    }

    public void sendEmailWithAttachments () throws MessagingException {
        MimeMessage msg = jms.createMimeMessage();

        MimeMessageHelper mmh = new MimeMessageHelper(msg, true);

        mmh.setFrom("test@gmail.com");
        mmh.setTo("shibliyevmurad@gmail.com");
        mmh.setSubject("Testing");
        mmh.setText("Don't open the file");
        mmh.addAttachment("DontRead.ME", new ClassPathResource("DontRead.ME"));

        jms.send(msg);
    }
}
