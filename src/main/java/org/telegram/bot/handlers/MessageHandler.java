package org.telegram.bot.handlers;

import org.telegram.bot.BotEngine;
import org.telegram.bot.service.files.KeyboardButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageHandler {

    private final BotEngine botEngine;

    public MessageHandler(BotEngine botEngine) {
        this.botEngine = botEngine;
    }

    public void sendFirstMessage(Message message) {
        botEngine.setChatId(message.getChatId().toString());
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Выберите  категорию");
        sendMessage.setReplyMarkup(KeyboardButton.InlineKeyboardChooseCategory);
        try {
            botEngine.setFirstMessageId(botEngine.execute(sendMessage).getMessageId());
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send first message",e);
        }
    }

    public void deleteMessage(int delaySeconds, int idMessage) {
        // Создайте задачу для удаления сообщений через 1 минуту
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            // Удалите сообщение
            DeleteMessage deleteMessage = new DeleteMessage(botEngine.getChatId(), idMessage);
            try {
                botEngine.execute(deleteMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Failed to delete message",e);
            }
        }, delaySeconds, TimeUnit.SECONDS);

        executorService.shutdown();
    }

    public void editInlineKeyBoardMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText editMessageText = new EditMessageText(text);
        editMessageText.setChatId(botEngine.getChatId());
        editMessageText.setMessageId(botEngine.getFirstMessageId());
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        try {
            botEngine.execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to edit InlineKeyBoard in message",e);
        }
    }
}
