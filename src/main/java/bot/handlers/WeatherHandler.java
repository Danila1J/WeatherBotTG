package bot.handlers;

import bot.BotEngine;
import bot.ChatContext;
import bot.service.Dictionaries;
import bot.service.KeyboardButton;
import bot.service.Singleton;
import weather.DescriptionPrediction;
import weather.WeatherApp;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

import static weather.WeatherApp.createWeatherStr;

/**
 * Обрабатывает действия, связанные с погодой, в чате.
 */
public class WeatherHandler {

    private final BotEngine botEngine;
    private final MessageHandler messageHandler;

    /**
     * Конструктор класса WeatherHandler.
     *
     * @param botEngine Экземпляр BotEngine для отправки запросов Telegram API.
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений чата.
     */
    public WeatherHandler(BotEngine botEngine, MessageHandler messageHandler) {
        this.botEngine = botEngine;
        this.messageHandler=messageHandler;
    }

    /**
     * Отправляет информацию о погоде для данного города в чат.
     * @param cityName Название города.
     * @param context Контекст чата.
     */
    public void sendWeatherMessage(String cityName, ChatContext context) {
        Singleton.getInstance().getProperties().setNameCity(cityName);
        WeatherApp.displayWeatherInfo(cityName);
        StringBuilder weather = new StringBuilder()
                .append("Сейчас на улице ").append(Singleton.getInstance().getProperties().getCondition())
                .append("\nТемпература в ").append(Dictionaries.declinationCityMap.get(cityName)).append(": ").append(Math.round(Singleton.getInstance().getProperties().getTempOfCity()))
                .append(" °C\nВетер ").append(Singleton.getInstance().getProperties().getWind()).append(" м/с")
                .append(createWeatherStr(Singleton.getInstance().getProperties().getS())).append("\n\n")
                .append(Singleton.getInstance().getProperties().getWeatherMessage());
        try {
            int weatherMessageId = botEngine.execute(new SendMessage(context.getChatId(), weather.toString())).getMessageId();  // Отправьте сообщение и получите его идентификатор
            messageHandler.deleteMessage(60, weatherMessageId, context);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send weather message", e);
        }
        //TODO Нужна ли эта функция?
        //sendPrediction(cityName);
        messageHandler.editInlineKeyboardMessage("Выберите категорию", KeyboardButton.InlineKeyboardChooseCategory, context);
    }

    /**
     * Отправляет прогноз погоды для данного города в чат.
     * @param cityName Название города.
     * @param context Контекст чата.
     */
    private void sendPrediction(String cityName, ChatContext context) {
        try {
            String p = new DescriptionPrediction().getStringPrediction(cityName);
            if (!Objects.equals(p, "")) {
                int pred_message = botEngine.execute(new SendMessage(context.getChatId(), new DescriptionPrediction().getStringPrediction(cityName))).getMessageId();
                messageHandler.deleteMessage(60, pred_message, context);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }
}
