package org.example.tttn.service.provider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.tttn.service.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

/**
 * Implementation của Email Service
 * Sử dụng JavaMailSender và Thymeleaf template để gửi email
 */
@Service
public class EmailServiceImplement implements IEmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine emailTemplateEngine;
    private final String fromEmail;
    private final String companyName;
    private final String supportUrl;

    public EmailServiceImplement(JavaMailSender javaMailSender,
                                 TemplateEngine templateEngine,
                                 @Qualifier("fromEmail") String fromEmail,
                                 @Qualifier("companyName") String companyName,
                                 @Qualifier("supportUrl") String supportUrl) {
        this.javaMailSender = javaMailSender;
        this.emailTemplateEngine = templateEngine;
        this.fromEmail = fromEmail;
        this.companyName = companyName;
        this.supportUrl = supportUrl;
    }

    /**
     * Gửi email reset mật khẩu cho người dùng.
     */
    @Override
    public boolean sendPasswordResetEmail(String toEmail, String username, String newPassword) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Reset Mật Khẩu - " + companyName);

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("newResetPassword", newPassword);
            context.setVariable("companyName", companyName);
            context.setVariable("supportUrl", supportUrl);
            context.setVariable("year", LocalDateTime.now().getYear());

            String htmlContent = emailTemplateEngine.process("reset-password", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            return true;

        } catch (Exception e) {
            return false;
        }
    }



}
