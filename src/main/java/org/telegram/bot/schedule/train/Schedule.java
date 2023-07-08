package org.telegram.bot.schedule.train;

import com.google.gson.Gson;
import org.telegram.bot.service.files.Dictionaries;
import org.telegram.bot.service.files.Singleton;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

public class Schedule {
    public static void rasp(String station_1, String station_2) throws Exception {
        Singleton.getInstance().getProperties().getStr().delete(0, Singleton.getInstance().getProperties().getStr().length());
        String now = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());



        //1
//        URL url = new URL(RASP_API_URL + Dictionaries.cityCodeMap.get(station_1) + "&to=" + Dictionaries.cityCodeMap.get(station_2)
//                + "&lang=ru_RU&page=1&date=" + now + "&transport_types=suburban");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("X-Yandex-API-Key", API_KEY);
//        String line;
//        try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
//            line = scanner.useDelimiter("\\A").next();
//        }
//        json.rasp.Root rasp = new Gson().fromJson(line, json.rasp.Root.class);


        //2
        try (InputStreamReader reader = new InputStreamReader(
                new URL(System.getProperty("RASP_API_URL") + System.getProperty("RASP_API_KEY") + "&format=json"
                        + "&from=" + Dictionaries.cityCodeMap.get(station_1)
                        + "&to=" + Dictionaries.cityCodeMap.get(station_2)
                        + "&lang=ru_RU&page=1"
                        + "&date=" + now
                        + "&transport_types=suburban").openStream())) {
            json.rasp.Root rasp = new Gson().fromJson(reader, json.rasp.Root.class);


            for (json.rasp.Segment segment : rasp.segments) {
                int departureHour = Integer.parseInt(segment.departure.substring(11, 13));
                int departureMinute = Integer.parseInt(segment.departure.substring(14, 16));

                if ((departureHour * 60 + departureMinute) < (LocalTime.now().getHour() * 60 + LocalTime.now().getMinute())) {
                    continue;
                }
                Singleton.getInstance().getProperties().getStr().append(segment.departure, 11, 16)
                        .append(" -> ")
                        .append(segment.arrival, 11, 16)
                        .append("\n\n");
            }

            if (Singleton.getInstance().getProperties().getStr().length() == 0) {
                Singleton.getInstance().getProperties().getStr().setLength(0);
                Singleton.getInstance().getProperties().getStr().append("Ближайших электричек на сегодня нет");
            }

            Singleton.getInstance().getProperties().setName_from(rasp.segments.get(0).from.title);
            Singleton.getInstance().getProperties().setName_to(rasp.segments.get(0).to.title);
        }
    }
}
