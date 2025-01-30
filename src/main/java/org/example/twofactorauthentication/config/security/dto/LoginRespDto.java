package org.example.twofactorauthentication.config.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRespDto {
    private String accessToken;
}
