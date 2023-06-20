package org.example;

import java.util.HashMap;
import java.util.Map;

public class Dictionaries {
    public static final Map<String, String> conditionMap = new HashMap<>(19) {{
        put("clear", "ясно☀️");
        put("partly-cloudy", "малооблачно🌤");
        put("cloudy", "облачно с прояснениями🌥️");
        put("overcast", "пасмурно☁️");
        put("drizzle", "морось");
        put("light-rain", "небольшой дождь🌧");
        put("rain", "дождь🌧");
        put("moderate-rain", "умеренно сильный дождь🌧");
        put("heavy-rain", "сильный дождь🌧");
        put("continuous-heavy-rain", "длительный сильный дождь🌧");
        put("showers", "ливень🌧️");
        put("wet-snow", "дождь со снегом🌧️🌨️");
        put("light-snow", "небольшой снег🌨️");
        put("snow", "снег🌨️");
        put("snow-showers", "снегопад🌨️");
        put("hail", "град");
        put("thunderstorm", "гроза🌩️");
        put("thunderstorm-with-rain", "дождь с грозой⛈️");
        put("thunderstorm-with-hail", "гроза с градом");
    }};
    public static final Map<String, String> cityCoordMap = Map.of(
            "Самара", "lat=53.2001&lon=50.15",
            "Новокуйбышевск", "lat=53.0959&lon=49.9462",
            "Домик", "lat=53.091793414535225&lon=49.99248353476951",
            "Котик", "lat=53.234815&lon=50.281392"
    );
    public static final Map<String, String> declinationCityMap = Map.of(
            "Самара", "Самаре",
            "Новокуйбышевск", "Новокуйбышеске",
            "Домик", "Домике",
            "Котик", "доме котика"
    );
    public static final Map<String, String> cityCodeMap = Map.of(
            "Самара", "s9606096",
            "Мирная", "s9606246",
            "Липяги", "s9606377",
            "Средневолжская", "s9606561",
            "Сокская", "s9635417"
    );
    public static final Map<String, String> translatedCitiesMap = Map.of(
            "WeatherSamara", "Самара",
            "WeatherNovokybishevsk", "Новокуйбышевск"
    );
    public static final Map<String, String> arrivalStationMap = Map.of(
            "FromMirnaya", "Мирная",
            "FromLipyagi", "Липяги",
            "FromSrednevolzhskaya", "Средневолжская",
            "FromSamara", "Самара"
    );
    public static Map<Double, String> Strength_rain = Map.of(
            0.25, "слабый дождь",
            0.5, "дождь",
            0.75, "сильный дождь",
            1.0, "сильный ливень"
    );
}