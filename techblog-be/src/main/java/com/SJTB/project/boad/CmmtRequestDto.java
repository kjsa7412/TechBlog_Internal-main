package com.SJTB.project.boad;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CmmtRequestDto {

    private Integer cmtId;
    private Integer boadId;
    private String userid;
    private String cmt;
    private String useyn;

}
