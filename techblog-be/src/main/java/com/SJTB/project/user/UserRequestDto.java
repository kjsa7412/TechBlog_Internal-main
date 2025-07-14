package com.SJTB.project.user;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRequestDto {
    private String userId;
    private String userPw;
    private String userName;
    private Integer profilePic;
    private String profileCont;
    private String delState;

    @Builder
    public UserRequestDto(String userId, String userPw, String userName, String profileCont, Integer profilePic, String delState) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.profileCont = profileCont;
        this.profilePic = profilePic;
        this.delState = delState;
    }
}