package com.bigbang.haruharu.dto.request.clova;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ClovaRequestDto {

    private String text;
    private String start;
    private String restart;
    private Boolean includeTokens;
    private Double topP;
    private Integer topK;
    private Integer maxTokens;
    private Double temperature;
    private Double repeatPenalty;
    private List<String> stopBefore;
    private Boolean includeAiFilters;

}
