package schedule;

import com.google.gson.Gson;
import exceptions.TrainsNotFoundException;
import bot.service.Dictionaries;
import bot.service.Properties;
import bot.service.Singleton;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

/**
 * Обрабатывает процесс получения и отображения расписания поездов.
 */
public class Schedule {

    private static final String API_URL = System.getProperty("SCHEDULE_API_URL");
    private static final String API_KEY = System.getProperty("SCHEDULE_API_KEY");
    private final Properties properties = Singleton.getInstance().getProperties();

    /**
     * Извлекает и обрабатывает расписание поездов на основе предоставленных станций.
     *
     * @param departureStation — станция отправления
     * @param arrivalStation станция прибытия
     * @throws TrainsNotFoundException, если поезда не найдены
     */
    public void fetchTrainSchedule(String departureStation, String arrivalStation) throws TrainsNotFoundException {
        String apiEndPoint = buildUrl(departureStation, arrivalStation, getCurrentDate());
        StringBuilder responseBuilder = properties.getStr();
        responseBuilder.delete(0, responseBuilder.length()); // Очищаем responseBuilder
        json.schedule.Root schedule = getTrainTimings(apiEndPoint); // Получаем данные с помощью обращения к API
        parseAndAppendSegments(schedule, responseBuilder);
        if (responseBuilder.length() == 0) { // Электрички не найдены
            responseBuilder.append("Ближайших электричек на сегодня нет");
        }
        try {
            properties.setName_from(schedule.segments.get(0).from.title);
            properties.setName_to(schedule.segments.get(0).to.title);
        } catch (IndexOutOfBoundsException e) {
            throw new TrainsNotFoundException();
        }
    }

    /**
     * Создает URL для API расписания поездов.
     *
     * @param departureStation — станция отправления
     * @param arrivalStation станция прибытия
     * @param date дата, на которую нужно расписание
     * @return построенная URL в виде строки
     */
    private String buildUrl(String departureStation, String arrivalStation, String date) {
        return Schedule.API_URL + Schedule.API_KEY + "&format=json"
                + "&from=" + Dictionaries.cityCodeMap.get(departureStation)
                + "&to=" + Dictionaries.cityCodeMap.get(arrivalStation)
                + "&lang=ru_RU&page=1"
                + "&date=" + date
                + "&transport_types=suburban";
    }

    /**
     * Получает текущую дату.
     *
     * @return  текущую дату в виде строки
     */
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    /**
     * Получает время поезда на основе предоставленной конечной точки API.
     *
     * @param apiEndPoint конечная точка API
     * @return Корневой объект расписания
     */
    private json.schedule.Root getTrainTimings(String apiEndPoint) {
        try (InputStreamReader reader = new InputStreamReader(new URL(apiEndPoint).openStream())) {
            return new Gson().fromJson(reader, json.schedule.Root.class);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while fetching train timings", e);
        }
    }

    /**
     * Разбирает сегменты поезда и добавляет их к предоставленному StringBuilder.
     *
     * @param schedule расписание для разбора сегментов из
     * @param responseBuilder — StringBuilder для добавления сведений о сегменте
     */
    private void parseAndAppendSegments(json.schedule.Root schedule, StringBuilder responseBuilder) {
        for (json.schedule.Segment segment : schedule.segments) {
            if (isSegmentSkipped(segment)) {
                continue;
            }
            appendSegmentDetails(segment, responseBuilder);
        }
    }

    /**
     * Определяет, следует ли пропускать определенный сегмент или нет.
     *
     * @param segment сегмент
     * @return true, если сегмент следует пропустить, иначе false
     */
    private boolean isSegmentSkipped(json.schedule.Segment segment) {
        return LocalTime.parse(segment.departure.substring(11, 16)).isBefore(LocalTime.now());
    }

    /**
     * Добавляет сведения о конкретном сегменте к предоставленному StringBuilder.
     *
     * @param segment сегмент
     * @param responseBuilder — StringBuilder для добавления сведений о сегменте к
     */
    private void appendSegmentDetails(json.schedule.Segment segment, StringBuilder responseBuilder) {
        responseBuilder.append(segment.departure, 11, 16)
                .append(" -> ")
                .append(segment.arrival, 11, 16)
                .append("\n\n");
    }
}
