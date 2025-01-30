package org.example.twofactorauthentication.config.security;

import lombok.RequiredArgsConstructor;
import org.example.twofactorauthentication.common.error.ErrorCode;
import org.example.twofactorauthentication.common.error.SecurityFilterChainException;
import org.example.twofactorauthentication.domain.user.User;
import org.example.twofactorauthentication.domain.user.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginAuthentication loginAuthentication = (LoginAuthentication) authentication;
        String nickname = authentication.getName();
        String password = (String) loginAuthentication.getCredentials();

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new SecurityFilterChainException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SecurityFilterChainException(ErrorCode.MISS_MATCH_PASSWORD);
        }

        return new LoginAuthentication(AuthUser.of(user));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginAuthentication.class.isAssignableFrom(authentication);
    }
}
