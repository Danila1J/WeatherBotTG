package routes;

import exceptions.ElementNotFoundException;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.AbstractPage;
import pages.MainPage;
import pages.samara.ArrivingTransportPage;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс Routes хранит информацию о маршрутах и обеспечивает методы для их получения.
 */
public class Routes {

    private static final Map<Character, String> ROUTE_URLS = new HashMap<>();
    private final int AMOUNT_TRANSPORT = 5;


    static {
        setupRouteUrls();
    }

    /**
     * Заполняет map URL-ами страниц для каждой остановки.
     */
    private static void setupRouteUrls() {
        ROUTE_URLS.putAll(Map.of(
                'T', System.getProperty("Telecentre->Railway.url"),
                'R', System.getProperty("Railway->Telecentre.url")
        ));
    }

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

        StringBuilder[] routesInfo = fetchRoutesInfo(AMOUNT_TRANSPORT, arrivingTransportPage);

        // Инициирование нового потока для закрытия драйвера через 10 минут
        new Thread(() -> {
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }).start();

        return routesInfo;
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