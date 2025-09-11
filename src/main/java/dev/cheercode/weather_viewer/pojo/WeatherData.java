package dev.cheercode.weather_viewer.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {
    private Long id; // City ID (deprecated request)
    private String name; // City name (deprecated request)
    private Coordinates coord;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Rain rain;
    private Snow snow;
    private Sys sys;
    private Long timezone; // Shift in seconds from UTC

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Coordinates {
        private BigDecimal lat;
        private BigDecimal lon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Main {
        private BigDecimal temp;
        @JsonProperty("feels_like")
        private BigDecimal feelsLike;
        @JsonProperty("temp_min")
        private BigDecimal minTemp;
        @JsonProperty("temp_max")
        private BigDecimal maxTemp;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Weather {
        private Integer id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Wind {
        private BigDecimal speed;
        private Integer deg; // Wind direction, degrees (meteorological)
        private String gust;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Clouds {
        private Integer all; // Cloudiness, %
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Rain {
        @JsonProperty("1h") private Integer millimeters; // Precipitation, mm/h. Please note that only mm/h as units of measurement are available for this parameter
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Snow {
        @JsonProperty("1h") private Integer millimeters; // Precipitation, mm/h. Please note that only mm/h as units of measurement are available for this parameter
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Sys {
        private Long sunrise;
        private Long sunset;
    }
}
