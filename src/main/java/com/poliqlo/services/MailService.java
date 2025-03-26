package com.poliqlo.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender sender;
    private final  SpringTemplateEngine templateEngine;
    List<MailInfo> list = new ArrayList<>();

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class MailInfo {

        String from;
        String to;
        String subject;
        String body;
        String attachments;

        public MailInfo(String to, String subject, String body) {
            this.from = "chucnb9@gmail.com";
            this.to = to;
            this.subject = subject;
            this.body = body;
        }

    }

    public void send(MailInfo mail) throws MessagingException, IOException {
        // Tạo message
        MimeMessage message = sender.createMimeMessage();
        // Sử dụng Helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getBody(), true);
        helper.setReplyTo(mail.getFrom());

        if (mail.getAttachments() != null) {
            FileSystemResource file = new FileSystemResource(new File(mail.getAttachments()));
            helper.addAttachment(mail.getAttachments(), file);
        }

        // Gửi message đến SMTP server
        sender.send(message);

    }

    public void send(String from,String to, String subject, String body, String attachment) throws MessagingException, IOException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setReplyTo(from);

        if (attachment!= null) {
            FileSystemResource file = new FileSystemResource(new File(attachment));
            helper.addAttachment(attachment, file);
        }

        sender.send(message);

    }
    public void send(String to, String subject, String body, String attachment) throws MessagingException, IOException {
        send("chucnb9@gmail.com", to, subject, body, attachment);
    }
    public void send(String to, String subject, String tempalte, Context context, String attachment) throws MessagingException, IOException {
        String htmlContent = templateEngine.process(tempalte, context);
        send("chucnb9@gmail.com", to, subject, htmlContent, attachment);
    }

}
