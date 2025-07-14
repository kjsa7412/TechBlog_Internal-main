package com.SJTB.project.img;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImgResponseDto {
    private Integer imgid;
    private String imgpath;
    private String imgname;
    private String imghashname;
    private String imgfullpath;
}
