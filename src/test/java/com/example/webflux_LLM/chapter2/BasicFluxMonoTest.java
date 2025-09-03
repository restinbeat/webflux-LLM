package com.example.webflux_LLM.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class BasicFluxMonoTest {

    @Test
    public void testBasicFluxMono() {
        Flux.<Integer>just(1, 2, 3, 4, 5)
            .map(data -> data * 2)
            .filter(data -> data % 4 == 0)
            .subscribe(data -> System.out.println("Flux가 구독한 data! = " + data));

        // 1. just 데이ㅓ로부터 흐름을 시작
        // 2. map과 filter와 같은 연산자로 데이터를 가공
        // 3. subscribe하면서 데이터를 방출

        // Mono 0개 1부터 1개의 데이터만 방출할 수 있는 객체 -> optional 정도
        // Flux 0개 이상의 데이터를 방출할 수 있는 객체 -> List, Stream 0개 이상의 데이터 방출
    }
}
