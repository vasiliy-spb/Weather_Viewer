package dev.cheercode.weather_viewer;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

//public class Main {
//    public static void main(String[] args) {
//        WebClient client = WebClient.create("http://api.openweathermap.org");
//
//        long id = 524901L;
//        String key = "3ac20745e6c18ecef4bb59c6fdb3561e";
//
//        Mono<String> result = client.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/data/2.5/forecast")
//                        .queryParam("id", id)
//                        .queryParam("appid", key)
//                        .build())
//                .retrieve()
//                .bodyToMono(String.class);
//
//        System.out.println(result.block());
//
//
//    }
//} // http://api.openweathermap.org/data/2.5/forecast?id=524901&appid=3ac20745e6c18ecef4bb59c6fdb3561e
