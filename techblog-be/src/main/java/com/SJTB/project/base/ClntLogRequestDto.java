package com.SJTB.project.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClntLogRequestDto {

    private int userid;
    private String actioncate;
    private int contid;
    private String useragent;

    @Builder
    public ClntLogRequestDto(int userid, String actioncate, int contid, String useragent){
        this.userid = userid;
        this.actioncate = actioncate;
        this.contid = contid;
        this.useragent = useragent;
    }

}
