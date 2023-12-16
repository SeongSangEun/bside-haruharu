package com.bigbang.haruharu.service.clova;

import com.bigbang.haruharu.domain.entity.concept.Concept;
import com.bigbang.haruharu.dto.request.clova.ClovaChatDto;
import com.bigbang.haruharu.dto.request.clova.ClovaChatRequestDto;
import com.bigbang.haruharu.dto.request.clova.ClovaRequestDto;
import com.bigbang.haruharu.dto.response.clova.ClovaResponseDto;
import com.bigbang.haruharu.dto.response.clova.ResultResponseDto;
import com.bigbang.haruharu.repository.concept.ConceptRepository;
import com.bigbang.haruharu.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

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
    private final ConceptRepository conceptRepository;


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

    public String convertTextApi(Long conceptSeq, String inputText) {
        Concept concept = conceptRepository.findById(conceptSeq).get();

        ClovaChatDto systemChat = ClovaChatDto.builder()
                .role("system")
                .content(concept.getSystemScript())
                .build();
        ClovaChatDto userChat = ClovaChatDto.builder()
                .role("user")
                .content(inputText)
                .build();
        List<ClovaChatDto> chatList = Arrays.asList(systemChat, userChat);
        List<String> stopList = Arrays.asList("\n");

        ClovaChatRequestDto build = ClovaChatRequestDto.builder()
                .messages(chatList)
                .topK(concept.getTopK())
                .topP(concept.getTopP())
                .maxTokens(concept.getMaxTokens())
                .temperature(concept.getTemperature())
                .repeatPenalty(concept.getRepeatPenalty())
                .stopBefore(stopList)
                .includeAiFilters(true)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.TEXT_EVENT_STREAM }));
        headers.add("X-NCP-CLOVASTUDIO-API-KEY", CLOVA_STUDIO_API_KEY);
        headers.add("X-NCP-APIGW-API-KEY", NCP_GATEWAY_API_KEY);
        headers.add("X-NCP-CLOVASTUDIO-REQUEST-ID", REQUEST_ID);

        HttpEntity request = new HttpEntity(build, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(CLOVA_URL, HttpMethod.POST, request, String.class);
        String s = extractResultData(exchange.getBody());
        ResultResponseDto resultResponseDto = jsonUtils.fromJson(s, ResultResponseDto.class);

        return resultResponseDto.getMessage().getContent();
    }

    public String convertSubjectApi(String inputText) {
        Concept concept = conceptRepository.findByName("제목생성");

        ClovaChatDto systemChat = ClovaChatDto.builder()
                .role("system")
                .content(concept.getSystemScript())
                .build();
        ClovaChatDto userChat = ClovaChatDto.builder()
                .role("user")
                .content(inputText)
                .build();
        List<ClovaChatDto> chatList = Arrays.asList(systemChat, userChat);
        List<String> stopList = Arrays.asList("\n");

        ClovaChatRequestDto build = ClovaChatRequestDto.builder()
                .messages(chatList)
                .topK(concept.getTopK())
                .topP(concept.getTopP())
                .maxTokens(concept.getMaxTokens())
                .temperature(concept.getTemperature())
                .repeatPenalty(concept.getRepeatPenalty())
                .stopBefore(stopList)
                .includeAiFilters(true)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.TEXT_EVENT_STREAM }));
        headers.add("X-NCP-CLOVASTUDIO-API-KEY", CLOVA_STUDIO_API_KEY);
        headers.add("X-NCP-APIGW-API-KEY", NCP_GATEWAY_API_KEY);
        headers.add("X-NCP-CLOVASTUDIO-REQUEST-ID", REQUEST_ID);

        HttpEntity request = new HttpEntity(build, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(CLOVA_URL, HttpMethod.POST, request, String.class);
        String s = extractResultData(exchange.getBody());
        ResultResponseDto resultResponseDto = jsonUtils.fromJson(s, ResultResponseDto.class);

        return resultResponseDto.getMessage().getContent();
    }


    public String convertHashTagsApi(String inputText) {
        Concept concept = conceptRepository.findByName("해쉬태그생성");

        ClovaChatDto systemChat = ClovaChatDto.builder()
                .role("system")
                .content(concept.getSystemScript())
                .build();
        ClovaChatDto userChat = ClovaChatDto.builder()
                .role("user")
                .content(inputText)
                .build();
        List<ClovaChatDto> chatList = Arrays.asList(systemChat, userChat);
        List<String> stopList = Arrays.asList("\n");

        ClovaChatRequestDto build = ClovaChatRequestDto.builder()
                .messages(chatList)
                .topK(concept.getTopK())
                .topP(concept.getTopP())
                .maxTokens(concept.getMaxTokens())
                .temperature(concept.getTemperature())
                .repeatPenalty(concept.getRepeatPenalty())
                .stopBefore(stopList)
                .includeAiFilters(true)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.TEXT_EVENT_STREAM }));
        headers.add("X-NCP-CLOVASTUDIO-API-KEY", CLOVA_STUDIO_API_KEY);
        headers.add("X-NCP-APIGW-API-KEY", NCP_GATEWAY_API_KEY);
        headers.add("X-NCP-CLOVASTUDIO-REQUEST-ID", REQUEST_ID);

        HttpEntity request = new HttpEntity(build, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(CLOVA_URL, HttpMethod.POST, request, String.class);
        String s = extractResultData(exchange.getBody());
        ResultResponseDto resultResponseDto = jsonUtils.fromJson(s, ResultResponseDto.class);

        return resultResponseDto.getMessage().getContent();
    }



    public String clovaTest() {
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

        ClovaChatRequestDto build = ClovaChatRequestDto.builder()
                .messages(chatList)
                .topK(0)
                .topP(0.8)
                .maxTokens(256)
                .temperature(0.8)
                .repeatPenalty(5.0)
                .stopBefore(stopList)
                .includeAiFilters(true)
                .build();

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.TEXT_EVENT_STREAM }));
        headers.add("X-NCP-CLOVASTUDIO-API-KEY", "NTA0MjU2MWZlZTcxNDJiY0Q+agAvO7ThcA564J17RAlpshebZTIhRYsVc/gX0I/uBNPkpPCgM8wJqKiESW6zvD6649SY1Yk8i9Smma0VqlNQnexUAmD9WKSs4x/FT+dyFg1uhzw7op3k/b/iIn/NIdkQLxRgClvARgus73DWEC+yKJKVp1dFxpl5sxyNsEnLH8nxtRDRg5ohdiUg2hy8GbHYa8SwWW4OawhaqWVqyVI=");
        headers.add("X-NCP-APIGW-API-KEY", "IHQD7VJfELEyMar7N2Q3Lk29CvgDcQBtLabO87bM");
        headers.add("X-NCP-CLOVASTUDIO-REQUEST-ID", "cb9f86056ca14f8d9e2924cadd6e4507");

        HttpEntity request = new HttpEntity(build, headers);

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-002";
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String s = extractResultData(exchange.getBody());
        ResultResponseDto resultResponseDto = jsonUtils.fromJson(s, ResultResponseDto.class);

        return url;
    }
    private static String extractResultData(String input) {
        String[] lines = input.split("\n");

        // "event:result"인 부분의 다음 라인이 데이터이므로 해당 라인 추출
        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].startsWith("event:result")) {
                return lines[i + 1].substring("data:".length());
            }
        }

        return null; // 찾지 못한 경우
    }
}
