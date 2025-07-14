package com.SJTB.project.gpt;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GptResponseDto {

    private String prosuccyn;
    private String question;
    private String answer;
    private int requesttoken;
    private int responsetoken;
    private String responsecode;

    @Builder
    public GptResponseDto(String prosuccyn, String question, String answer, int requesttoken, int responsetoken, String responsecode){

        this.prosuccyn = prosuccyn;
        this.question = question;
        this.answer = answer;
        this.requesttoken = requesttoken;
        this.responsetoken = responsetoken;
        this.responsecode = responsecode;

    }

}
