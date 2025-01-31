package org.example.twofactorauthentication.event;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.twofactorauthentication.service.MailService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisterEventListener {

    private final MailService mailService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final long VERIFICATION_CODE_EXPIRATION = 10 * 60;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistration(UserRegisterEvent event) {
        try {
            String code = mailService.sendMail(event.email());
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(event.email(), code, Duration.ofSeconds(VERIFICATION_CODE_EXPIRATION));
        } catch (MessagingException e) {
            log.warn("Mail 전송 실패 : {}", e.getMessage());
        }
    }
}
