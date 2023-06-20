package org.example;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyboardButton {
    // Варианты выбора категории
    public static final InlineKeyboardMarkup InlineKeyboardChooseCategory = createInlineKeyboard("Погода", "WeatherApp","Расписание электричек", "TabletimeFrom");

    // Варианты выбора города
    public static final InlineKeyboardMarkup InlineKeyboardChooseCity = createInlineKeyboard("Самара", "WeatherSamara","Новокуйбышевск", "WeatherNovokybishevsk");

    // Варианты выбора станции отправления
    public static final InlineKeyboardMarkup InlineKeyboardChooseDeparture = createInlineKeyboard("Cамара", "FromSamara","Мирная", "FromMirnaya","Липяги", "FromLipyagi","Средневолжская", "FromSrednevolzhskaya");

    // Варианты выбора станции прибытия (для Самары)
    public static final InlineKeyboardMarkup InlineKeyboardChooseArrivalSamara = createInlineKeyboard("Мирная", "ToMirnaya","Липяги", "ToLipyagi","Средневолжская", "ToSrednevolzhskaya");

    // Варианты выбора станции прибытия (для Мирной)
    public static final InlineKeyboardMarkup InlineKeyboardChooseArrivalMirnaya = createInlineKeyboard("Cамара", "ToSamara","Липяги", "ToLipyagi","Средневолжская", "ToSrednevolzhskaya");

    // Варианты выбора станции прибытия (для Липяг)
    public static final InlineKeyboardMarkup InlineKeyboardChooseArrivalLipyagi = createInlineKeyboard("Cамара", "ToSamara","Мирная", "ToMirnaya","Средневолжская", "ToSrednevolzhskaya");

    // Варианты выбора станций прибытия (для Средневолжской)
    public static final InlineKeyboardMarkup InlineKeyboardChooseArrivalSrednevolzhskaya = createInlineKeyboard("Cамара", "ToSamara","Мирная", "ToMirnaya","Липяги", "ToLipyagi");

    private static InlineKeyboardMarkup createInlineKeyboard(String... buttons) {
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
}