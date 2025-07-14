package com.SJTB.project.img;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class ImgRequestDto {
    private String userid;
    private String imgcate;
    private Integer boadid;
}
