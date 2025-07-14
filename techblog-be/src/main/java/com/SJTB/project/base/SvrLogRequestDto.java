package com.SJTB.project.base;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SvrLogRequestDto {

    private int userid;
    private String actioncate;
    private String prosuccyn;
    private String responsecont;

    @Builder
    public SvrLogRequestDto(int userid, String actioncate, String prosuccyn, String responsecont){
        this.userid = userid;
        this.actioncate = actioncate;
        this.prosuccyn = prosuccyn;
        this.responsecont = responsecont;
    }
}
