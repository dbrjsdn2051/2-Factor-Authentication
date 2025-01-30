package org.example.twofactorauthentication.controller;

import lombok.RequiredArgsConstructor;
import org.example.twofactorauthentication.common.format.ApiResult;
import org.example.twofactorauthentication.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/verify")
    public ResponseEntity<ApiResult<Void>> verifyEmail(@RequestParam String email, @RequestParam String code) {
        authService.verificationEmail(email, code);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResult.success(null));
    }
}
