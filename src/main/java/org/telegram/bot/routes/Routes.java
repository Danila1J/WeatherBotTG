package org.telegram.bot.routes;

import org.openqa.selenium.chrome.ChromeDriver;
import pages.AbstractPage;
import pages.MainPage;
import pages.to.samara.ArrivingTransportPage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Routes {

    private static final Map<Character, String> routeUrls = new HashMap<>();

    static {
        setupRouteUrls();
    }

    // Создаем map для хранения URL веб-сайтов для каждой остановки
    private static void setupRouteUrls() {
        routeUrls.putAll(Map.of(
                'T', System.getProperty("Telecentre->Railway.url"),
                'R', System.getProperty("Railway->Telecentre.url")
        ));
    }

    private void setup(ChromeDriver driver) {
        //ChromeDriver driver = new ChromeDriver(); // Создание экземпляра драйвера
        driver.manage().window().maximize(); // Устанавливаем размер окна браузера, как максимально возможный
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // Установим время ожидания для поиска элементов
        AbstractPage.setDriver(driver); // Установить созданный драйвер для поиска в веб-страницах
    }

    // Метод получения URL на основе первого символа в названии направления
    private String getUrlByRouteName(String routeName) {
        char firstCharacter = routeName.charAt(0);
        return routeUrls.getOrDefault(firstCharacter, "");
    }

    // Метод для настройки и использования драйвера
    private StringBuilder[] useDriver(String url,ChromeDriver driver) {
        setup(driver);
        driver.get(url);

        MainPage mainPage = new MainPage();
        ArrivingTransportPage arrivingTransportPage = mainPage.mainMenuTransport();

        StringBuilder[] temp = getRoutes(5, arrivingTransportPage);

        //TODO: сделать общий драйвер для погоды и маршрутов

        //В классе BotEngine создается класс Route для повторного переиспользования,
        //Скоре всего нужно создать доп класс для менеджмента страниц
        //Из-за этого появляется проблема при одновременном открытии маршрутов и погоды
        //После открытия погоды маршруты перестают обновляться и приходит сообщение об ошибке
        //А именно "На выбранном маршруте отсутствует транспорт"

        //Создается отдельный поток, который закроет драйвер через 10 минут
        new Thread(() -> {
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //driver.quit();
        }).start();

        return temp;
    }

    public StringBuilder[] getRouteDetails(String routeName,ChromeDriver driver) {
        String url = getUrlByRouteName(routeName);
        return url.isEmpty() ? null : useDriver(url,driver);
    }

    //Получение маршрутов
    private StringBuilder[] getRoutes(int amount, ArrivingTransportPage arrivingTransportPage) {
        StringBuilder[] answer = new StringBuilder[amount];
        int j = 0;
        for (int i = 0; j < amount; i++) {
            String name = arrivingTransportPage.getNameText(i + 1);
            if (name.equals("19") || name.equals("23") || name.equals("47") || name.equals("170")) {
                continue;
            }
            answer[j] = new StringBuilder();
            switch (arrivingTransportPage.getTypeText(i + 1)) {
                case "trans-trol " -> answer[j].append("Т\uD83D\uDE8E ").append(name);
                case "trans-bus " -> answer[j].append("А\uD83D\uDE8C ").append(name);
            }
            String time = arrivingTransportPage.getTimeText(i + 1);
            answer[j].append(" (").append(time).append(declineMinutes(Integer.parseInt(time))).append(")").append("\n\n");
            j++;
        }
        return answer;
    }

    //Склонение слова 'минут'
    private static String declineMinutes(int minutes) {
        int lastDigit = minutes % 10;
        int preLastDigit = minutes % 100 / 10;

        if (preLastDigit == 1) {
            return " минут";
        }

        return switch (lastDigit) {
            case 1 -> " минута";
            case 2, 3, 4 -> " минуты";
            default -> " минут";
        };
    }
}