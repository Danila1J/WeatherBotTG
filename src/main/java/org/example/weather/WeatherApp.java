package org.example.weather;

import com.google.gson.Gson;
import org.example.Dictionaries;
import org.example.Properties;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WeatherApp {

    private static final Gson gson = new Gson();
    private static final String WEATHER_API_URL = "https://api.weather.yandex.ru/v2/informers?";
    private static final String API_KEY = "bd085f91-2540-42a4-94c3-d0f4c62a1f8f";


    // Метод для получения погоды по заданному городу
    private static json.Root fetchWeather(String selectedCity) {
        json.Root fetchedWeather = null;
        try {
            URL url = new URL(WEATHER_API_URL + Dictionaries.cityCoordMap.get(selectedCity) + "&[lang=ru_RU]");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Yandex-API-Key", API_KEY);
            String line;
            try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
                line = scanner.useDelimiter("\\A").next();
            }
            fetchedWeather = gson.fromJson(line, json.Root.class);
        } catch (IOException e) {
            System.out.println("Возникло исключение");
        }
        return fetchedWeather;
    }

    // Метод для обработки погоды и сохранения результатов в объект Properties
    public static void displayWeatherInfo() {
        json.Root weather = fetchWeather(Properties.getNameCity());
        Properties.getS().clear();
        Properties.setTempOfCity(weather.fact.temp); //Температура
        Properties.setCondition(Dictionaries.conditionMap.get(weather.fact.condition)); //Описание
        Properties.setWind(weather.fact.wind_speed); //Скорость ветра

        for (json.Part part : weather.forecast.parts) {
            switch (part.part_name) {
                case "morning" -> {
                    Properties.setMorning_t(part.temp_avg);
                    Properties.setMorning_cond(Dictionaries.conditionMap.get(part.condition));
                    Properties.getS().add(part.part_name);
                }
                case "day" -> {
                    Properties.setDay_t(part.temp_avg);
                    Properties.setDay_cond(Dictionaries.conditionMap.get(part.condition));
                    Properties.getS().add(part.part_name);
                }
                case "evening" -> {
                    Properties.setEvening_t(part.temp_avg);
                    Properties.setEvening_cond(Dictionaries.conditionMap.get(part.condition));
                    Properties.getS().add(part.part_name);
                }
                case "night" -> {
                    Properties.setNight_t(part.temp_avg);
                    Properties.setNight_cond(Dictionaries.conditionMap.get(part.condition));
                    Properties.getS().add(part.part_name);
                }
            }
        }
        generateTemperatureMessage(weather.fact.temp);
    }

    private static void generateTemperatureMessage(double temperature) {
        Properties.setWeatherMessage(switch ((int) temperature) {
            case 0, 1, 2, 3, 4 ->
                    "Ещё слишком холодно носить лёгкую осеннюю одежду, а ну бегом укутываться во что-то тёплое!!!\nБез шапки ходить ещё рановато";
            case 5, 6, 7, 8, 9 -> "Сейчас холодно одевайся потеплее!\nШарфик и перчатки могут пригодиться";
            case 10, 11, 12, 13, 14 -> "Сейчас прохладно, лучше надеть что-то теплее куртку/пальто";
            case 15, 16, 17, 18, 19 -> "Сейчас достаточно тепло, так что можешь надеть свитер и штанишки)";
            case 20, 21, 22, 23, 24 -> "Сейчас тепло, можно надеть толстовку/лёгкий свитер или даже майку";
            case 25, 26, 27, 28, 29 -> "Можно надеть майку и штаны, а возможно и шортики))";
            case 30, 31, 32, 33, 34 -> "Выходить на улицу в крайнем случае!!!\nНа улице очень жарко, девочки";
            default -> "Invalid temperature";
        });
    }
}
