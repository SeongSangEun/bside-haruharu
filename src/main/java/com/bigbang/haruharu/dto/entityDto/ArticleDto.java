package com.bigbang.haruharu.dto.entityDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleDto {

    private Long articleSeq;
    private String subject;
    private String originText;
    private String convertedText;
    private String imageUrl;
    private Integer likeCount;
    private String conceptName;

}
