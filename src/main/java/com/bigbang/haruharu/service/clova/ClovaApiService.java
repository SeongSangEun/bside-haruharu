package com.bigbang.haruharu.service.clova;

import com.bigbang.haruharu.dto.request.clova.ClovaRequestDto;
import com.bigbang.haruharu.dto.response.clova.ClovaResponseDto;
import com.bigbang.haruharu.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Service
@RequiredArgsConstructor
public class ClovaApiService {

    @Value("${clova.url}")
    private String CLOVA_URL;
    @Value("${clova.X-NCP-CLOVASTUDIO-API-KEY}")
    private String CLOVA_STUDIO_API_KEY;
    @Value("${clova.X-NCP-APIGW-API-KEY}")
    private String NCP_GATEWAY_API_KEY;
    @Value("${clova.X-NCP-CLOVASTUDIO-REQUEST-ID}")
    private String REQUEST_ID;

    private final JsonUtils jsonUtils;


    public String callClovaApi(String text) {
        ClovaRequestDto clovaRequestDto = ClovaRequestDto.builder()
                .text(text)
                .start("")
                .restart("")
                .includeTokens(true)
                .topP(0.8)
                .topK(0)
                .maxTokens(100)
                .temperature(0.8)
                .repeatPenalty(5.0)
                .includeAiFilters(false)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        headers.add("X-NCP-CLOVASTUDIO-API-KEY", CLOVA_STUDIO_API_KEY);
        headers.add("X-NCP-APIGW-API-KEY", NCP_GATEWAY_API_KEY);
        headers.add("X-NCP-CLOVASTUDIO-REQUEST-ID", REQUEST_ID);

        HttpEntity request = new HttpEntity(clovaRequestDto, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> exchange = restTemplate.exchange(CLOVA_URL, HttpMethod.POST, request, String.class);

        ClovaResponseDto clovaResponseDto = jsonUtils.fromJson(exchange.getBody(), ClovaResponseDto.class);
        return clovaResponseDto.getResult().getOutputText();

    }


}
