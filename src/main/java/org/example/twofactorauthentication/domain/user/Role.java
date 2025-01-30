package org.example.twofactorauthentication.domain.user;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    VERIFY_USER("ROLE_VERIFY_USER");

    private final String authorityName;

    Role(String authorityName) {
        this.authorityName = authorityName;
    }
}
