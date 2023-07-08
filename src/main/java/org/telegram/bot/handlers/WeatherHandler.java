package org.telegram.bot.handlers;

import org.telegram.bot.BotEngine;
import org.telegram.bot.service.files.Dictionaries;
import org.telegram.bot.service.files.KeyboardButton;
import org.telegram.bot.service.files.Singleton;
import org.telegram.bot.service.files.TimeOfDay;
import org.telegram.bot.weather.DescriptionPrediction;
import org.telegram.bot.weather.WeatherApp;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

import static org.telegram.bot.weather.WeatherApp.createWeatherStr;

public class WeatherHandler {

    private final BotEngine botEngine;
    private final MessageHandler messageHandler;

    public WeatherHandler(BotEngine botEngine, MessageHandler messageHandler) {
        this.botEngine = botEngine;
        this.messageHandler=messageHandler;
    }

    public void sendWeatherMessage(String cityName) {
        Singleton.getInstance().getProperties().setNameCity(cityName);
        WeatherApp.displayWeatherInfo(cityName);
        String weather = TimeOfDay.timeOfDay(botEngine.getUserName()) +
                "\n\nСейчас на улице " + Singleton.getInstance().getProperties().getCondition() +
                "\nТемпература в " + Dictionaries.declinationCityMap.get(cityName) + ": " + Math.round(Singleton.getInstance().getProperties().getTempOfCity()) +
                " °C\nВетер " + Singleton.getInstance().getProperties().getWind() + " м/с" +
                createWeatherStr(Singleton.getInstance().getProperties().getS()) + "\n\n" +
                Singleton.getInstance().getProperties().getWeatherMessage();
        try {
            int weather_message = botEngine.execute(new SendMessage(botEngine.getChatId(), weather)).getMessageId();  // Отправьте сообщение и получите его идентификатор
            messageHandler.deleteMessage(60, weather_message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send weather message", e);
        }
        sendPrediction(cityName);
        messageHandler.editInlineKeyBoardMessage("Выберите категорию", KeyboardButton.InlineKeyboardChooseCategory);
    }

    private void sendPrediction(String cityName) {
        try {
            String p = new DescriptionPrediction().getStringPrediction(cityName);
            if (!Objects.equals(p, "")) {
                int pred_message = botEngine.execute(new SendMessage(botEngine.getChatId(), new DescriptionPrediction().getStringPrediction(cityName))).getMessageId();
                messageHandler.deleteMessage(60, pred_message);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }
}
