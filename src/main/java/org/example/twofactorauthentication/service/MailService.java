package org.example.twofactorauthentication.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.twofactorauthentication.common.encrypto.EncryptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EncryptionUtil encryptionUtil;
    private static final String MAIL_SUBJECT = "인증 코드 안내";

    @Value("${spring.mail.username}")
    String host;

    public String sendMail(String to) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        Context context = new Context();
        String code = generateVerificationCode();
        context.setVariable("code", code);
        context.setVariable("email", encryptionUtil.encrypt(to));

        String htmlContent = templateEngine.process("email/verification", context);

        mimeMessageHelper.setFrom(host);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(MAIL_SUBJECT);
        mimeMessageHelper.setText(htmlContent, true);

        javaMailSender.send(message);

        return code;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        IntStream.range(0, 6).forEach(e -> code.append(random.nextInt(10)));
        return code.toString();
    }

}
