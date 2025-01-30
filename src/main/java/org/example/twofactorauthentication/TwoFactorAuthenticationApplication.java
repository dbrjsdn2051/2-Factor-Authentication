package org.example.twofactorauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TwoFactorAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwoFactorAuthenticationApplication.class, args);
    }

}
