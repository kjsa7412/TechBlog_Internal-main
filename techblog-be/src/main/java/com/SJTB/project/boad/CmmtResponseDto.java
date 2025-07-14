package com.SJTB.project.boad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmmtResponseDto {
    private Integer cmtId;
    private Integer boadId;
    private String userId;
    private String userName;
    private String profilePicPath;
    private String cmt;
    private String writeDate;
}
