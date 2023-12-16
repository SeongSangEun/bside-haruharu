package com.bigbang.haruharu.dto.entityDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ArticleDto {

    private Long articleSeq;
    private String subject;
    private String originText;
    private String convertedText;
    private String imageUrl;
    private Integer likeCount;
    private String conceptName;
    private boolean isPostLiked;
    private LocalDate createdDate;
    private String hashTags;

    public ArticleDto(Long articleSeq, String subject, String originText, String convertedText, String imageUrl, Integer likeCount, String conceptName, boolean isPostLiked, LocalDateTime createdDate, String hashTags) {
        this.articleSeq = articleSeq;
        this.subject = subject;
        this.originText = originText;
        this.convertedText = convertedText;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.conceptName = conceptName;
        this.isPostLiked = isPostLiked;
        this.createdDate = createdDate.toLocalDate();
        this.hashTags = hashTags;
    }
}
