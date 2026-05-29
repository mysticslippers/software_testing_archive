package me.ifmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RoutePage extends BasePage {

    public RoutePage(WebDriver driver) {
        super(driver);
    }

    public boolean isRoutePanelOpened() {
        boolean isOpened;
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Откуда')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Куда')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Маршруты')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'На машине')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Пешком')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Общественный транспорт')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Такси')]"))
            ));
            isOpened = true;
        } catch (Exception exception) {
            System.out.println("Панель маршрута не открылась!");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            isOpened = false;
        }
        return isOpened;
    }

    @Override
    public boolean isPageLoaded() {
        return isRoutePanelOpened();
    }
}