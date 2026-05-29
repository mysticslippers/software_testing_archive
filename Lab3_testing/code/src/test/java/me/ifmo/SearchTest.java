package me.ifmo;

import me.ifmo.pages.HomePage;
import me.ifmo.pages.SearchResultsPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest extends BaseTest{

    @Test
    public void testUserCanSearchOrganizationByName() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.search("ИТМО");

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.isSearchResultsOpened(), "Результаты поиска по запросу 'ИТМО' не открылись!");
        assertTrue(searchResultsPage.isSearchInputContains("ИТМО"), "В поисковой строке не отображается запрос 'ИТМО'!");
        assertTrue(searchResultsPage.hasResultsPanel(), "Панель результатов поиска не отображается!");
    }

    @Test
    public void testUserCanSearchCategory() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.search("кафе");

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.isSearchResultsOpened(), "Результаты поиска по категории 'кафе' не открылись!");
        assertTrue(searchResultsPage.isSearchInputContains("кафе"), "В поисковой строке не отображается запрос 'кафе'!");
        assertTrue(searchResultsPage.hasResultsPanel(), "Панель результатов поиска не отображается!");
    }

    @Test
    public void testUserCanSearchAddress() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.search("Невский проспект");

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.isSearchResultsOpened(), "Результаты поиска по адресу 'Невский проспект' не открылись!");
        assertTrue(searchResultsPage.isSearchInputContains("Невский проспект"), "В поисковой строке не отображается запрос 'Невский проспект'!");
        assertTrue(searchResultsPage.hasResultsPanel(), "Панель результатов поиска не отображается!");
    }
}
