package org.telegram.bot.handlers;

import org.telegram.bot.BotEngine;
import org.telegram.bot.schedule.train.Schedule;
import org.telegram.bot.service.files.KeyboardButton;
import org.telegram.bot.service.files.Singleton;
import org.telegram.bot.service.files.TimeOfDay;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ScheduleHandler {
    private final BotEngine botEngine;
    private final MessageHandler messageHandler;

    public ScheduleHandler(BotEngine botEngine, MessageHandler messageHandler) {
        this.botEngine = botEngine;
        this.messageHandler = messageHandler;
    }

    public void sendRasp(String station1, String station2) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            try {
                Schedule.rasp(station1, station2);
                stringBuilder.append(TimeOfDay.timeOfDay(botEngine.getUserName())).append("\n\nРасписание ").append(station1).append("🚂").append(" - ").append(station2).append("🚂").append("\n\n").append(Singleton.getInstance().getProperties().getStr());
            } catch (Exception e) {
                botEngine.execute(new SendMessage(botEngine.getChatId(), "На выбранном направлении нет электричек"));
            }
            int rasp_message = botEngine.execute(new SendMessage(botEngine.getChatId(), stringBuilder.toString())).getMessageId();

            messageHandler.deleteMessage(60, rasp_message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send rasp message", e);
        }
        messageHandler.editInlineKeyBoardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory);
    }
}
