package pages.samara;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;
import exceptions.ElementNotFoundException;

/**
 * Класс ArrivingTransportPage предоставляет функциональные возможности для взаимодействия с элементами
 * связанные с транспортными данными, представленными на странице.
 */
public class ArrivingTransportPage {
    final WebDriver driver;// Экземпляр WebDriver для взаимодействия со страницей
    private final int AMOUNT_TRANSPORT = 5;// Максимальное кол-во отображаемого транспорта

    /**
     * Конструктор для ArrivingTransportPage.
     * Инициализирует WebDriver для страницы.
     *
     * @param driver экземпляр WebDriver.
     */
    public ArrivingTransportPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Извлекает время до прибытия транспорта из указанного элемента.
     *
     * @param i индекс транспортного маршрута на странице.
     * @return Строка, содержащая время(в минутах).
     * @throws ElementNotFoundException, если указанный элемент не найден.
     */
    public String getTimeText(int i) throws ElementNotFoundException {
        String minutesBeforeArrival;
        try {
            minutesBeforeArrival = driver.findElement(By.xpath("(//tr[contains(@class,'trans-')]/td[@class='trans-num'])[" + i + "]")).getText();
        } catch (WebDriverException e) {
            throw new ElementNotFoundException();
        }
        return minutesBeforeArrival;
    }

    /**
     * Извлекает тип транспорта из указанного элемента.
     *
     * @param i Индекс транспортного маршрута на странице.
     * @return Строка, содержащая тип транспорта(trans-trol или trans-bus).
     * @throws ElementNotFoundException, если указанный элемент не найден.
     */
    public String getTypeText(int i) throws ElementNotFoundException {
        String typeTransport;
        try {
            typeTransport = driver.findElement(By.xpath("(//tr[contains(@class,'trans-')])[" + i + "]")).getAttribute("class");
        } catch (WebDriverException e) {
            throw new ElementNotFoundException();
        }
        return typeTransport.substring(0, typeTransport.indexOf(" "));
    }

    /**
     * Извлекает номер транспортного маршрута из указанного элемента.
     *
     * @param i Индекс транспортного маршрута на странице.
     * @return Строка, содержащая номер транспортного маршрута.
     * @throws ElementNotFoundException, если указанный элемент не найден.
     */
    public String getNumberRouteText(int i) throws ElementNotFoundException {
        String nameRoute;
        try {
            nameRoute = driver.findElement(By.xpath("(//tr[contains(@class,'trans-')]/td[@class='trans-name'])[" + i + "]")).getText();
        } catch (WebDriverException e) {
            throw new ElementNotFoundException();
        }
        return nameRoute.substring(0, nameRoute.indexOf(":"));
    }

    /**
     * Рассчитывает количество транспортных маршрутов, отображаемых на странице.
     *
     * @return Минимум из общего количества транспортных маршрутов и 5
     * @throws ElementNotFoundException, если транспортные маршруты не найдены.
     */
    public int getAmount() throws ElementNotFoundException {
        int amountTransport;
        try {
            amountTransport = driver.findElements(By.xpath("//tr[contains(@class,'trans-')]")).size();
            if (amountTransport == 0) throw new ElementNotFoundException();
        } catch (WebDriverException e) {
            throw new ElementNotFoundException();
        }
        return Math.min(amountTransport, AMOUNT_TRANSPORT);
    }
}
