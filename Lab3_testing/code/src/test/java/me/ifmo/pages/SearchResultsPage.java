package me.ifmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SearchResultsPage extends BasePage {
    private final By searchInput = By.xpath("//input[not(@type='hidden') and not(@disabled)]");
    private final By resultsPanel = By.xpath(
            "//*[contains(text(), 'Фильтры') " +
                    "or contains(text(), 'Места') " +
                    "or contains(text(), 'Результаты поиска') " +
                    "or contains(text(), 'Найдено') " +
                    "or contains(text(), 'Показать')]"
    );

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isSearchResultsOpened() {
        boolean isOpened;
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/search/"),
                    ExpectedConditions.presenceOfElementLocated(resultsPanel)
            ));
            isOpened = true;
        } catch (Exception exception) {
            System.out.println("Результаты поиска не открылись!");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            isOpened = false;
        }
        return isOpened;
    }

    public boolean isSearchInputContains(String query) {
        boolean isContained;
        try {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
            String value = input.getAttribute("value");
            System.out.println("Search input value: " + value);
            isContained = value != null && value.toLowerCase().contains(query.toLowerCase());
        } catch (Exception exception) {
            System.out.println("Не удалось проверить значение поисковой строки: " + query);
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            isContained = false;
        }
        return isContained;
    }

    public boolean hasResultsPanel() {
        boolean hasResults;
        try {
            hasResults = wait.until(ExpectedConditions.presenceOfElementLocated(resultsPanel)).isDisplayed();
        } catch (Exception exception) {
            System.out.println("Панель результатов поиска не найдена!");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            hasResults = false;
        }
        return hasResults;
    }

    public void openFirstResult() {
        By firstResult = By.xpath(
                "(" +
                        "//*[contains(text(), 'оценок')]/ancestor::div[1]" +
                        " | " +
                        "//*[contains(text(), 'оценка')]/ancestor::div[1]" +
                        " | " +
                        "//*[contains(text(), 'отзыв')]/ancestor::div[1]" +
                        " | " +
                        "//*[contains(text(), 'Санкт-Петербург')]/ancestor::div[1]" +
                        ")[1]"
        );

        WebElement result = wait.until(ExpectedConditions.elementToBeClickable(firstResult));
        result.click();
    }

    @Override
    public boolean isPageLoaded() {
        return isSearchResultsOpened();
    }
}
