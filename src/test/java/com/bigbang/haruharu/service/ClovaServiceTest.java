package com.bigbang.haruharu.service;

import com.bigbang.haruharu.service.clova.ClovaApiService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClovaServiceTest {

    @Autowired
    private ClovaApiService clovaApiService;

    @Test
    public void callApiTest() {

//        String text = "###\\n키워드: 창문, 소통\\n문장 : 창문의 틈새로 들어오는 바람과 함께 너의 목소리가 들려온다.\\n키워드: 향기, 계절\\n문장: 봄의 향기는 너무 짙어 여름까지 지속된다.\\n키워드: 창문,파랑\\n문장: 창문의 색과 하늘의 색이 닮아있다. 그래서 우리는 서로에게 스며들 수 밖에 없다.\\n###\\n키워드:초콜릿,고구마\\n문장:";
//
//        String s = clovaApiService.callClovaApi(text);
//        System.out.println("s = " + s);


    }
}
