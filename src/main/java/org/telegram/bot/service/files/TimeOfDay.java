package org.telegram.bot.service.files;

import java.time.LocalTime;

public class TimeOfDay {
    public static String timeOfDay(String userName) {
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
