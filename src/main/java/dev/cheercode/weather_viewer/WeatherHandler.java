package dev.cheercode.weather_viewer;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

//@Component
//public class WeatherHandler {
//    public Mono<ServerResponse> weather(ServerRequest request) {
//        return ServerResponse
//                .ok()
//                .contentType(MediaType.TEXT_PLAIN)
//                .body(BodyInserters.fromValue("Hello from weather"));
//    }
//}

