package com.bigbang.haruharu.dto.request.clova;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClovaChatDto {
    private String role;
    private String content;
}
