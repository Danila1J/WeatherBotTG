package bot.handlers;

import bot.BotEngine;
import bot.ChatContext;
import bot.service.Dictionaries;
import bot.service.KeyboardButton;
import bot.service.Singleton;
import weather.WeatherPredictionManager;
import weather.WeatherApp;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

import static weather.WeatherApp.constructWeatherInfoString;

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
     * @param context Текущий контекст чата.
     */
    public void sendWeatherInfo(String cityName, ChatContext context) {
        Singleton.getInstance().getProperties().setNameCity(cityName);
        WeatherApp.displayWeatherInfoForCity(cityName);
        String weatherInfoStr = buildWeatherInfoString(cityName);
        sendWeatherInfoToChat(weatherInfoStr, context);
        messageHandler.updateInlineKeyboardMessage("Выберите категорию", KeyboardButton.InlineKeyboardChooseCategory, context);
    }

    /**
     * Создает форматированную строку с информацией о погоде для отправки в чат.
     * @param cityName Название города.
     * @return Строка с информацией о погоде в городе.
     */
    private String buildWeatherInfoString(String cityName) {
        return new StringBuilder()
                .append("Сейчас на улице ").append(Singleton.getInstance().getProperties().getCondition())
                .append("\nТемпература в ").append(Dictionaries.declinationCityMap.get(cityName)).append(": ").append(Math.round(Singleton.getInstance().getProperties().getTempOfCity()))
                .append(" °C\nВетер ").append(Singleton.getInstance().getProperties().getWind()).append(" м/с")
                .append(constructWeatherInfoString(Singleton.getInstance().getProperties().getS())).append("\n\n")
                .append(Singleton.getInstance().getProperties().getWeatherMessage())
                .toString();
    }

    /**
     * Отправляет информацию о погоде в чат.
     *
     * @param weatherInfo Отправляемая информация о погоде.
     * @param context Текущий контекст чата.
     * @throws RuntimeException Если сообщение не может быть отправлено.
     */
    private void sendWeatherInfoToChat(String weatherInfo, ChatContext context) {
        try {
            int weatherMessageId = botEngine.execute(new SendMessage(context.getChatId(), weatherInfo)).getMessageId();
            messageHandler.deleteMessageAfterDelay(60, weatherMessageId, context);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send weather message", e);
        }
    }

    /**
     * Отправляет прогноз погоды для данного города в чат.
     * Этот метод в настоящее время не используется.
     * @param cityName Название города.
     * @param context Текущий контекст чата.
     * @throws RuntimeException Если сообщение не может быть отправлено.
     */
    private void sendWeatherPrediction(String cityName, ChatContext context) {
        try {
            String p = new WeatherPredictionManager().fetchWeatherPrediction(cityName);
            if (!Objects.equals(p, "")) {
                int predictionMessageId = botEngine.execute(new SendMessage(context.getChatId(), new WeatherPredictionManager().fetchWeatherPrediction(cityName))).getMessageId();
                messageHandler.deleteMessageAfterDelay(60, predictionMessageId, context);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }
}
