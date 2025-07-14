package com.SJTB.project.boad;

import lombok.*;

/*게시물에 대한 조회 및 좋아요 수*/
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoadCountResponseDto {
    private int viewCnt;
    private int likeCnt;

    @Builder
    public BoadCountResponseDto(int viewCnt, int likeCnt){
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
    }
}
