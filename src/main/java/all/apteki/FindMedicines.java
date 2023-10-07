package all.apteki;

import bot.service.Dictionaries;
import exceptions.ElementNotFoundException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import pages.all.apteki.FindMedicinesPage;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//TODO Изменить документацию

/**
 * Класс FindMedicines обеспечивает методы для получения информации о лекарствах.
 */

public class FindMedicines {
    private static final String URL = System.getProperty("allApteki.url");

    private int amountReleaseForm = 0;
    private List<String> listReleaseForm;
    boolean b = true;
    ChromeDriver chromeDriver;
    FindMedicinesPage findMedicinesPage;

    public FindMedicines(ChromeDriver driver) {
        chromeDriver = driver;
        findMedicinesPage = new FindMedicinesPage(driver);
    }

    /**
     * Устанавливает драйвер для AbstractPage, и время ожидания для поиска элементов.
     *
     * @param driver драйвер ChromeDriver.
     */
    private void setupChromeDriver(ChromeDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Установим время ожидания для поиска элементов
        //AbstractPage.setDriver(driver); // Установить созданный драйвер для поиска в веб-страницах
    }

    /**
     * Получает список форм выпуска лекарства
     */
    private List<String> getReleaseForm() throws ElementNotFoundException {
        int amountRecords = findMedicinesPage.getAmountRecord();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= amountRecords; i++) {
            String form =findMedicinesPage.getRealeaseFormFromNameMedicine(i);
            if(Dictionaries.releaseForm.containsKey(form)) list.add(form);
        }
        List<String> listWithoutDuplicates = list.stream().distinct().toList();
        amountReleaseForm = listWithoutDuplicates.size();
        listReleaseForm = listWithoutDuplicates;
        return listWithoutDuplicates;
    }

    //Получает форму с названием
    private List<String> getFullReleaseForm() throws ElementNotFoundException {
        int amountRecords = findMedicinesPage.getAmountRecord();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= amountRecords; i++) {
            list.add(findMedicinesPage.getFullRealeaseForm(i));
        }
        return list;
    }

    /**
     * Устанавливает драйвер и извлекает информацию о лекарствах из URL-адреса.
     *
     * @param name название лекарства
     * @return Список объектов String, содержащих сведения о форме выпускаемых лекарств
     */
    public List<String> getMedicinesDetails(String name) throws ElementNotFoundException {
        chromeDriver.get(URL);
        chromeDriver.manage().window().fullscreen();


        if (b) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            findMedicinesPage.closeAds();// закрыть
            b = false;
        }

        findMedicinesPage.pressCheckBoxNextToMe(); // Нажать кнопку искать "Рядом со мной"
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        findMedicinesPage.enterNameMedicine(name); // Ввод названия лекарства

        findMedicinesPage.pressButtonFind(); // Нажать кнопку "Найти"

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (findMedicinesPage.getAmountRecord() >= 25)
            findMedicinesPage.setToDisplay50Entries(); // Отображать 50 записей

        return getReleaseForm();
    }

    public List<String> getMedicinesThisReleaseForm(String name, boolean... b) throws ElementNotFoundException {
        if (b[0]) {
            findMedicinesPage.choiceReleaseForm(name);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //сортировка по количеству предложений
        findMedicinesPage.sortByNumberOfOffers();

        return getFullReleaseForm();
    }

    public InputFile getSelectMedicines(String num) throws IOException {
        // Клик на выбранное лекарство
        findMedicinesPage.clickOnSelectMedicines(Integer.parseInt(num));
        return new InputFile().setMedia(findMedicinesPage.getScreenshot());
    }

    public String getSelectMedicineName() {
        return findMedicinesPage.getSelectMedicineName();
    }
}
