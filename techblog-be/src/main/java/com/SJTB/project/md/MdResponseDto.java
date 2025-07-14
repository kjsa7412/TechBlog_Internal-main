package com.SJTB.project.md;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MdResponseDto {
    private String slug;
    private String title;
    private String description;
    private String thumbnail;
    private String keywords;
    private String author;
    private String datePublished;
    private String dateModified;
    private String content;
}
