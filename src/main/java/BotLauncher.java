import bot.BotEngine;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

/**
 * Основной класс для запуска Telegram Bot
 */
public class BotLauncher {

    /**
     * Основной способ запуска Telegram Bot
     *
     * @param args аргументы командной строки
     * @throws TelegramApiException если есть проблема с выполнением запроса Telegram API
     * @throws IOException если есть проблема с загрузкой файла свойств
     */
    public static void main(String[] args) throws TelegramApiException, IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("config.properties"));
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(new BotEngine());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
