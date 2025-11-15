package com.polarbookshop.orderservice.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class BookClient {
    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient;

    public BookClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
                .get()
                .uri(BOOKS_ROOT_API + isbn)
                .retrieve()
                .bodyToMono(Book.class)
                //.timeout(Duration.ofSeconds(3))//Устанавливает 3-секундный таймаут для запроса GET
                .timeout(Duration.ofSeconds(3), Mono.empty())//Резервный вариант возвращает пустой объект Mono
                //Экспоненциальная отсрочка используется в качестве стратегии повтора. Разрешаются три попытки с начальной задержкой 100 мс
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                //Возвращает пустой объект при получении ответа 404
                .onErrorResume(WebClientResponseException.NotFound.class, exception -> Mono.empty())
                //Если после трех повторных попыток произойдет какая-либо ошибка, перехватите исключение и верните пустой объект.
                .onErrorResume(Exception.class, exception -> Mono.empty());

    }
}

