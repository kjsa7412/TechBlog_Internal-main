package com.SJTB.project.boad;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoadContResponseDto {
    private int contid;
    private int boadid;
    private String contcate;
    private String cont;

    @Builder
    public BoadContResponseDto(int contid, int boadid, String contcate, String cont){
        this.contid = contid;
        this.boadid = boadid;
        this.contcate = contcate;
        this.cont = cont;
    }
}
