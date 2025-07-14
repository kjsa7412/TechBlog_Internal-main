package com.SJTB.project.auth;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthResponseDto {
    private String userId;
    private String userName;
    private String profileCont;
    private Integer profilePic;
    private String profilePicPath;
    private String userAuth;

    @Builder
    public AuthResponseDto(String userId, String userName, String profileCont, Integer profilePic,
                           String profilePicPath, String userAuth) {
        this.userId = userId;
        this.userName = userName;
        this.profileCont = profileCont;
        this.profilePic = profilePic;
        this.profilePicPath = profilePicPath;
        this.userAuth = userAuth;
    }
}