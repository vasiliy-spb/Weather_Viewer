package dev.cheercode.weather_viewer.service;

import dev.cheercode.weather_viewer.exception.WeatherServiceException;
import dev.cheercode.weather_viewer.pojo.GeoData;
import dev.cheercode.weather_viewer.pojo.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
public class WeatherService {
    private WebClient webClient;
    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openweathermap.org").build();
    }

    public Mono<List<GeoData>> getGeoData(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geo/1.0/direct")
                        .queryParam("q", city)
                        .queryParam("limit", 5)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToFlux(GeoData.class)
                .collectList();
    }

    public Mono<List<GeoData>> getGeoData(BigDecimal lat, BigDecimal lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geo/1.0/reverse")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("limit", 5)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToFlux(GeoData.class)
                .collectList();
    }

    public Mono<WeatherData> getWeatherData(BigDecimal lat, BigDecimal lon, String lang, String units) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/weather")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("units", units)
                        .queryParam("lang", lang)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> Mono.error(new WeatherServiceException("Weather API error")))
                .bodyToMono(WeatherData.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)));
    }

//    public Mono<String> get(long cityId) {
//        return client.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/data/2.5/forecast")
//                        .queryParam("id", cityId)
//                        .queryParam("appid", KEY)
//                        .build())
//                .retrieve()
//                .toEntityList(String.class)
//                .map(l -> l.getBody().get(0));
//    }
//
//    public Mono<String> get(String city) {
//        return client.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/data/2.5/forecast")
//                        .queryParam("q", city)
//                        .queryParam("appid", KEY)
//                        .build())
//                .retrieve()
//                .toEntityList(String.class)
//                .map(l -> l.getBody().get(0));
//    }
//
//    public Flux<String> getForAll(List<String> cities) {
//        return Flux.fromIterable(cities)
//                .flatMap(this::get);
//    }

}
