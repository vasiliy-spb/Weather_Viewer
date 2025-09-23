package dev.cheercode.weather_viewer.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

public record WeatherDto(
        BigDecimal latitude, // широта
        BigDecimal longitude, // долгота
        String city, // название города
        String country, // код страны, например "RU", "GB", "CA"

        BigDecimal temperature,
// Текущая температура. Если выбран язык en, то значение температуры в Fahrenheit, а если ru, то в Celsius
        BigDecimal feelsLike, // температура ощущается как
        BigDecimal minTemperature, // минимальная температура дня
        BigDecimal maxTemperature, // максимальная температура дня
        Integer humidity, // влажность в процентах
        Integer pressure, // атмосферное давление в hPa

        // Weather
        String weather, // короткое название погоды (например "ясно")
        String description, // описание погоды (например "переменная облачность")
        String icon,
        // номер и буква иконки текущей погоды, например "02d" (сами иконки лежат в директории "static/weather_icon/") имена файлов в таком формате [icon]@2x.png например "02d@2x.png"

        // Wind
        BigDecimal speed, // скорость ветра, если язык ru, то в метрах в секунду, а если en, то в милях в час

        Integer cloudiness, // Cloudiness, %

        LocalTime currentTime, // текущее время в локации
        LocalTime sunrise, // Sunrise time, unix, UTC
        LocalTime sunset, // Sunset time, unix, UTC
        Double sunPositionPercentage, // Положение солнца в процентах (0-100)
        String currentTimeFormatted, // Текущее время в формате HH:mm
        Double sunrisePositionPercentage, // положение рассвета в %
        Double sunsetPositionPercentage,   // положение заката в %
        Boolean isDay // true - день, false - ночь
) {
}
