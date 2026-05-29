package me.ifmo;

import me.ifmo.pages.HomePage;
import me.ifmo.pages.OrganizationCardPage;
import me.ifmo.pages.SearchResultsPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrganizationCardTest extends BaseTest{

    @Test
    public void testUserCanOpenOrganizationCardFromSearchResults() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.search("ИТМО");

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.isSearchResultsOpened(), "Результаты поиска по запросу 'кафе' не открылись!");
        searchResultsPage.openFirstResult();

        OrganizationCardPage organizationCardPage = new OrganizationCardPage(driver);
        assertTrue(organizationCardPage.isCardOpened(), "Карточка организации не открылась!");
    }

    @Test
    public void testOrganizationCardContainsRouteAction() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.search("кафе");

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.isSearchResultsOpened(), "Результаты поиска по запросу 'кафе' не открылись!");
        searchResultsPage.openFirstResult();

        OrganizationCardPage organizationCardPage = new OrganizationCardPage(driver);
        assertTrue(organizationCardPage.hasText("Маршрут"), "Карточка организации не содержит действие 'Маршрут'!");
    }
}
