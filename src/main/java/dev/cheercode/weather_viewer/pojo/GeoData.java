package dev.cheercode.weather_viewer.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoData {
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
    private String state;
    @JsonProperty("local_names")
    private LocalNames localNames;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class LocalNames {
        private String en;
        private String ru;
    }
}
