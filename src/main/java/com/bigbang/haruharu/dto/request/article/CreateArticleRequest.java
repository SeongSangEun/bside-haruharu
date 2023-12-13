package com.bigbang.haruharu.dto.request.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class CreateArticleRequest {

    @Schema( type = "long", example = "long", description="컨셉 번호를 입력해주세요.")
    private Long conceptSeq;

    @Schema( type = "string", example = "원본 메시지", description="원본 메시지 입니다.")
    private String inputMessage;

    @Schema( type = "string", example = "이미지 URL", description="이미지 저장 API 호출후 응답받은 url 을 입력해주세요.")
    private String imageUrl;
//    private String subject;
    @Schema( type = "[string, string]", example = "해쉬태그 리스트", description="입력받은 해쉬태그 리스트를 입력해주세요.")
    private List<String> hashTagList;
}
