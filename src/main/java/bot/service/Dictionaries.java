package bot.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
            "Новокуйбышевск", "lat=53.0959&lon=49.9462"
    );
    public static final Map<String, String> declinationCityMap = Map.of(
            "Самара", "Самаре",
            "Новокуйбышевск", "Новокуйбышеске"
    );
    public static final Map<String, String> cityCodeMap = Map.of(
            "Самара", "s9606096",
            "Мирная", "s9606246",
            "Липяги", "s9606377",
            "Средневолжская", "s9606561",
            "Сокская", "s9635417",
            "Похвистнево", "s9606404"
    );
    public static final Map<String, String> translatedCitiesMap = Map.of(
            "WeatherSamara", "Самара",
            "WeatherNovokybishevsk", "Новокуйбышевск"
    );
    private static final Map<String, String> stationNames = Map.of(
            "Mirnaya", "Мирная",
            "Lipyagi", "Липяги",
            "Srednevolzhskaya", "Средневолжская",
            "Samara", "Самара",
            "Sokskaya", "Сокская",
            "Pohvistnevo", "Похвистнево"
    );

    public static final Map<String, String> arrivalStationMap = stationNames.entrySet().stream()
            .collect(Collectors.toMap(
                    entry -> "From" + entry.getKey(),
                    Map.Entry::getValue
            ));

    public static final Map<String, String> departureStationMap = stationNames.entrySet().stream()
            .collect(Collectors.toMap(
                    entry -> "To" + entry.getKey(),
                    Map.Entry::getValue
            ));
    public static final Map<String, String> routes = Map.of(
            "Telecentre->Railway", "Телецентр -> Ж/Д",
            "Railway->Telecentre", "Ж/Д -> Телецентр"
    );

    public static final Map<String, String> releaseForm = new HashMap<>(19) {{
        put("аэр", "Аэрозоль");
        put("гель", "Гель");
        put("гран", "Гранулы");
        put("драже", "Драже");
        put("капли", "Капли");
        put("капс", "Капсулы");
        put("крем", "Крем");
        put("лосьон", "Лосьон");
        put("мазь", "Мазь");
        put("пор", "Порошок");
        put("р-р", "Раствор");
        put("сироп", "Сироп");
        put("спрей", "Спрей");
        put("супп", "Суппозитории");
        put("сусп", "Суспензии");
        put("таб", "Таблетки");
        put("шамп", "Шампунь");
        put("эликсир", "Эликсир");
        put("эмульс", "Эмульсия");
        put("форте","Форте");
    }};
}