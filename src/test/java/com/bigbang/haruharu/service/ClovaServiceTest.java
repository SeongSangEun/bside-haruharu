package com.bigbang.haruharu.service;

import com.bigbang.haruharu.domain.entity.concept.Concept;
import com.bigbang.haruharu.dto.request.clova.ClovaChatDto;
import com.bigbang.haruharu.dto.request.clova.ClovaChatRequestDto;
import com.bigbang.haruharu.dto.request.clova.ClovaRequestDto;
import com.bigbang.haruharu.dto.response.clova.ClovaResponseDto;
import com.bigbang.haruharu.service.clova.ClovaApiService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ClovaServiceTest {

    @Autowired
    private ClovaApiService clovaApiService;

    @Test
    public void createTest() {

        ClovaChatDto systemChat = ClovaChatDto.builder()
                .role("system")
                .content("- 주어진 키워드 를 바탕으로 10자~50자 내의 감성있는 글의 문장을 만들어낸다.\\n- 마치 하상욱 시인의 시처럼 짧고 강렬한 인상을 준다.\\n- 비유적 표현도 적절히 사용한다.\\n###\\n키워드: 창문, 소통\\n문장 : 창문의 틈새로 들어오는 바람과 함께 너의 목소리가 들려온다.\\n키워드: 향기, 계절\\n문장: 봄의 향기는 너무 짙어 여름까지 지속된다.\\n키워드: 창문,파랑\\n문장: 창문의 색과 하늘의 색이 닮아있다. 그래서 우리는 서로에게 스며들 수 밖에 없다.\\n###")
                .build();
        ClovaChatDto userChat = ClovaChatDto.builder()
                .role("user")
                .content("눈이내려, 그럼에도 불구하고 내 마음은 너무 뜨거워")
                .build();
        List<ClovaChatDto> chatList = Arrays.asList(systemChat, userChat);
        List<String> stopList = Arrays.asList("\n");

        ClovaChatRequestDto.ClovaChatRequestDtoBuilder clovaChatRequestDtoBuilder = ClovaChatRequestDto.builder()
                .messages(chatList)
                .topK(0)
                .topP(0.8)
                .maxTokens(256)
                .temperature(0.8)
                .repeatPenalty(5.0)
                .stopBefore(stopList)
                .includeAiFilters(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        headers.add("X-NCP-CLOVASTUDIO-API-KEY", "NTA0MjU2MWZlZTcxNDJiY0Q+agAvO7ThcA564J17RAlpshebZTIhRYsVc/gX0I/uBNPkpPCgM8wJqKiESW6zvD6649SY1Yk8i9Smma0VqlNQnexUAmD9WKSs4x/FT+dyFg1uhzw7op3k/b/iIn/NIdkQLxRgClvARgus73DWEC+yKJKVp1dFxpl5sxyNsEnLH8nxtRDRg5ohdiUg2hy8GbHYa8SwWW4OawhaqWVqyVI=");
        headers.add("X-NCP-APIGW-API-KEY", "IHQD7VJfELEyMar7N2Q3Lk29CvgDcQBtLabO87bM");
        headers.add("X-NCP-CLOVASTUDIO-REQUEST-ID", "cb9f86056ca14f8d9e2924cadd6e4507");

        HttpEntity request = new HttpEntity(clovaChatRequestDtoBuilder, headers);

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-002";
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("exchange = " + exchange);
    }
}
