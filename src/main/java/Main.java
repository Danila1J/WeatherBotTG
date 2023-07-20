import bot.BotEngine;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

public class Main {
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