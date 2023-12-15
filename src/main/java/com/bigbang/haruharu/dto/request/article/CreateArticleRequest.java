package com.bigbang.haruharu.dto.request.article;

import com.bigbang.haruharu.domain.entity.base.BaseEntity;
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

    @Schema( type = "string", example = "사용자가 작성한 제목", description="사용자가 제목을 직접 입력한다면 제목을 입력해주세요.")
    private String subject;
    @Schema( type = "string", example = "Y / N", description="클로바 AI가 제목 지어주길 희망한다면 Y, 그렇지않으면 N")
    private BaseEntity.YN createAiSubject;

//    private String subject;
//    @Schema( type = "[string, string]", example = "해쉬태그 리스트", description="입력받은 해쉬태그 리스트를 입력해주세요.")
//    private List<String> hashTagList;
}
