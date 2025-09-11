package dev.cheercode.weather_viewer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

//@Configuration
//public class WeatherRouter {
//    @Bean
//    public RouterFunction<ServerResponse> route(WeatherHandler weatherHandler) {
//        return RouterFunctions
//                .route(
//                        RequestPredicates.GET("/weather")
//                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
//                        weatherHandler::weather
//                )
//                .andRoute(RequestPredicates.GET("/w"),
//                        serverRequest -> {
//                            String user = serverRequest.queryParam("user")
//                                    .orElse("Nobody");
//                            System.out.println("user = " + user);
//                            return ServerResponse
//                                    .ok()
//                                    .render("index", Map.of("user", user));
//                        });
//    }
//}
