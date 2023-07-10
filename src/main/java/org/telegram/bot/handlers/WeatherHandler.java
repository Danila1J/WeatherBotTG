package org.telegram.bot.handlers;

import org.telegram.bot.BotEngine;
import org.telegram.bot.service.files.Dictionaries;
import org.telegram.bot.service.files.KeyboardButton;
import org.telegram.bot.service.files.Singleton;
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
        StringBuilder weather = new StringBuilder()
                .append("Сейчас на улице ").append(Singleton.getInstance().getProperties().getCondition())
                .append("\nТемпература в ").append(Dictionaries.declinationCityMap.get(cityName)).append(": ").append(Math.round(Singleton.getInstance().getProperties().getTempOfCity()))
                .append(" °C\nВетер ").append(Singleton.getInstance().getProperties().getWind()).append(" м/с")
                .append(createWeatherStr(Singleton.getInstance().getProperties().getS())).append("\n\n")
                .append(Singleton.getInstance().getProperties().getWeatherMessage());
        try {
            int weather_message = botEngine.execute(new SendMessage(botEngine.getChatId(), weather.toString())).getMessageId();  // Отправьте сообщение и получите его идентификатор
            messageHandler.deleteMessage(60, weather_message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send weather message", e);
        }
        //TODO Нужна ли эта функция?
        //sendPrediction(cityName);
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
