package org.telegram.bot.schedule.train;

import com.google.gson.Gson;
import org.telegram.bot.service.files.Dictionaries;
import org.telegram.bot.service.files.Properties;
import org.telegram.bot.service.files.Singleton;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

public class Schedule {

    private static final String API_URL = System.getProperty("SCHEDULE_API_URL");
    private static final String API_KEY = System.getProperty("SCHEDULE_API_KEY");
    private final Properties properties = Singleton.getInstance().getProperties();

    public void train(String departureStation, String arrivalStation) {
        String apiEndPoint = constructUrl(departureStation, arrivalStation, getCurrentDate());
        StringBuilder responseBuilder = properties.getStr();

        //Очищаем StringBuilder
        responseBuilder.delete(0, responseBuilder.length());

        //Получаем данные API
        json.schedule.Root schedule = fetchTrainTimings(apiEndPoint);

        parseSegments(schedule, responseBuilder);

        //Электрички не найдены
        if (responseBuilder.length() == 0) {
            responseBuilder.append("Ближайших электричек на сегодня нет");
        }

        properties.setName_from(schedule.segments.get(0).from.title);
        properties.setName_to(schedule.segments.get(0).to.title);
    }

    private String constructUrl(String departureStation, String arrivalStation, String date) {
        return Schedule.API_URL + Schedule.API_KEY + "&format=json"
                + "&from=" + Dictionaries.cityCodeMap.get(departureStation)
                + "&to=" + Dictionaries.cityCodeMap.get(arrivalStation)
                + "&lang=ru_RU&page=1"
                + "&date=" + date
                + "&transport_types=suburban";
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private json.schedule.Root fetchTrainTimings(String apiEndPoint) {
        try (InputStreamReader reader = new InputStreamReader(new URL(apiEndPoint).openStream())) {
            return new Gson().fromJson(reader, json.schedule.Root.class);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while fetching train timings", e);
        }
    }

    private void parseSegments(json.schedule.Root schedule, StringBuilder responseBuilder) {
        for (json.schedule.Segment segment : schedule.segments) {
            if (shouldSkipSegment(segment)) {
                continue;
            }
            appendSegmentDetails(segment, responseBuilder);
        }
    }

    private boolean shouldSkipSegment(json.schedule.Segment segment) {
        return LocalTime.parse(segment.departure.substring(11, 16)).isBefore(LocalTime.now());
    }

    private void appendSegmentDetails(json.schedule.Segment segment, StringBuilder responseBuilder) {
        responseBuilder.append(segment.departure, 11, 16)
                .append(" -> ")
                .append(segment.arrival, 11, 16)
                .append("\n\n");
    }
}
