package bot.handlers;

import bot.ChatContext;
import bot.service.Dictionaries;
import bot.service.KeyboardButton;
import bot.service.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Обрабатывает действия, связанные с обратным вызовом(callback), в чате.
 */
public class CallbackHandler {
    // Map для хранения обработчиков обратного вызова
    public final Map<String, BiConsumer<String, ChatContext>> cityWeatherHandlers = new HashMap<>();
    public final Map<String, BiConsumer<String, ChatContext>> medicinesReleaseFormHandlers = new HashMap<>();
    public final Map<String, BiConsumer<String, ChatContext>> selectMedicinesHandlers = new HashMap<>();
    public final Map<String, BiConsumer<String, ChatContext>> routeHandlers = new HashMap<>();
    public final Map<String, Consumer<ChatContext>> arrivalStationsHandlers = new HashMap<>();
    public final Map<String, Consumer<ChatContext>> buttonHandlers = new HashMap<>();

    /**
     * Конструктор CallbackHandler.
     *
     * @param messageHandler  Экземпляр MessageHandler для обработки сообщений.
     * @param weatherHandler  Экземпляр WeatherHandler для обработки погоды.
     * @param routeHandler    Экземпляр RouteHandler для обработки маршрута.
     * @param scheduleHandler Экземпляр ScheduleHandler для обработки расписания.
     */
    public CallbackHandler(MessageHandler messageHandler, WeatherHandler weatherHandler, RouteHandler routeHandler, ScheduleHandler scheduleHandler, MedicinesHandler medicinesHandler) {
        initCityWeatherHandlers(weatherHandler);
        initRouteHandlers(routeHandler);
        initArrivalStationsHandlers(messageHandler);
        initDepartureStationsHandlers(scheduleHandler);
        initButtonHandlers(messageHandler, scheduleHandler);
        initMedicinesReleaseForm(medicinesHandler);
        initMedicines(medicinesHandler);
    }

    /**
     * Инициализирует WeatherHandlers по городам.
     *
     * @param weatherHandler Экземпляр WeatherHandler для обработки отдельных городов.
     */
    private void initCityWeatherHandlers(WeatherHandler weatherHandler) {
        cityWeatherHandlers.putAll(Map.of(
                "WeatherSamara", weatherHandler::sendWeatherInfo,
                "WeatherNovokybishevsk", weatherHandler::sendWeatherInfo
        ));
    }

    private void initMedicinesReleaseForm(MedicinesHandler medicinesHandler) {
        medicinesReleaseFormHandlers.put("Аэрозоль",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Гель",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Гранулы",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Драже",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Капли",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Капсулы",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Крем",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Лосьон",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Мазь",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Порошок",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Раствор",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Сироп",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Спрей",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Суппозитории",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Суспензии",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Таблетки",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Шампунь",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Эликсир",medicinesHandler::sendThisReleaseForm);
        medicinesReleaseFormHandlers.put("Эмульсия",medicinesHandler::sendThisReleaseForm);
    }

    private void initMedicines(MedicinesHandler medicinesHandler) {
        selectMedicinesHandlers.put("1",medicinesHandler::sendSelectScreenShotMedicines);
        selectMedicinesHandlers.put("2",medicinesHandler::sendSelectScreenShotMedicines);
        selectMedicinesHandlers.put("3",medicinesHandler::sendSelectScreenShotMedicines);
        selectMedicinesHandlers.put("4",medicinesHandler::sendSelectScreenShotMedicines);
        selectMedicinesHandlers.put("5",medicinesHandler::sendSelectScreenShotMedicines);
        selectMedicinesHandlers.put("6",medicinesHandler::sendSelectScreenShotMedicines);
        selectMedicinesHandlers.put("7",medicinesHandler::sendSelectScreenShotMedicines);
        selectMedicinesHandlers.put("8",medicinesHandler::sendSelectScreenShotMedicines);
        selectMedicinesHandlers.put("9",medicinesHandler::sendSelectScreenShotMedicines);
        selectMedicinesHandlers.put("10",medicinesHandler::sendSelectScreenShotMedicines);

    }

    /**
     * Инициализирует обработчики маршрутов.
     *
     * @param routeHandler Экземпляр RouteHandler для обработки маршрутов.
     */
    private void initRouteHandlers(RouteHandler routeHandler) {
        routeHandlers.putAll(Map.of(
                "Telecentre->Railway", routeHandler::sendTransportArrivalInfo,
                "Railway->Telecentre", routeHandler::sendTransportArrivalInfo
        ));
    }

    /**
     * Инициализирует обработчики прибытия.
     *
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений.
     */
    private void initArrivalStationsHandlers(MessageHandler messageHandler) {
        String arrivalMessage = "Cтанция прибытия";
        String[] arrivalStations = {"FromSamara", "FromMirnaya", "FromLipyagi", "FromSrednevolzhskaya", "FromSokskaya", "FromPohvistnevo"};
        for (String station : arrivalStations) {
            arrivalStationsHandlers.put(station, context -> messageHandler.updateInlineKeyboardMessage(arrivalMessage, KeyboardButton.createInlineKeyboardForDeparture(station), context));
        }
    }

    /**
     * Инициализирует обработчики прибытия.
     *
     * @param scheduleHandler Экземпляр MessageHandler для обработки расписания.
     */
    private void initDepartureStationsHandlers(ScheduleHandler scheduleHandler) {
        String[] departureStations = {"ToSamara", "ToMirnaya", "ToLipyagi", "ToSrednevolzhskaya", "ToSokskaya", "ToPohvistnevo"};
        for (String station : departureStations) {
            buttonHandlers.put(station, context -> scheduleHandler.sendTrainSchedule(Singleton.getInstance().getProperties().getName_from(), Dictionaries.departureStationMap.get(station), context));
        }
    }

    /**
     * Инициализирует обработчики кнопок.
     *
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений.
     */
    private void initButtonHandlers(MessageHandler messageHandler, ScheduleHandler scheduleHandler) {
        buttonHandlers.putAll(Map.of(
                "WeatherApp", context -> messageHandler.updateInlineKeyboardMessage("Выберите город", KeyboardButton.InlineKeyboardChooseCity, context),
                "Routes", context -> messageHandler.updateInlineKeyboardMessage("Выберите маршрут", KeyboardButton.InlineKeyboardChooseRoutes, context),
                "TabletimeFrom", context -> messageHandler.updateInlineKeyboardMessage("Cтанция отправления", KeyboardButton.InlineKeyboardChooseDeparture, context),
                "FindMedicines", context -> messageHandler.sendMessage("Введите название лекарства", context)
        ));
    }
}
