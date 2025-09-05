package com.example.webflux_LLM;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/reactive")
@Slf4j
public class ReactiveExampleController {

    @GetMapping("/onenine/legacy")
    public Mono<List<Integer>> produceOneToNineLegacy() {
        return Mono.fromCallable(() -> {
            List<Integer> sink = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                sink.add(i);
            }
            return sink;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/onenine/list")
    public Mono<List<Integer>> produceOneToNine() {
        return Mono.defer(() -> {
            List<Integer> sink = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {}
                sink.add(i);
            }
            return Mono.just(sink);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/onenine/flux")
    public Flux<Integer> produceOneToNineFlux() {
        return Flux.<Integer>create(sink -> {
            for (int i = 0; i < 10; i++) {
                try {
                    log.info("현재 처리하고 있는 스레드 이름: " + Thread.currentThread().getName()); // reactor-http-nio-3 블로킹 되지 않아야 함.
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                sink.next(i);
            }
            sink.complete();
        }).subscribeOn(Schedulers.boundedElastic()); // 스레드 종류 boundedElastic vs parallel, 구독시점 아닌 발행시점에도 변경가능
        // 리액티브 스트림 구현체 Flux, Mono를 사용하여 발생하는 데이터를 바로바로 리액티브하게 처리
        // 비동기 동작 - 논 블로킹하게 동작
        // 구독하지 않고 발행 하는 publish() -> Hot Sequence
        // 일반적인 구독-발행 순 -> Cold Sequence

        // 리액티브 프로그래밍 필수 요소
        // 1. 데이터가 준비될 때 마다 바로바로 리액티브하게 처리 > 리액티브 스트림 구현체 Flux, Mono를 사용하여 발생하는 데이터 바로바로 처리
        // 2. 로직을 짤 때는 반드시 논 블로킹하게 짜야함 > 이를 위해 비동기 프로그래밍 필요
    }
}
