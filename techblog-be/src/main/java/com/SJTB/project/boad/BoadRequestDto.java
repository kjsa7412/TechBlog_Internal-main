package com.SJTB.project.boad;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BoadRequestDto {
    private Integer boadid;
    private String userid;
    private String title;
    private String openstatus;
    private Integer thumbnailid;
    private String thumbnailpath;
    private String hashtag;
    private Integer lastmd;
    private List<BoadContRequestDto> conts;
    private String boadConts;
}
