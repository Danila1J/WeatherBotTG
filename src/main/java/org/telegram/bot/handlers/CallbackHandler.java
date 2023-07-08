package org.telegram.bot.handlers;

import org.telegram.bot.service.files.KeyboardButton;
import org.telegram.bot.service.files.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CallbackHandler {
    public final Map<String, Consumer<String>> cityWeather = new HashMap<>();
    public final Map<String, Consumer<String>> routes = new HashMap<>();
    public final Map<String, Runnable> toStation = new HashMap<>();
    public final Map<String, Runnable> buttons = new HashMap<>();

    public CallbackHandler(MessageHandler messageHandler, WeatherHandler weatherHandler, RouteHandler routeHandler, ScheduleHandler scheduleHandler) {
        initCallbackHandlersCityWeather(weatherHandler);
        initCallbackHandlersRoutes(routeHandler);
        initCallbackHandlersToStation(messageHandler);
        initCallbackHandlers(messageHandler, scheduleHandler);
    }

    private void initCallbackHandlersCityWeather(WeatherHandler weatherHandler) {
        cityWeather.putAll(Map.of(
                "WeatherSamara", weatherHandler::sendWeatherMessage,
                "WeatherNovokybishevsk", weatherHandler::sendWeatherMessage
        ));
    }
    private void initCallbackHandlersRoutes(RouteHandler routeHandler) {
        routes.putAll(Map.of(
                "Telecentre->Railway", routeHandler::sendRoute,
                "Railway->Telecentre", routeHandler::sendRoute
        ));
    }
    private void initCallbackHandlersToStation(MessageHandler messageHandler) {
        toStation.putAll(Map.of(
                "FromSamara", () -> messageHandler.editInlineKeyBoardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalSamara),
                "FromMirnaya", () -> messageHandler.editInlineKeyBoardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalMirnaya),
                "FromLipyagi", () -> messageHandler.editInlineKeyBoardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalLipyagi),
                "FromSrednevolzhskaya", () -> messageHandler.editInlineKeyBoardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalSrednevolzhskaya)
        ));
    }
    private void initCallbackHandlers(MessageHandler messageHandler, ScheduleHandler scheduleHandler) {
        buttons.putAll(Map.of(
                "WeatherApp", () -> messageHandler.editInlineKeyBoardMessage("Выберите город", KeyboardButton.InlineKeyboardChooseCity),
                "Routes", () -> messageHandler.editInlineKeyBoardMessage("Выберите маршрут", KeyboardButton.InlineKeyboardChooseRoutes),
                "TabletimeFrom", () -> messageHandler.editInlineKeyBoardMessage("Cтанция отправления", KeyboardButton.InlineKeyboardChooseDeparture),
                "ToMirnaya", () -> scheduleHandler.sendRasp(Singleton.getInstance().getProperties().getName_from(), "Мирная"),
                "ToSamara", () -> scheduleHandler.sendRasp(Singleton.getInstance().getProperties().getName_from(), "Самара"),
                "ToLipyagi", () -> scheduleHandler.sendRasp(Singleton.getInstance().getProperties().getName_from(), "Липяги"),
                "ToSrednevolzhskaya", () -> scheduleHandler.sendRasp(Singleton.getInstance().getProperties().getName_from(), "Средневолжская")
        ));
    }
}
