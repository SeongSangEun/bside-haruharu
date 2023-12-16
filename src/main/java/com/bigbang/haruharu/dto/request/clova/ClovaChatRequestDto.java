package com.bigbang.haruharu.dto.request.clova;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ClovaChatRequestDto {

    private List<ClovaChatDto> messages;
    private Double topP;
    private Integer topK;
    private Integer maxTokens;
    private Double temperature;
    private Double repeatPenalty;
    private List<String> stopBefore;
    private Boolean includeAiFilters;
}
