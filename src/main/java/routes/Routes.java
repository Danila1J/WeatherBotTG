package routes;

import exceptions.ElementNotFoundException;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.AbstractPage;
import pages.MainPage;
import pages.samara.ArrivingTransportPage;

import java.time.Duration;
import java.util.*;

/**
 * Класс Routes хранит информацию о маршрутах и обеспечивает методы для их получения.
 */
public class Routes {

    private static final Map<Character, String> ROUTE_URLS =
            Map.of(
                    'T', System.getProperty("Telecentre->Railway.url"),
                    'R', System.getProperty("Railway->Telecentre.url")
            );

    /**
     * Устанавливает драйвер для AbstractPage, и время ожидания для поиска элементов.
     *
     * @param driver драйвер ChromeDriver.
     */
    private void setupChromeDriver(ChromeDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Установим время ожидания для поиска элементов
        AbstractPage.setDriver(driver); // Установить созданный драйвер для поиска в веб-страницах
    }

    /**
     * Возвращает URL на основе первого символа в названии направления.
     *
     * @param routeName название маршрута.
     * @return URL для данного маршрута.
     */
    private String fetchUrlForRoute(String routeName) {
        char firstCharacter = routeName.charAt(0);
        return ROUTE_URLS.getOrDefault(firstCharacter, "");
    }

    /**
     * Устанавливает драйвер и извлекает информацию о маршруте из URL-адреса.
     *
     * @param url    URL маршрута
     * @param driver экземпляр ChromeDriver
     * @return Массив объектов StringBuilder, содержащих сведения о маршруте
     * @throws ElementNotFoundException
     */
    private StringBuilder[] gatherRouteDetails(String url, ChromeDriver driver) throws ElementNotFoundException {
        setupChromeDriver(driver);
        driver.get(url);

        MainPage mainPage = new MainPage();
        ArrivingTransportPage arrivingTransportPage = mainPage.gotoArrivingTransports();

        return fetchRoutesInfo(arrivingTransportPage.getAmount(), arrivingTransportPage);
    }

    /**
     * Извлекает детали маршрута для заданного имени.
     *
     * @param routeName имя маршрута
     * @param driver    экземпляр ChromeDriver
     * @return массив объектов StringBuilder, содержащих информацию о маршрутах
     * @throws ElementNotFoundException
     */
    public StringBuilder[] getRouteDetails(String routeName, ChromeDriver driver) throws ElementNotFoundException {
        String url = fetchUrlForRoute(routeName);
        return url.isEmpty() ? null : gatherRouteDetails(url, driver);
    }

    /**
     * Получает сведения о маршруте со страницы ArrivingTransportPage.
     *
     * @param amount                маршрутов для выборки
     * @param arrivingTransportPage — экземпляр ArrivingTransportPage
     * @return массив объектов StringBuilder с собранными маршрутами
     * @throws ElementNotFoundException
     */
    private StringBuilder[] fetchRoutesInfo(int amount, ArrivingTransportPage arrivingTransportPage) throws ElementNotFoundException {
        StringBuilder[] routes = new StringBuilder[amount];
        Set<String> numbersToSkip = new HashSet<>(Arrays.asList("19", "23", "47", "170"));

        int j = 0;
        for (int i = 1; j < amount; i++) {
            String nameRoute = arrivingTransportPage.getNumberRouteText(i);
            if (numbersToSkip.contains(nameRoute)) continue;
            routes[j] = new StringBuilder();

            String type = arrivingTransportPage.getTypeText(i);

            if (type.startsWith("trans-trol")) {
                routes[j].append("Т\uD83D\uDE8E ").append(nameRoute);
            } else if (type.startsWith("trans-bus")) {
                routes[j].append("А\uD83D\uDE8C ").append(nameRoute);
            }

            String time = arrivingTransportPage.getTimeText(i);
            routes[j].append(" (").append(time).append(declineMinutes(Integer.parseInt(time))).append(")").append("\n\n");
            j++;
        }
        return routes;
    }


    /**
     * Склоняет слово «минут».
     *
     * @param minutes количество минут
     * @return склоненную форму слова «минуты»
     */
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