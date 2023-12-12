package com.bigbang.haruharu.dto.response.clova;

import lombok.Getter;

import java.util.List;

@Getter
public class ResultResponseDto {
    private String text;
    private String stopReason;
    private String inputText;
    private Integer inputLength;
    private List<String> inputTokens;
    private String outputText;
    private Integer outputLength;
    private List<String> outputTokens;
    private List<AiFilter> aiFilter;
    private Boolean ok;

    @Getter
    public static class AiFilter {
        private String groupName;
        private String name;
        private String score;
    }

}
