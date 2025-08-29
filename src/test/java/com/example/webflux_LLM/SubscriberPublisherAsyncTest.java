package com.example.webflux_LLM;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class SubscriberPublisherAsyncTest {

    @Test
    public void produceOneToNineFlux() {
        Flux<Integer> intFlux = Flux.<Integer>create(sink -> {
            for (int i= 1; i<=9; i++) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                sink.next(i);
            }

            sink.complete();
        }).subscribeOn(Schedulers.boundedElastic()); // 스레드 1개로는 블로킹 회피할 수 없음 -> 스케쥴러 스레드 사용

        intFlux.subscribe(data -> {
            System.out.println("실행 되고 있는 스레드 이름: " + Thread.currentThread().getName());
            System.out.println("Flux가 구독 중 !! : " + data);
        });
        System.out.println("Netty 이벤트 루프로 스레드 통과 !!");

        try {
            Thread.sleep(5000); // 메인 스레드가 살아있어야 스케쥴러 제공하는 스레드도 사용됨
        } catch (Exception e) {
        }
    }

}
