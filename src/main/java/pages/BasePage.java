package pages;

import pages.samara.ArrivingTransportPage;
import pages.yandex.weather.YandexWeatherPage;

// Элементы общие для системы
public class BasePage extends AbstractPage {

    // Доступ к элементам главного меню
    public YandexWeatherPage mainMenuWeather() {
        return new YandexWeatherPage(driver);
    }

    public ArrivingTransportPage gotoArrivingTransports() {
        return new ArrivingTransportPage(driver);
    }
}
