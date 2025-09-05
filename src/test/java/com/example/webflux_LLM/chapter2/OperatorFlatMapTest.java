package com.example.webflux_LLM.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class OperatorFlatMapTest {
    /**
     * Mono<Mono<T>> -> Mono<T>
     * Mono<FLux<T>> -> FLux<T>
     * FLux<Mono<T>> -> FLux<T>
     */

    @Test
    public void monoToFlux() {
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerMap = one.flatMapMany(data -> {
            return Flux.just(data, data + 1, data + 2);
        });
        integerMap.subscribe(data -> System.out.println("data = " + data));
    }

    @Test
    public void testWebClientFlatmap() {
        Flux<String> flatMap = Flux.just(callWebClient("1단계 - 문제 이해하기", 1500),
            callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
            callWebClient("3단계 - 최종응답", 500))
        .flatMap(monoData -> {
            return monoData;
        });

        flatMap.subscribe(data -> System.out.println("flatMapped data = " + data));

        Flux<String> objectFlux = Flux.<Mono<String>>create(sink -> {
            sink.next(callWebClient("1단계 - 문제 이해하기", 1500));
            sink.next(callWebClient("2단계 - 문제 단계별로 풀어가기", 1000));
            sink.next(callWebClient("3단계 - 최종응답", 500));
            sink.complete();
        }).flatMap(monoData -> {
            return monoData;
        });

        try {
            Thread.sleep(10000);
        } catch (Exception e) {}
    }

    public Mono<String> callWebClient(String request, long delay) {
        return Mono.defer(() -> {
            try{
                Thread.sleep(delay);
                return Mono.just(request + " -> 딜레이: " + delay);
            } catch (Exception e) {
                return Mono.empty();
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
