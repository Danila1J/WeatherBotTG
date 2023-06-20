package org.example;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Rasp {

    private static final String RASP_API_URL = "https://api.rasp.yandex.net/v3.0/search/?";
    private static final String API_KEY = "bd085f91-2540-42a4-94c3-d0f4c62a1f8f";

    public static void rasp(String station_1, String station_2) throws Exception {
        Properties.str.delete(0, Properties.str.length());
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
                new URL("https://api.rasp.yandex.net/v3.0/search/?apikey=ac685133-7f4d-4df0-b343-704ad839e3f8&format=json&from="
                        + Dictionaries.cityCodeMap.get(station_1) + "&to=" + Dictionaries.cityCodeMap.get(station_2)
                        + "&lang=ru_RU&page=1&date=" + now + "&transport_types=suburban").openStream())) {
            json.rasp.Root rasp = new Gson().fromJson(reader, json.rasp.Root.class);


            for (json.rasp.Segment segment : rasp.segments) {
                int departureHour = Integer.parseInt(segment.departure.substring(11, 13));
                int departureMinute = Integer.parseInt(segment.departure.substring(14, 16));

                if ((departureHour * 60 + departureMinute) < (LocalTime.now().getHour() * 60 + LocalTime.now().getMinute())) {
                    continue;
                }
                Properties.str.append(segment.departure, 11, 16)
                        .append(" -> ")
                        .append(segment.arrival, 11, 16)
                        .append("\n\n");
            }

            if (Properties.str.length() == 0) {
                Properties.str.setLength(0);
                Properties.str.append("Ближайших электричек на сегодня нет");
            }

            Properties.setName_from(rasp.segments.get(0).from.title);
            Properties.setName_to(rasp.segments.get(0).to.title);
        }
    }
}
