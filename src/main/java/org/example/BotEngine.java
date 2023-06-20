package org.example;

import org.example.weather.WeatherApp;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.example.Dictionaries.arrivalStationMap;
import static org.example.Dictionaries.translatedCitiesMap;

public class BotEngine extends TelegramLongPollingBot {
    private static final String botUserName = "Погода для котиков";
    private static final String token = "5328248497:AAGMCitu_3E_sZ5sKVheqAllqAIsD__XGeI";
    private String chatId;
    private String userName = "";
    private Integer messageId;

    private final Map<String, Consumer<String>> callbackHandlersCityWeather = new HashMap<>();
    private final Map<String, Runnable> callbackHandlersToStation = new HashMap<>();
    private final Map<String, Runnable> callbackHandlers = new HashMap<>();

    private void initCallbackHandlersCityWeather() {
        callbackHandlersCityWeather.putAll(Map.of(
                "WeatherSamara", this::sendWeatherMessage,
                "WeatherNovokybishevsk", this::sendWeatherMessage
        ));
    }

    private void initCallbackHandlersToStation() {
        callbackHandlersToStation.putAll(Map.of(
                "FromSamara", () -> editInlineKeyBoardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalSamara),
                "FromMirnaya", () -> editInlineKeyBoardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalMirnaya),
                "FromLipyagi", () -> editInlineKeyBoardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalLipyagi),
                "FromSrednevolzhskaya", () -> editInlineKeyBoardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalSrednevolzhskaya)
        ));
    }

    private void initCallbackHandlers() {
        callbackHandlers.putAll(Map.of(
                "WeatherApp", () -> editInlineKeyBoardMessage("Выберите город", KeyboardButton.InlineKeyboardChooseCity),
                "TabletimeFrom", () -> editInlineKeyBoardMessage("Cтанция отправления", KeyboardButton.InlineKeyboardChooseDeparture),
                "ToMirnaya", () -> sendRasp(Properties.getName_from(), "Мирная"),
                "ToSamara", () -> sendRasp(Properties.getName_from(), "Самара"),
                "ToLipyagi", () -> sendRasp(Properties.getName_from(), "Липяги"),
                "ToSrednevolzhskaya", () -> sendRasp(Properties.getName_from(), "Средневолжская")
        ));
    }

    BotEngine() {
        super(token);
        initCallbackHandlersCityWeather();
        initCallbackHandlersToStation();
        initCallbackHandlers();
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            sendFirstMessage(update.getMessage());
        }
        if (update.hasCallbackQuery()) {
            processCallbackQuery(update.getCallbackQuery());
            chatId=update.getCallbackQuery().getMessage().getChatId().toString();
        }
    }

    private void sendFirstMessage(Message message) {
        userName = message.getFrom().getFirstName();
        chatId = message.getChatId().toString();
        SendMessage sendMessage=new SendMessage(chatId,"Выберите  категорию");
        sendMessage.setReplyMarkup(KeyboardButton.InlineKeyboardChooseCategory);
        try {
            messageId=execute(sendMessage).getMessageId();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        //sendInlineKeyBoardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory);
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        //Отправка кнопок в сообщениях
        if (callbackHandlers.containsKey(callbackQuery.getData())) {
            callbackHandlers.get(callbackQuery.getData()).run();
            //if(Objects.equals(callbackQuery.getData(), "WeatherApp")) messageId=callbackQuery.getMessage().getMessageId();
        }
        //Погода в городах
        if (callbackHandlersCityWeather.containsKey(callbackQuery.getData())) {
            callbackHandlersCityWeather.get(callbackQuery.getData()).accept(translatedCitiesMap.get(callbackQuery.getData()));
        }
        //Расписание
        if (callbackHandlersToStation.containsKey(callbackQuery.getData())) {
            callbackHandlersToStation.get(callbackQuery.getData()).run();
            Properties.setName_from(arrivalStationMap.get(callbackQuery.getData()));
        }
    }

    private void sendWeatherMessage(String cityName) {
        Properties.setNameCity(cityName);
        WeatherApp.displayWeatherInfo();
        String weather = timeOfDay() +
                "\n\nСейчас на улице " + Properties.getCondition() +
                "\nТемпература в " + Dictionaries.declinationCityMap.get(cityName) + ": " + Math.round(Properties.getTempOfCity()) +
                " °C\nВетер " + Properties.getWind() + " м/с" +
                createWeatherStr(Properties.getS()) + "\n\n" +
                Properties.getWeatherMessage();
        SendMessage sendMessage = new SendMessage(chatId, weather);
        try {
            //хочу чтобы тут добавлялись id отосланных сообщений в очередь FIFO
            int weather_message=execute(sendMessage).getMessageId();
            //И спустя 5 минут сообщения удалялись
            DeleteMessage deleteMessage=new DeleteMessage(chatId,weather_message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send weather message", e);
        }
        editInlineKeyBoardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory);
    }

    private void sendRasp(String station1, String station2) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            try {
                Rasp.rasp(station1, station2);
                stringBuilder.append(timeOfDay()).append("\n\nРасписание ").append(station1).append("🚂").append(" - ").append(station2).append("🚂").append("\n\n").append(Properties.str);
            } catch (Exception e) {
                execute(new SendMessage(chatId, "На выбранном направлении нет электричек"));
            }
            execute(new SendMessage(chatId, stringBuilder.toString()));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send rasp message", e);
        }
        editInlineKeyBoardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory);
    }

    private void editInlineKeyBoardMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText editMessageText = new EditMessageText(text);
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static String createWeatherStr(List<String> weather) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : weather) {
            switch (s) {
                case "morning" ->
                        stringBuilder.append("\nУтро: ").append(Properties.getMorning_t()).append("°C ").append(Properties.getMorning_cond());
                case "day" ->
                        stringBuilder.append("\nДень: ").append(Properties.getDay_t()).append("°C ").append(Properties.getDay_cond());
                case "evening" ->
                        stringBuilder.append("\nВечер: ").append(Properties.getEvening_t()).append("°C ").append(Properties.getEvening_cond());
                case "night" ->
                        stringBuilder.append("\nНочь: ").append(Properties.getNight_t()).append("°C ").append(Properties.getNight_cond());
            }
        }
        return stringBuilder.toString();
    }

    private String timeOfDay() {
        final int endOneNight = (6 * 60) * 60;
        final int startMorning = (6 * 60) * 60 + 1;
        final int endMorning = (12 * 60) * 60;
        final int startDay = (12 * 60) * 60 + 1; // День - средина этого периода 12:00
        final int endDay = (18 * 60) * 60;
        final int startEvening = (18 * 60) * 60 + 1;
        final int endEvening = (24 * 60) * 60 - 1;
        final int startTwoNight = 0; //(0 * 60) * 60; // Максимально возможное кол-во секунд = DateTime.MaxValue = 86399
        // Без секунды полночь
        // Для наглядности: const int EndTwoNight = (24 * 60) * 60 - 1;

        LocalTime nowTime = LocalTime.now();
        int seconds = nowTime.toSecondOfDay();

        if (seconds <= endOneNight && seconds >= startTwoNight) {
            return "Доброй ночи, а потиму ещё не спим\nВсе кошечки и котики уже давно спят";
        } else if (seconds >= startMorning && seconds <= endMorning) {
            return "Проснулись, улыбнулись\nПервым делом идём чистить зубки и идём кушать";
        } else if (seconds >= startDay && seconds <= endDay) {
            return "Добрый день, " + userName;
        } else if (seconds >= startEvening && seconds <= endEvening) {
            return "Добрый вечер, " + userName;
        }
        return "";
    }
}