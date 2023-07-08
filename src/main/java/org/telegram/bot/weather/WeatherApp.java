package org.telegram.bot.weather;

import com.google.gson.Gson;
import org.telegram.bot.service.files.Dictionaries;
import org.telegram.bot.service.files.Properties;
import org.telegram.bot.service.files.Singleton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class WeatherApp {

    private static final Gson gson = new Gson();
    private static final Properties properties = Singleton.getInstance().getProperties();


    // Метод для получения погоды по заданному городу
    private static json.Root fetchWeather(String selectedCity) {
        json.Root fetchedWeather = null;
        try {
            URL url = new URL(System.getProperty("WEATHER_API_URL") + Dictionaries.cityCoordMap.get(selectedCity) + "&[lang=ru_RU]");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Yandex-API-Key", System.getProperty("WEATHER_API_KEY"));
            String line;
            try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
                line = scanner.useDelimiter("\\A").next();
            }
            fetchedWeather = gson.fromJson(line, json.Root.class);
        } catch (IOException e) {
            System.out.println("Возникло исключение, вероятно закончилось кол-во запросов");
        }
        return fetchedWeather;
    }

    public static String createWeatherStr(List<String> weather) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : weather) {
            switch (s) {
                case "morning" ->
                        stringBuilder.append("\nУтро: ").append(properties.getMorning_t()).append("°C ").append(properties.getMorning_cond());
                case "day" ->
                        stringBuilder.append("\nДень: ").append(properties.getDay_t()).append("°C ").append(properties.getDay_cond());
                case "evening" ->
                        stringBuilder.append("\nВечер: ").append(properties.getEvening_t()).append("°C ").append(properties.getEvening_cond());
                case "night" ->
                        stringBuilder.append("\nНочь: ").append(properties.getNight_t()).append("°C ").append(properties.getNight_cond());
            }
        }
        return stringBuilder.toString();
    }

    // Метод для обработки погоды и сохранения результатов в объект Properties
    public static void displayWeatherInfo(String cityName) {
        json.Root weather = fetchWeather(cityName);
        properties.getS().clear();
        properties.setTempOfCity(weather.fact.temp); //Температура
        properties.setCondition(Dictionaries.conditionMap.get(weather.fact.condition)); //Описание
        properties.setWind(weather.fact.wind_speed); //Скорость ветра

        for (json.Part part : weather.forecast.parts) {
            switch (part.part_name) {
                case "morning" -> {
                    properties.setMorning_t(part.temp_avg);
                    properties.setMorning_cond(Dictionaries.conditionMap.get(part.condition));
                    properties.getS().add(part.part_name);
                }
                case "day" -> {
                    properties.setDay_t(part.temp_avg);
                    properties.setDay_cond(Dictionaries.conditionMap.get(part.condition));
                    properties.getS().add(part.part_name);
                }
                case "evening" -> {
                    properties.setEvening_t(part.temp_avg);
                    properties.setEvening_cond(Dictionaries.conditionMap.get(part.condition));
                    properties.getS().add(part.part_name);
                }
                case "night" -> {
                    properties.setNight_t(part.temp_avg);
                    properties.setNight_cond(Dictionaries.conditionMap.get(part.condition));
                    properties.getS().add(part.part_name);
                }
            }
        }
        generateTemperatureMessage(weather.fact.temp);
    }

    private static void generateTemperatureMessage(double temperature) {
        properties.setWeatherMessage(switch ((int) temperature) {
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
