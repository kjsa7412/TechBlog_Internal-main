package com.SJTB.project.boad;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoadResponseDto {
    private Integer boadId;
    private String title;
    private String summary;
    private String useYN;
    private String openStatus;
    private String[] hashtag;
    private Integer thumbnailid;
    private String thumbnailpath;
    private Integer views;
    private Integer likes;
    private Integer lastMd;
    private List<BoadContResponseDto> conts;
    // 아래 부터는 UserEntity의 정보
    private String userId;
    private String userName;

    @Builder
    public BoadResponseDto(Integer boadId, String title, String summary, String useYN, String openStatus, String[] hashtag,
                           Integer thumbnailid, String thumbnailpath, Integer views, Integer likes, Integer lastMd, List<BoadContResponseDto> conts,
                           String userId, String userName) {
        this.boadId = boadId;
        this.title = title;
        this.summary = summary;
        this.useYN = useYN;
        this.openStatus = openStatus;
        this.hashtag = hashtag;
        this.thumbnailid  = thumbnailid;
        this.thumbnailpath = thumbnailpath;
        this.views = views;
        this.likes = likes;
        this.conts = conts;
        this.lastMd = lastMd;
        // 아래 부터는 UserEntity의 정보
        this.userId = userId;
        this.userName = userName;
    }
}
