package org.example.twofactorauthentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.twofactorauthentication.common.encrypto.EncryptionUtil;
import org.example.twofactorauthentication.common.error.BusinessException;
import org.example.twofactorauthentication.common.error.ErrorCode;
import org.example.twofactorauthentication.domain.user.User;
import org.example.twofactorauthentication.domain.user.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;

    @Transactional
    public void verificationEmail(String email, String code) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String decryptedEmail = encryptionUtil.decrypt(email);
        String storedCode = ops.get(decryptedEmail);
        if (!storedCode.equals(code)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_CODE);
        }
        redisTemplate.delete(decryptedEmail);

        User findUser = userRepository.findByEmail(decryptedEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        findUser.addAuthorizationRole();
        userRepository.save(findUser);
    }
}
