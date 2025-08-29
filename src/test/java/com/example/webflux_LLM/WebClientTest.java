package com.example.webflux_LLM;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootTest
public class WebClientTest {

    private WebClient webClient = WebClient.builder().build();

    @Test
    public void testWebClient() {
        Flux<Integer> intFlux = webClient.get()
            .uri("http://localhost:8080/reactive/onenine/flux")
            .retrieve()
            .bodyToFlux(Integer.class);

        intFlux.subscribe(data -> {
            System.out.println("실행 되고 있는 스레드 이름: " + Thread.currentThread().getName());
            System.out.println("Flux가 구독 중 !! : " + data);
        });
        System.out.println("Netty 이벤트 루프로 스레드 통과 !!");

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
    }
    
}
