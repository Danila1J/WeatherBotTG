package weather;

import bot.service.Dictionaries;
import bot.service.Properties;
import bot.service.Singleton;
import com.google.gson.Gson;
import json.weather.Part;
import json.weather.Root;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class WeatherApp {

    private static final Gson gson = new Gson();
    private static final Properties properties = Singleton.getInstance().getProperties();

    /**
     * Получает информацию о погоде на основе заданного названия города.
     *
     * @param selectedCity название города для получения погоды
     * @return данные о погоде в виде класса Root
     */
    private static Root fetchWeatherData(String selectedCity) {
        Root fetchedWeather = null;
        try {
            URL url = new URL(System.getProperty("WEATHER_API_URL") + Dictionaries.cityCoordMap.get(selectedCity) + "&[lang=ru_RU]");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Yandex-API-Key", System.getProperty("WEATHER_API_KEY"));
            String line;
            try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
                line = scanner.useDelimiter("\\A").next();
            }
            fetchedWeather = gson.fromJson(line, Root.class);
        } catch (IOException e) {
            System.out.println("An exception occurred, likely due to reaching the request limit.");
        }
        return fetchedWeather;
    }

    /**
     * Создает строку информации о погоде на основе заданного списка погоды.
     *
     * @param weatherList список строк, представляющих погодные условия
     * @return строка, представляющая погодные условия
     */
    public static String constructWeatherInfoString(List<String> weatherList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String weatherCondition : weatherList) {
            appendWeatherConditionDetails(weatherCondition, stringBuilder);
        }
        return stringBuilder.toString();
    }

    /**
     * Получает, обрабатывает и отображает информацию о погоде для определенного города.
     *
     * @param cityName название города для отображения погоды
     */
    public static void displayWeatherInfoForCity(String cityName) {
        Root weatherData = fetchWeatherData(cityName);
        clearAndSetWeatherProperties(weatherData);
        generateTemperatureMessage(weatherData.fact.temp);
    }

    /**
     * Очищает существующие свойства погоды и устанавливает новые на основе заданных данных о погоде.
     *
     * @param weatherData данные о погоде для извлечения свойств
     */
    private static void clearAndSetWeatherProperties(Root weatherData) {
        properties.getS().clear();
        setWeatherProperties(weatherData);
        setWeatherConditionPartsProperties(weatherData);
    }

    /**
     * Устанавливает общие свойства погоды в Properties.
     *
     * @param weatherData данные о погоде для извлечения свойств
     */
    private static void setWeatherProperties(Root weatherData) {
        properties.setTempOfCity(weatherData.fact.temp);
        properties.setCondition(Dictionaries.conditionMap.get(weatherData.fact.condition));
        properties.setWind(weatherData.fact.wind_speed);
    }

    /**
     * Задает свойства погоды для разных частей дня.
     *
     * @param weatherData данные о погоде для извлечения свойств
     */
    private static void setWeatherConditionPartsProperties(Root weatherData) {
        for (Part part : weatherData.forecast.parts) {
            setPartWeatherProperties(part);
        }
    }

    /**
     * Задает свойства для определенной части дня.
     *
     * @param part часть дня
     */
    private static void setPartWeatherProperties(Part part) {
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

    /**
     * Добавляет сведения о конкретных погодных условиях к заданному StringBuilder.
     *
     * @param weatherCondition погодные условия в виде строки (например, «утро», «день» и т. д.)
     * @param stringBuilder    StringBuilder для добавления
     */
    private static void appendWeatherConditionDetails(String weatherCondition, StringBuilder stringBuilder) {
        switch (weatherCondition) {
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

    /**
     * Генерирует сообщение на основе текущей температуры и устанавливает его в качестве сообщения о погоде.
     *
     * @param temperature текущая температура
     */
    private static void generateTemperatureMessage(double temperature) {
        properties.setWeatherMessage(getWeatherMessageBasedOnTemperature((int) temperature));
    }

    /**
     * Возвращает сообщение о погоде в соответствии с предоставленным диапазоном температур.
     *
     * @param temperature температура
     * @return соответствующее сообщение о погоде в виде строки
     */
    private static String getWeatherMessageBasedOnTemperature(int temperature) {
        return switch (temperature) {
            case 0, 1, 2, 3, 4 -> "Ещё слишком холодно носить лёгкую осеннюю одежду, одеваемся во что-то тёплое!!!\nБез шапки ходить ещё рановато";
            case 5, 6, 7, 8, 9 -> "Сейчас холодно одеваемся потеплее!\nШарфик и перчатки могут пригодиться";
            case 10, 11, 12, 13, 14 -> "Сейчас прохладно, лучше надеть что-то теплее куртку/пальто";
            case 15, 16, 17, 18, 19 -> "Сейчас достаточно тепло, так что можешь надеть свитер и штаны)";
            case 20, 21, 22, 23, 24 -> "Сейчас тепло, можно надеть толстовку/лёгкий свитер или даже майку";
            case 25, 26, 27, 28, 29 -> "Можно надеть майку и штаны, а возможно и шорты))";
            case 30, 31, 32, 33, 34, 35, 36 -> "Выходить на улицу в крайнем случае!!!\nНа улице очень жарко";
            default -> "Invalid temperature";
        };
    }
}