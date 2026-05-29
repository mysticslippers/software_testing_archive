package me.ifmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OrganizationCardPage extends BasePage {

    public OrganizationCardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCardOpened() {
        boolean isOpened;
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Маршрут')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Телефон')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Сайт')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Режим работы')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'График работы')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Написать отзыв')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'оценок')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'отзыв')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Поделиться')]"))
            ));
            isOpened = true;
        } catch (Exception exception) {
            System.out.println("Карточка организации не открылась!");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            isOpened = false;
        }
        return isOpened;
    }

    public boolean hasText(String text) {
        boolean hasText;
        By textLocator = By.xpath("//*[contains(normalize-space(.), '" + text + "')]");
        try {
            hasText = wait.until(ExpectedConditions.presenceOfElementLocated(textLocator)).isDisplayed();
        } catch (Exception exception) {
            System.out.println("Текст в карточке не найден: " + text);
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            hasText = false;
        }
        return hasText;
    }

    public void clickRouteButton() {
        By buttonRoute = By.xpath(
                "//*[contains(text(), 'Маршрут') " +
                        "or contains(text(), 'Проехать') " +
                        "or contains(text(), 'Как добраться')]"
        );

        wait.until(ExpectedConditions.elementToBeClickable(buttonRoute)).click();
    }

    @Override
    public boolean isPageLoaded() {
        return isCardOpened();
    }
}