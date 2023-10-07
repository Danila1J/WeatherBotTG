package bot.handlers;

import all.apteki.FindMedicines;
import bot.BotEngine;
import bot.ChatContext;
import bot.service.Dictionaries;
import bot.service.KeyboardButton;
import exceptions.ElementNotFoundException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * Обрабатывает действия, связанные с маршрутом, в чате.
 */
public class MedicinesHandler {

    private final BotEngine botEngine;
    private final MessageHandler messageHandler;
    private final FindMedicines findMedicines;
    private ChromeDriver chromeDriverMed;

    /**
     * Конструктор класса MedicinesHandler.
     *
     * @param botEngine      Экземпляр BotEngine для отправки запросов Telegram API.
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений чата.
     */
    public MedicinesHandler(BotEngine botEngine, MessageHandler messageHandler, ChromeDriver chromeDriverMed) {
        this.botEngine = botEngine;
        this.messageHandler = messageHandler;
        this.findMedicines = new FindMedicines(chromeDriverMed);
        this.chromeDriverMed = chromeDriverMed;
    }

    /**
     * Отправляет кнопки с выбором формы выпуска лекарства в чат.
     *
     * @param name    Название лекарства.
     * @param context Контекст чата.
     */
    public void sendButtonReleaseForm(String name, ChatContext context) {
        List<String> strings = null;
        try {
            strings = findMedicines.getMedicinesDetails(name);
        } catch (ElementNotFoundException e) {
            sendThenDeleteMessage("Лекарство не найдено", context, 60);
            messageHandler.updateInlineKeyboardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory, context);
        }

        String[] buttonFormRelease = new String[strings.size() * 2];
        for (int i = 0; i < strings.size(); i++) {
            if (Dictionaries.releaseForm.containsKey(strings.get(i))) {
                buttonFormRelease[i * 2] = Dictionaries.releaseForm.get(strings.get(i));
                buttonFormRelease[i * 2 + 1] = Dictionaries.releaseForm.get(strings.get(i));
            }else {
                buttonFormRelease[i * 2] = strings.get(i);
                buttonFormRelease[i * 2 + 1] = strings.get(i);
            }
        }
        messageHandler.updateInlineKeyboardMessage("Выберите форму выпуска лекарства", KeyboardButton.createInlineKeyboard(buttonFormRelease), context);
    }

    public void sendThisReleaseForm(String name, ChatContext context) {
        List<String> strings = null;
        try {
            strings = findMedicines.getMedicinesThisReleaseForm(name,true);
//            if (Dictionaries.releaseForm.containsKey(name)) {
//                strings = findMedicines.getMedicinesThisReleaseForm(name, true);
//            } else {
//                strings = findMedicines.getMedicinesThisReleaseForm(name, false);
//            }
        } catch (ElementNotFoundException e) {
            sendThenDeleteMessage("Лекарство не найдено", context, 60);
            messageHandler.updateInlineKeyboardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory, context);
        }
        String[] buttonFullFormRelease = new String[strings.size() * 2];
        for (int i = 0; i < strings.size(); i++) {
            buttonFullFormRelease[i * 2] = strings.get(i);
            buttonFullFormRelease[i * 2 + 1] = String.valueOf(i + 1);
        }

        messageHandler.updateInlineKeyboardMessage("Выберите форму выпуска лекарства", KeyboardButton.createInlineKeyboard(buttonFullFormRelease), context);
    }

    public void sendSelectScreenShotMedicines(String num, ChatContext context) {
        InputFile medicines = null;
        try {
            medicines = findMedicines.getSelectMedicines(num);
        } catch (Exception e) {
            e.printStackTrace();
            sendThenDeleteMessage("Лекарство не найдено", context, 60);
            messageHandler.updateInlineKeyboardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory, context);
        }

        // Название лекарства перед фото
        sendThenDeleteMessage(findMedicines.getSelectMedicineName(), context, 3600);

        //Отправка фото
        SendPhoto sendPhoto = new SendPhoto(context.getChatId(), medicines);
        try {
            int id = botEngine.execute(sendPhoto).getMessageId();
            messageHandler.deleteMessageAfterDelay(3600, id, context);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        messageHandler.updateInlineKeyboardMessage("Выберите форму выпуска лекарства", KeyboardButton.InlineKeyboardChooseCategory, context);
    }


    /**
     * Отправляет сообщение пользователю с последующим удалением, спустя указанное время.
     *
     * @param messageText    Текст сообщения.
     * @param context        Контекст чата.
     * @param delayInSeconds Задержка в секундах.
     */
    private void sendThenDeleteMessage(String messageText, ChatContext context, int delayInSeconds) {
        try {
            int messageId = botEngine.execute(new SendMessage(context.getChatId(), messageText)).getMessageId();
            if (messageId != 0) messageHandler.deleteMessageAfterDelay(delayInSeconds, messageId, context);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send exception message(" + messageText + ")", e);
        }
    }
}

