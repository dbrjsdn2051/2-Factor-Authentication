package org.example.twofactorauthentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.twofactorauthentication.common.error.BusinessException;
import org.example.twofactorauthentication.common.error.ErrorCode;
import org.example.twofactorauthentication.controller.dto.rep.UserCreateRepDto;
import org.example.twofactorauthentication.controller.dto.resp.UserCreateRespDto;
import org.example.twofactorauthentication.controller.dto.resp.UserInfoRespDto;
import org.example.twofactorauthentication.domain.user.User;
import org.example.twofactorauthentication.domain.user.UserRepository;
import org.example.twofactorauthentication.event.UserRegisterEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public UserCreateRespDto register(UserCreateRepDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        boolean isExistsNickname = userRepository.existsByNickname(dto.getNickname());
        if (isExistsNickname) {
            throw new BusinessException(ErrorCode.EXISTS_ALREADY_USER);
        }

        boolean isExistsEmail = userRepository.existsByEmail(dto.getEmail());
        if (isExistsEmail) {
            throw new BusinessException(ErrorCode.EXISTS_ALREADY_EMAIL);
        }

        User user = UserCreateRepDto.from(dto, encodedPassword);
        User savedUser = userRepository.save(user);

        applicationEventPublisher.publishEvent(new UserRegisterEvent(dto.getEmail()));

        return new UserCreateRespDto(savedUser);
    }

    public UserInfoRespDto findUser(String nickname) {
        User findUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new UserInfoRespDto(findUser);
    }
}
