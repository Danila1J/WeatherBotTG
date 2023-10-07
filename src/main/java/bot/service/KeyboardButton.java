package bot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class KeyboardButton {
    // Варианты выбора категории
    public static final InlineKeyboardMarkup InlineKeyboardChooseCategory = createInlineKeyboard("Погода", "WeatherApp", "Расписание электричек", "TabletimeFrom", "Маршруты", "Routes", "Поиск лекарств", "FindMedicines");

    // Варианты выбора города
    public static final InlineKeyboardMarkup InlineKeyboardChooseCity = createInlineKeyboard("Самара", "WeatherSamara", "Новокуйбышевск", "WeatherNovokybishevsk");

    // Варианты выбора станции отправления
    public static final InlineKeyboardMarkup InlineKeyboardChooseDeparture = createInlineKeyboard("Cамара", "FromSamara", "Мирная", "FromMirnaya", "Липяги", "FromLipyagi", "Средневолжская", "FromSrednevolzhskaya", "Сокская", "FromSokskaya", "Похвистнево", "FromPohvistnevo");

    private  static List<String> stations = Arrays.asList("ToMirnaya", "ToLipyagi", "ToSrednevolzhskaya", "ToSokskaya", "ToPohvistnevo", "ToSamara");

    // Map станций прибытия
    private static final Map<String, List<String>> departureToArrival = Map.of(
            "FromSamara", stations.stream().filter(station -> !station.equals("ToSamara")).toList(),
            "FromMirnaya", stations.stream().filter(station -> !station.equals("ToMirnaya")).toList(),
            "FromLipyagi", stations.stream().filter(station -> !station.equals("ToLipyagi")).toList(),
            "FromSrednevolzhskaya", stations.stream().filter(station -> !station.equals("ToSrednevolzhskaya")).toList(),
            "FromSokskaya", stations.stream().filter(station -> !station.equals("ToSokskaya")).toList(),
            "FromPohvistnevo", stations.stream().filter(station -> !station.equals("ToPohvistnevo")).toList()
            );

    // Варианты выбора маршрутов
    public static final InlineKeyboardMarkup InlineKeyboardChooseRoutes = createInlineKeyboard("Телецентр -> Ж/Д", "Telecentre->Railway", "Ж/Д -> Телецентр", "Railway->Telecentre");

    public static InlineKeyboardMarkup createInlineKeyboard(String... buttons) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < buttons.length; i += 2) {
            String buttonText = buttons[i];
            String callbackData = buttons[i + 1];
            InlineKeyboardButton button = new InlineKeyboardButton(buttonText);
            button.setCallbackData(callbackData);
            rows.add(Collections.singletonList(button));
        }
        return new InlineKeyboardMarkup(rows);
    }

    // Метод создания InlineKeyboardMarkup для заданной станции отправления
    public static InlineKeyboardMarkup createInlineKeyboardForDeparture(String departure) {
        List<String> arrivals = departureToArrival.get(departure);
        if (arrivals == null) {
            throw new IllegalArgumentException("Invalid departure station: " + departure);
        }
        List<String> buttons = new ArrayList<>();
        for (String arrival : arrivals) {
            buttons.add(Dictionaries.departureStationMap.get(arrival));
            buttons.add(arrival);
        }
        return createInlineKeyboard(buttons.toArray(new String[0]));
    }
}