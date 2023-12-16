package com.bigbang.haruharu.advice.payload;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_PARAMETER(400, null, "잘못된 요청 데이터 입니다."),
    INVALID_REPRESENTATION(400, null, "잘못된 표현 입니다."),
    INVALID_FILE_PATH(400, null, "잘못된 파일 경로 입니다."),
    INVALID_OPTIONAL_ISPRESENT(400, null, "해당 값이 존재하지 않습니다."),
    INVALID_CHECK(400, null, "해당 값이 유효하지 않습니다."),
    INVALID_AUTHENTICATION(400, null, "잘못된 인증입니다."),
    INVALID_ARTICLE(400, null, "이미 삭제되었거나, 존재하지 않는 글입니다."),
    OVER_DAILYCOUNT(400, null, "오늘 글 생성한도를 초과하였습니다."),
    EXIST_TODAY_ARTICLE(400, null, "오늘 글을 이미 작성하였습니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
