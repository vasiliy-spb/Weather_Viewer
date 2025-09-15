package dev.cheercode.weather_viewer.dto;

import java.math.BigDecimal;

public record WeatherDto(
        String city, // название города
        String country, // код страны, например "RU", "GB", "CA"
        Long timezone, // Shift in seconds from UTC. Этот параметр определяет на светлом или на тёмном фоне показываются данные погоды в этой карточке: от восхода до захода солнца фон светлый, до восхода и после захода — тёмный

        BigDecimal temperature, // Текущая температура. Если выбран язык en, то значение температуры в Fahrenheit, а если ru, то в Celsius
        BigDecimal feelsLike, // температура ощущается как
        BigDecimal minTemperature, // минимальная температура дня
        BigDecimal maxTemperature, // максимальная температура дня
        Integer humidity, // влажность в процентах
        Integer pressure, // атмосферное давление в hPa

        // Weather
        String weather, // короткое название погоды (например "ясно")
        String description, // описание погоды (например "переменная облачность")
        String icon, // номер и буква иконки текущей погоды, например "02d" (сами иконки лежат в директории "static/weather_icon/") имена файлов в таком формате [icon]@2x.png например "02d@2x.png"

        // Wind
        BigDecimal speed, // скорость ветра, если язык ru, то в метрах в секунду, а если en, то в милях в час
        Integer degree, // направление ветра, degrees (meteorological)

        Integer cloudiness, // Cloudiness, %

        BigDecimal rainMillimeters, // Precipitation, mm/h
        BigDecimal snowMillimeters, // Precipitation, mm/h

        Long sunrise, // Sunrise time, unix, UTC
        Long sunset // Sunset time, unix, UTC
) {
}
