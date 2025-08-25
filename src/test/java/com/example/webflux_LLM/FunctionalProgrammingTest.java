package com.example.webflux_LLM;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
public class FunctionalProgrammingTest {

    @Test
    public void produceOneToNineFluxOperator() {
        Flux.fromIterable(IntStream.range(1, 9).boxed().toList())
            .map(data -> data * 4)
            .filter(data -> data % 4 == 0)
            .subscribe(System.out::println);
    }

}
