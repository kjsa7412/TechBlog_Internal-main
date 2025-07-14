package com.SJTB.project.auth;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestDto {
    private String userId;
    private String userPw;
    private String loginToken;

    @Builder
    public AuthRequestDto(String userId, String userPw, String loginToken) {
        this.userId = userId;
        this.userPw = userPw;
        this.loginToken = loginToken;
    }
}