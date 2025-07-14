package com.SJTB.project.gpt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GptRequestDto {
    private String userid;
    private String question;
}
