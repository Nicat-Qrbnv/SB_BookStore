package org.ltclab.sb_bookstore.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.ltclab.sb_bookstore.service.MailingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class MailingController {

    private final MailingService ms;

    @PostMapping("/send-email")
    public String sendEmail() {
        ms.sendEmail();
        return "Sent!";
    }

    @PostMapping("/send-email-with-attachments")
    public String sendEmailWA() {
        try {
            ms.sendEmailWithAttachments();
            return "Sent!";
        } catch (MessagingException e) {
            return e.getMessage();
        }
    }
}
