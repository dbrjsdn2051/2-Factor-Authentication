package org.example.twofactorauthentication.service;

import lombok.RequiredArgsConstructor;
import org.example.twofactorauthentication.common.error.BusinessException;
import org.example.twofactorauthentication.common.error.ErrorCode;
import org.example.twofactorauthentication.controller.dto.rep.UserCreateRepDto;
import org.example.twofactorauthentication.controller.dto.resp.UserCreateRespDto;
import org.example.twofactorauthentication.controller.dto.resp.UserInfoRespDto;
import org.example.twofactorauthentication.domain.user.User;
import org.example.twofactorauthentication.domain.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserCreateRespDto register(UserCreateRepDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        userRepository.findByNickname(dto.getNickname())
                .ifPresent(user -> {
                    throw new BusinessException(ErrorCode.EXISTS_ALREADY_USER);
                });

        User user = UserCreateRepDto.from(dto, encodedPassword);
        User savedUser = userRepository.save(user);
        return new UserCreateRespDto(savedUser);
    }

    public UserInfoRespDto findUser(String nickname) {
        User findUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new UserInfoRespDto(findUser);
    }
}
