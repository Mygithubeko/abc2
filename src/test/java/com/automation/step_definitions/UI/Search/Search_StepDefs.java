package com.automation.step_definitions.UI.Search;

//https://gitlab.com/cerme/wentsy/wentsy-pm/-/wikis/Service-Search-Criteria

import com.automation.pages.ServiceProviderPanelPage.SP_ServiceandPackageDetailsNavigationsPage;
import com.automation.pages.RegisterandLoginPages.UserandSPLoginandRegistrationPage;
import com.automation.pages.ServiceProviderPanelPage.SP_MyServicesPage;
import com.automation.pages.ServiceProviderPanelPage.SP_ServicePackagesPage;
import com.automation.pages.WentsyHomePage.HomePage;
import com.automation.pages.WentsyHomePage.SearchPage;
import com.automation.pages.WentsyHomePage.ServiceDetailedPage;
import com.automation.step_definitions.DB.SearchStepDefsDB;
import com.automation.utilities.BrowserUtils;
import com.automation.utilities.Driver;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.*;

@Slf4j
public class Search_StepDefs {

    SearchPage searchPage = new SearchPage();
    HomePage homePage = new HomePage();
    ServiceDetailedPage serviceDetailedPage = new ServiceDetailedPage();
    UserandSPLoginandRegistrationPage userandSPLoginandRegistrationPage = new UserandSPLoginandRegistrationPage();
    SP_ServicePackagesPage sp_servicePackagesPage = new SP_ServicePackagesPage();
    SP_MyServicesPage sp_myServicesPage = new SP_MyServicesPage();
    SP_ServiceandPackageDetailsNavigationsPage SPServiceandPackageDetailsNavigationsPage = new SP_ServiceandPackageDetailsNavigationsPage();

    public static String randomService;

    @When("user types {string} keyword into What are you looking for? inputbox")
    public void user_types_keyword_into_What_are_you_looking_for_inputbox(String keyword) {
        log.info("user types a keyword in search box");
        searchPage.whatAreYouLookingForSearchInbutBox.sendKeys(keyword);
        //add keyword to the list for further verification
        searchPage.searchedKeywordandLocation(keyword);
        BrowserUtils.waitFor(2);
    }

    @When("user enters {string} location keyword into In which city? inputbox")
    public void user_enters_location_keyword_into_In_which_city_inputbox(String location) {
        log.info("user types a location in search box");
        searchPage.inWhichCitySearchInbutBox.sendKeys(location+" ");
        //select first element from dropdown
        Driver.get().findElements(By.cssSelector(".pac-item")).get(0).click();

        //add location to the list for further verification
        //searchPage.searchedKeywordandLocation(location);
        BrowserUtils.waitFor(3);
    }

    //hidden by developers upon the request of PO
    @When("user chooses any date interval from Date Picker")
    public void user_chooses_any_date_interval_from_Date_Picker() {

        //https://stackoverflow.com/questions/44000098/automate-bootstrap-datepicker-using-selenium-webdriver

        /*
        // we will select first 6 month from now on
        // select datePicker
        searchPage.dateRangePickerSearchInbutBox.click();
        //select tomorrow as the start date
        boolean flag = true;
        for (int i = 0; i < searchPage.dayDatePicker.size(); i++) {
            if (searchPage.dayDatePicker.get(i).isEnabled() && flag) {
               searchPage.dayDatePicker.get(i).click();
              flag = false;
            }
        }
        flag=true;

        //select six months later
        for (int i = 0; i < 5; i++) {
            searchPage.nextMonthDatePickerArrow.click();
        }
        BrowserUtils.waitFor(2);
        //select first available day of the sixth month
        for (int i = 1; i <31 ; i++) {
            String xpathEndDay= "//span[text()='"+i+"' and @class='in-range ng-star-inserted']";
            if(Driver.get().findElement(By.xpath(xpathEndDay)).isDisplayed() && flag){
                System.out.println("flag = " + flag);
                System.out.println("Driver.get().findElement(By.xpath(xpathDay)).getText() = " + Driver.get().findElement(By.xpath(xpathEndDay)).getText());
                Driver.get().findElement(By.xpath(xpathEndDay)).click();
                flag =false;
            }
        }
        flag=true;
         */
    }
    @When("user clicks search button on homepage")
    public void user_clicks_search_button_on_homepage() {
        log.info("user clicks search button");
        searchPage.searchButton.click();
        BrowserUtils.waitFor(1);

    }

    @Then("user navigates to search results page for searched {string} location")
    public void user_navigates_to_search_results_page_for_searched_location(String location) {
        log.info("user navigates to search results page for searched {string} location");
        BrowserUtils.waitFor(13);
        BrowserUtils.switchToWindow(homePage.getSearchResultsPageTitle(location));
        // verify that number of search results appear
        BrowserUtils.verifyElementDisplayed(searchPage.NumberofSearchResults);
    }

    @Then("user sees searched {string} keyword and {string} location in the search panel")
    public void user_sees_searched_keyword_and_location_in_the_search_panel(String keyword, String location) {
        log.info("user sees searched keyword and location in the search panel");
        //verify that searched keyword should be able to seen in the search panel
        Assert.assertEquals(searchPage.searchTermSearchFormInputBox.getAttribute("value"), keyword.toLowerCase());
        //verify that searched location should be able to seen in the search panel
        Assert.assertTrue(searchPage.inWhichCitySearchInbutBox.getAttribute("value").contains(location));
    }

    //if the user does not type any location into "In Which City?" inputbox then user navigates to the search results page for location of "Nederland"
    @Then("user navigates to search results page")
    public void user_navigates_to_search_results_page() {
        log.info("user navigates to search results page");
        BrowserUtils.waitFor(18);
        BrowserUtils.switchToWindow(homePage.getSearchResultsPageTitle("Nederland"));
        // verify that number of search results appear
        BrowserUtils.verifyElementDisplayed(searchPage.NumberofSearchResults);

    }

    @Then("user sees {string} service on the search results page")
    public void user_sees_service_on_the_search_results_page(String serviceName) {
        log.info("user sees service on the search results page");
        //locator xpath for service
        String serviceWebElementxpath= "//*[@alt='"+serviceName+"']";
        //verify that expected service should be able to seen in the search results page
        Assert.assertTrue(Driver.get().findElement(By.xpath(serviceWebElementxpath)).isDisplayed());
    }

    //this feauture is currently hidden by the developers upon the request of PO
    @Then("search results should be sorted in corresponding to the searched location {string}")
    public void search_results_should_be_sorted_in_corresponding_to_the_searched_location(String location) {

        log.info("search results are sorted in corresponding to the searched location");

        BrowserUtils.waitFor(5);
        searchPage.acceptCookiesButtonSearchResultPage.click();

        //verify that distance is in an ascending order on the Search result page
        for (int i = 0; i < searchPage.distanceonSearchResultPage.size()-1; i++) {
            //get km for each service on the page
            //convert String to Double. ex: convert (1 Km) to 1.0
            Assert.assertTrue( (Double.parseDouble(searchPage.distanceonSearchResultPage.get(i).getText().replaceAll("[^0-9.]", "")))<=Double.parseDouble(searchPage.distanceonSearchResultPage.get(i+1).getText().replaceAll("[^0-9.]", "")));
        }

        // if searched location is found in the page, verify that first service should have same location
        for (WebElement each:searchPage.locationonSearchResultPage) {
                if(each.getText().contains(location)){
                    Assert.assertTrue(searchPage.locationonSearchResultPage.get(0).getText().contains(location));
                }
        }
    }

    @Then("user sees all event types on the search panel")
    public void user_sees_all_event_types_on_the_search_panel() {
        log.info("all event types are seen on the search panel");

        Select eventTypeDropdown = new Select(searchPage.dropdownListofEventTypesonSearchPanel);
        for (int i = 0; i <eventTypeDropdown.getOptions().size()-1 ; i++) {
            //verify that actual and expected list of event types match
            SearchStepDefsDB.expectedListofEventTypes.contains(eventTypeDropdown.getOptions().get(i).getText());
        }
    }

    @Then("user sees all categories on the search panel")
    public void user_sees_all_categories_on_the_search_panel() {
        log.info("all categories are seen on the search panel");
        Select categoryDropdown = new Select(searchPage.dropdownListofCategoryTypesonSearchPanel);
        for (int i = 0; i <categoryDropdown.getOptions().size()-1; i++) {
            //verify that actual and expected list of category types match
            SearchStepDefsDB.expectedListofCategoryTypes.contains(categoryDropdown.getOptions().get(i).getText());
        }
    }

    @When("user clicks search button on search panel")
    public void user_clicks_search_button_on_search_panel() {
        log.info("user clicks search button on search panel");
        BrowserUtils.waitForClickablility(searchPage.searchButtononSearchPanel,3);
        searchPage.searchButtononSearchPanel.click();
        BrowserUtils.waitFor(5);
    }

    @When("user types random service keyword into What are you looking for? inputbox on search panel")
    public void user_types_random_service_keyword_into_What_are_you_looking_for_inputbox_on_search_panel() {
        log.info("user types random service keyword on search panel");
        //user types random service (selected from DB) into What are you looking for? inputbox on search panel
        searchPage.searchTermSearchFormInputBox.sendKeys(SearchStepDefsDB.serviceName.split("=")[0]);

    }

    @When("user types {string} keyword into What are you looking for? inputbox on search panel")
    public void user_types_keyword_into_What_are_you_looking_for_inputbox_on_search_panel(String keyword) {
        log.info("user types keyword on search panel");
        searchPage.searchTermSearchFormInputBox.sendKeys(keyword);
    }

    @When("user enters {string} location keyword into Location inputbox on search panel")
    public void user_enters_location_keyword_into_Location_inputbox_on_search_panel(String location) {
        log.info("user enters location keyword on search panel");
        searchPage.inWhichCitySearchInbutBox.clear();
        searchPage.inWhichCitySearchInbutBox.sendKeys(location);
        Driver.get().findElements(By.cssSelector(".pac-item")).get(0).click();
    }

    @When("user select {string} event type")
    public void user_select_event_type(String eventType) {
        log.info("user selects event type");
        Select eventTypeDropdown = new Select(searchPage.dropdownListofEventTypesonSearchPanel);
        for (int i = 0; i <eventTypeDropdown.getOptions().size()-1 ; i++) {
            if(eventTypeDropdown.getOptions().get(i).getText().equals(eventType)) {
                eventTypeDropdown.getOptions().get(i).click();
                break;
            }
        }
        BrowserUtils.waitFor(3);

    }

    @When("user select {string} category")
    public void user_select_category(String categoryType) {
        log.info("user selects category");
        Select categoryDropdown = new Select(searchPage.dropdownListofCategoryTypesonSearchPanel);
        for (int i = 0; i <categoryDropdown.getOptions().size()-1 ; i++) {
            if(categoryDropdown.getOptions().get(i).getText().equals("- "+categoryType)) {
                categoryDropdown.getOptions().get(i).click();
                break;
            }
        }
        BrowserUtils.waitFor(2);
    }


    @Then("user sees search results corresponding to the selected event type")
    public void user_sees_search_results_corresponding_to_the_selected_event_type() {
        log.info("search results are corresponding to the selected event type");

        //verify that search results are consistent with selected event type

            List<String> serviceListSearchResults = new ArrayList<>();
            for (WebElement each:searchPage.ServiceNamesResultPage) {
                serviceListSearchResults.add(each.getText().toString());
            }
        System.out.println("serviceListSearchResults = " + serviceListSearchResults);
        System.out.println("serviceListSearchResults.size() = " + serviceListSearchResults.size());
        SearchStepDefsDB.serviceListforEventType.containsAll(serviceListSearchResults);

        }

    @Then("user sees search results corresponding to the selected category")
    public void user_sees_search_results_corresponding_to_the_selected_category() {

        log.info("search results are seen corresponding to the selected category");

        //verify that search results are consistent with selected event type

        List<String> serviceListSearchResults = new ArrayList<>();
        for (WebElement each:searchPage.ServiceNamesResultPage) {
            serviceListSearchResults.add(each.getText().toString());
        }
        System.out.println("serviceListSearchResults = " + serviceListSearchResults);
        SearchStepDefsDB.serviceListforCategory.containsAll(serviceListSearchResults);

    }

    @When("user chooses random service on the search results page")
    public void user_chooses_random_service_on_the_search_results_page() {

        log.info("user chooses random service on the search results page");


        boolean flag = BrowserUtils.getElementsText(searchPage.ServiceNamesResultPage).contains(SearchStepDefsDB.serviceName);
        //if the service is not seen on current page, navigate to next page
        while(!flag) {
            BrowserUtils.clickWithJS(searchPage.nextPageButtononSearchResultPage);
            BrowserUtils.waitFor(4);
            flag = BrowserUtils.getElementsText(searchPage.ServiceNamesResultPage).contains(SearchStepDefsDB.serviceName);
        }
        //click random service on search result page
        if (flag) {
            for (WebElement each : searchPage.ServiceNamesResultPage) {
                if (each.getText().equalsIgnoreCase(SearchStepDefsDB.serviceName)) {
                    BrowserUtils.clickWithJS(each);
                    System.out.println("each.getText() = " + each.getText());
                    break;
                }
            }
        }

    }

    @Then("user navigates to random service detailed page")
    public void user_navigates_to_random_service_detailed_page() {
        log.info("user navigates to random service detailed page");

        BrowserUtils.waitFor(4);
        //verify that page title contains random serviceName
        Assert.assertTrue(Driver.get().getTitle().contains(SearchStepDefsDB.serviceName));
    }

    @When("user clicks clear button on search panel")
    public void user_clicks_clear_button_on_search_panel() {
        log.info("user clicks clear button on search panel");

        BrowserUtils.waitForClickablility(searchPage.clearButtononSearchPanel,1);
        searchPage.clearButtononSearchPanel.click();
        BrowserUtils.clickWithJS(searchPage.clearButtononSearchPanel);
        BrowserUtils.waitFor(2);

    }

    @Then("user sees default search filters")
    public void user_sees_default_search_filters() {
        log.info("user sees default search filters");

        //verify that searched term on search panel is cleared
        searchPage.searchTermSearchFormInputBox.getText().isEmpty();
        //verify that location term on search panel should be equal to Nederland
        searchPage.inWhichCitySearchInbutBox.getText().equalsIgnoreCase("Nederland");
        //verify that default selected type is "All event types"
        BrowserUtils.waitForVisibility(searchPage.dropdownListofEventTypesonSearchPanel,2);
        Select eventTypeDropdown = new Select(searchPage.dropdownListofEventTypesonSearchPanel);
        Assert.assertEquals(eventTypeDropdown.getFirstSelectedOption().getText(),"All event types");
        //verify that default selected category is "All categories"
        Select categoryDropdown = new Select(searchPage.dropdownListofCategoryTypesonSearchPanel);
        Assert.assertEquals(categoryDropdown.getFirstSelectedOption().getText(),"All categories");
    }

    @When("user sort search results based on {string}")
    public void user_sort_search_results_based_on(String sortCriteria) {

        log.info("user sort search results");

        //click selected option
        BrowserUtils.clickWithJS(Driver.get().findElement(By.xpath("(//*[text()=' "+sortCriteria+" '])[2]")));
        BrowserUtils.waitFor(5);
    }

    @Then("user sees results corresponding to {string} sorting")
    public void user_sees_results_corresponding_to_sorting(String sortCriteria) {
        log.info("search results are seen based on sorting");

        // wait until package price is visible
        BrowserUtils.waitForVisibility(searchPage.priceonSearchResultPage.get(0), 7);

        //create an empty list
        List<Double> listofPackagePrice = new LinkedList<>();

        for (WebElement each : searchPage.priceonSearchResultPage) {
            //add text of each element to the list
            // we get rid of all non digit characters
            double d = Double.parseDouble(each.getText().replaceAll("[^0-9.]", ""));
            listofPackagePrice.add(Double.parseDouble(each.getText().replaceAll("[^0-9.]", "")));
        }
        if (sortCriteria.equals("Price: low to high")) {
            for (int i = 0; i <listofPackagePrice.size() - 1; i++) {
                //verify that each package price is equal or less than next in line
                Assert.assertTrue(listofPackagePrice.get(i) <= listofPackagePrice.get(i + 1));
            }
        }
        if (sortCriteria.equals("Price: high to low")) {
            for (int i = 0; i < listofPackagePrice.size() - 1; i++) {
                //verify that each package price is equal or greater than next in line
                Assert.assertTrue(listofPackagePrice.get(i) >= listofPackagePrice.get(i + 1));
            }
        }
        // for "average score", "newest to oldest" and "oldest to newest" options database connection is required
    }

    @When("user chooses a random service on the service detailed page")
    public void user_chooses_a_random_service_on_the_service_detailed_page() {
        log.info("user chooses a random service on the service detailed page");
        //click the photo of a random service on search result page
        WebElement randomServiceWE= BrowserUtils.getRandomElement(searchPage.ServiceNamesResultPage);
        BrowserUtils.clickWithJS(randomServiceWE);
        randomService = randomServiceWE.getText();
        System.out.println("randomService = " + randomService);

    }

    @Then("user navigates to selected random service detailed page")
    public void user_navigates_to_selected_random_service_detailed_page() {
        log.info("user navigates to selected random service detailed page");

        BrowserUtils.waitFor(4);
        //verify that page title contains serviceName
        Assert.assertTrue(Driver.get().getTitle().contains(randomService));

    }


    @When("user chooses {string} service on the service detailed page")
    public void user_chooses_service_on_the_service_detailed_page(String serviceName) {
        log.info("user chooses service on the service detailed page");

        //click the photo of the relevant service on search result page
        for (WebElement each:searchPage.ServiceNamesResultPage) {
            if(each.getText().equals(serviceName)){
                BrowserUtils.clickWithJS(each);
                break;
            }
        }
    }

    @Then("user navigates to {string} service detailed page")
    public void user_navigates_to_service_detailed_page(String serviceName) {
        log.info("user navigates to service detailed page");
        BrowserUtils.waitFor(4);
        //verify that page title contains serviceName
        Assert.assertTrue(Driver.get().getTitle().contains(serviceName));

    }

    @Then("user sees all available packages on service detailed page")
    public void user_sees_all_available_packages_on_service_detailed_page(List<String> expectedPackages) {
        log.info("all available packages on service detailed page are seen");

        List actualPackagesonServiceDetailedPage = new ArrayList<>();
        for (int i = 0; i < serviceDetailedPage.packageNameonServiceDetailedPage.size(); i++) {
            actualPackagesonServiceDetailedPage.add(serviceDetailedPage.packageNameonServiceDetailedPage.get(i).getText());
        }
        //verify that search results cover expected packages
        Assert.assertTrue(actualPackagesonServiceDetailedPage.containsAll(expectedPackages));

    }

    @Then("price, number of persons, deposit, cancellation period, and package duration of {string} package on service detailed page should be correct")
    public void price_number_of_persons_deposit_cancellation_period_and_package_duration_of_package_on_service_detailed_page_should_be_correct (String packageName) {
        log.info("price, number of persons, deposit, cancellation period, and package duration of the package on service detailed page should be correct");

        //get the index number of the given package on service detailed page
        int indexNumberofPackage = serviceDetailedPage.getIndexofPackageonSPServiceDetailPage(packageName);

        //actual Price of the package
        //retrives it as string such as € 20 / person
        //convert it to Integer such as 20
        int actualPriceofPackage = Integer.parseInt(serviceDetailedPage.packagePriceonServiceDetailedPage.get(indexNumberofPackage).getText().replaceAll("[^0-9]", ""));

        //actual minumum and max number of persons of the package
        int actualminNumberofPersonsofPackage;
        int actualmaxNumberofPersonsofPackage;
        //if SP defines max number of attendees:
        // on user demo it is seen as:   6 - 200
        if(serviceDetailedPage.numberofPersonsonServiceDetailedPage.get(indexNumberofPackage).getText().contains("-")){
            actualminNumberofPersonsofPackage = Integer.parseInt(serviceDetailedPage.numberofPersonsonServiceDetailedPage.get(indexNumberofPackage).getText().replaceAll("[^0-9-]", "").split("-")[0]);
            actualmaxNumberofPersonsofPackage = Integer.parseInt(serviceDetailedPage.numberofPersonsonServiceDetailedPage.get(indexNumberofPackage).getText().replaceAll("[^0-9-]", "").split("-")[1]);
        }
        //if SP does not defines max number of attendees:
        // on user demo it is seen as:  3 (min)
        else{
            actualminNumberofPersonsofPackage = Integer.parseInt(serviceDetailedPage.numberofPersonsonServiceDetailedPage.get(indexNumberofPackage).getText().replaceAll("[^0-9]", ""));
            actualmaxNumberofPersonsofPackage = 0;
        }

        //actual deposit
        int actualDeposit = Integer.parseInt(serviceDetailedPage.depositonServiceDetailedPage.get(indexNumberofPackage).getText().replaceAll("[^0-9]", ""));

        //actual cancellation period
        int actualCancellationPeriod = Integer.parseInt(serviceDetailedPage.cancellationPeriodonServiceDetailedPage.get(indexNumberofPackage).getText().replaceAll("[^0-9]", ""));

        //actual package duration
        //on user demo seen it seen as: 3 : 00
        int actualPackageDuration = Integer.parseInt(serviceDetailedPage.packageDurationonServiceDetailedPage.get(indexNumberofPackage).getText().replaceAll("[^0-9:]", "").split(":")[0]);

        //get service name for given package
        String serviceName = serviceDetailedPage.getServiceNameforgivenPackage();

        //open new tab and switch to it
        BrowserUtils.switchesToNewTab();

        //navigates to related package detail page on SP page in new tab
        SPServiceandPackageDetailsNavigationsPage.navigatetoSPPackageDetailPage(serviceName,packageName);

        //click Capacity and Price Tab in Package Detail page
        sp_servicePackagesPage.capacityandPriceTabonPackageDetail.click();
        //expexted price of Package (coming from SP panel)
        int expectedPriceofPackage= Integer.parseInt(sp_servicePackagesPage.ServicePackagePriceInputBox.getAttribute("value"));
        //price comparision/validation
        Assert.assertEquals(actualPriceofPackage, expectedPriceofPackage);

        //expected minNumberofPersonsofPackage and maxNumberofPersonsofPackage (coming from SP panel)
        int expectedminNumberofPersonsofPackage= Integer.parseInt(sp_servicePackagesPage.PackageMinCapacityInputBox.getAttribute("value"));
        int expectedmaxNumberofPersonsofPackage;
        //if expectedmaxNumberofPersonsofPackage is not null
        if(!sp_servicePackagesPage.PackageMaxCapacityInputBox.getAttribute("value").isEmpty()) {
            expectedmaxNumberofPersonsofPackage= Integer.parseInt(sp_servicePackagesPage.PackageMaxCapacityInputBox.getAttribute("value"));
        //if expectedmaxNumberofPersonsofPackage is  null
        } else{
            expectedmaxNumberofPersonsofPackage=0;
        }

        //min and max number of persons comparision/validation
        Assert.assertEquals(actualminNumberofPersonsofPackage, expectedminNumberofPersonsofPackage);
        Assert.assertEquals(actualmaxNumberofPersonsofPackage, expectedmaxNumberofPersonsofPackage);

        //expexted deposit (coming from SP panel)
        int expectedDeposit= Integer.parseInt(sp_servicePackagesPage.PackageDepositPercentageInputBox.getAttribute("value"));
        //deposit comparision/validation
        Assert.assertEquals(actualDeposit, expectedDeposit);

        //expexted cancellation period (coming from SP panel)
        int expectedCancellationPeriod= Integer.parseInt(sp_servicePackagesPage.PackageCancellationPeriodInputBox.getAttribute("value"));
        //cancellation period comparision/validation
        Assert.assertEquals(actualCancellationPeriod, expectedCancellationPeriod);

        //package duration (coming from SP panel)
        int expectedPackageDuration= Integer.parseInt(sp_servicePackagesPage.PackageDurationHoursInputBox.getAttribute("value"));
        //package duration comparision/validation
        Assert.assertEquals(actualPackageDuration, expectedPackageDuration);

        // logout from SP account
        userandSPLoginandRegistrationPage.logoutforSP();

        //Driver navigates back to service detailed back
        BrowserUtils.switchesToPreviousTab();
    }

    @Then("description of {string} package on service detailed page should be correct")
    public void description_of_package_on_service_detailed_page_should_be_correct(String packageName) {

        log.info("description of the package on service detailed page should be correct");

        //get the index number of the given package on service detailed page
        int indexNumberofPackage = serviceDetailedPage.getIndexofPackageonSPServiceDetailPage(packageName);

        //actual description of the package
        String actualDesciptionofPackage = serviceDetailedPage.packageDescriptionServiceDetailedPage.get(indexNumberofPackage).getText();

        //get service name for given package
        String serviceName = serviceDetailedPage.getServiceNameforgivenPackage();

        //open new tab and switch to it
        BrowserUtils.switchesToNewTab();

        //navigates to related package detail page on SP page in new tab
        SPServiceandPackageDetailsNavigationsPage.navigatetoSPPackageDetailPage(serviceName,packageName);

        //click description Tab in Package Detail page
        sp_servicePackagesPage.descriptionTabonPackageDetail.click();

        //expected package description (coming from SP panel)
        String expectedDescriptionofPackage= sp_servicePackagesPage.PackagedescriptiononPackageDetail.getAttribute("value");

        //package duration comparision/validation
        Assert.assertTrue(expectedDescriptionofPackage.contains(actualDesciptionofPackage));

        // logout from SP account and close new tab
        userandSPLoginandRegistrationPage.logoutforSP();

        //Driver navigates back to service detailed back
        BrowserUtils.switchesToPreviousTab();

    }
    @Then("user sees all available options for {string} package on service detailed page")
    public void user_sees_all_available_options_for_package_on_service_detailed_page(String packageName) {
        log.info("all available options for the package on service detailed page are seen");

        //get the index number of the given package on service detailed page
        int indexNumberofPackage = serviceDetailedPage.getIndexofPackageonSPServiceDetailPage(packageName);

        //locator for packageoptions ==> (//*[@class='mb-2']) [indexNUMBER+1]/parent::div/div/div/a

        List<WebElement> packageOptions= Driver.get().findElements(By.xpath("(//*[@class='mb-2']) ["+(indexNumberofPackage+1)+"]/parent::div/div/div/a"));

        //add text of each Weblement to create a list of actual package options
        //package options looks like: Type: Barbeque Cuisine: Indian
        //we get only first parts: Type and Cuisine
        List<String> listofActualPackageOptions =new ArrayList<>();
        for (WebElement each: packageOptions) {
            listofActualPackageOptions.add(each.getText().split(":")[0]);
        }
        System.out.println("listofActualPackageOptions = " + listofActualPackageOptions);

        //get service name for given package
        String serviceName = serviceDetailedPage.getServiceNameforgivenPackage();

        //open new tab and switch to it
        BrowserUtils.switchesToNewTab();

        //navigates to related package detail page on SP page in new tab
        SPServiceandPackageDetailsNavigationsPage.navigatetoSPPackageDetailPage(serviceName,packageName);

        //click Package Options Tab in Package Detail page
        sp_servicePackagesPage.packageOptionsTabonPackageDetail.click();

        //expected package options (coming from SP panel)
        List<String> expectedPackageOptions = new ArrayList<>();
        BrowserUtils.waitForVisibility(sp_servicePackagesPage.selectedPackageOptions1onPackageDetail.get(0),1);
        for (int i = 0; i <sp_servicePackagesPage.selectedPackageOptions1onPackageDetail.size() ; i++) {
            expectedPackageOptions.add(sp_servicePackagesPage.selectedPackageOptions1onPackageDetail.get(i).getText());
        }
        System.out.println("expectedPackageOptions = " + expectedPackageOptions);

        //package options comparision/validation
        Assert.assertTrue(expectedPackageOptions.containsAll(listofActualPackageOptions));

        // logout from SP account and close new tab
        userandSPLoginandRegistrationPage.logoutforSP();

        //Driver navigates back to service detailed back
        BrowserUtils.switchesToPreviousTab();

    }

    @Then("user sees all available add-on options for {string} on service detailed page if the service has an active package")
    public void user_sees_all_available_add_on_options_for_on_service_detailed_page_if_the_service_has_an_active_package(String string) {
        log.info("all available add-on options for the package on service detailed page are seen");

    }

//    @Then("either add to basket or check availability button is visible and clickable on service detailed page")
//    public void either_add_to_basket_or_check_availabilty_button_is_visible_and_clickable_on_service_detailed_page() {
//        try {
//            if (serviceDetailedPage.checkAvailabilityButtonfortheFirstPackageonServiceDetailedPage.isDisplayed()) {
//                BrowserUtils.waitForClickablility(serviceDetailedPage.checkAvailabilityButtonfortheFirstPackageonServiceDetailedPage, 1);
//            }
//        } catch (Exception exception) {
//            BrowserUtils.waitForClickablility(serviceDetailedPage.addtoBasketButtonforPackagesonServiceDetailedPage.get(0),1);
//        }
//    }
//    @Then("request a quote button should be visible and clickable on service detailed page")
//    public void request_a_quote_button_should_be_visible_and_clickable_on_service_detailed_page() {
//        BrowserUtils.waitForClickablility(serviceDetailedPage.requestaQuoteButtononServiceDetailedPage,1);
//    }

    @Then("working hours of the {string} service matches expected data on service detailed page")
    public void working_hours_of_the_service_matches_expected_data_on_service_detailed_page(String serviceName) {

        log.info("working hours of the service matches expected data on service detailed page");

        Map<String, String> mapactualWorkingDays= new LinkedHashMap<>();
        //add text of each element to the Map
        for (int i = 0; i < serviceDetailedPage.workHoursandDaysServiceDetailedPage.size() ; i++) {
            // text of each element looks like:  Monday:  9:00 - 21:00  or Tuesday: Closed
            //we need string manipulation
            //we get days with 3 letter such as: Mon, Tue
            mapactualWorkingDays.put(serviceDetailedPage.workHoursandDaysServiceDetailedPage.get(i).getText().split(":", 2)[0].substring(0, 3), serviceDetailedPage.workHoursandDaysServiceDetailedPage.get(i).getText().split(":", 2)[1]);
        }

        //we need to remove the days from Map if the service is closed on that day
        if(mapactualWorkingDays.get("Mon").contains("Closed")){
            mapactualWorkingDays.remove("Mon");
        }
        if(mapactualWorkingDays.get("Tue").contains("Closed")){
            mapactualWorkingDays.remove("Tue");
        }
        if(mapactualWorkingDays.get("Wed").contains("Closed")){
            mapactualWorkingDays.remove("Wed");
        }
        if(mapactualWorkingDays.get("Thu").contains("Closed")){
            mapactualWorkingDays.remove("Thu");
        }
        if(mapactualWorkingDays.get("Fri").contains("Closed")){
            mapactualWorkingDays.remove("Fri");
        }
        if(mapactualWorkingDays.get("Sat").contains("Closed")){
            mapactualWorkingDays.remove("Sat");
        }
        if(mapactualWorkingDays.get("Sun").contains("Closed")){
            mapactualWorkingDays.remove("Sun");
        }

        //we convert Map to the List to compare it with expected working days and hours
        List<String> actualWorkingDays = new LinkedList<>();
        actualWorkingDays.addAll(mapactualWorkingDays.keySet());
        List<String> actualWorkingHours = new LinkedList<>();
        actualWorkingHours.addAll(mapactualWorkingDays.values());

        //as the each element refers to the same hour we can get the first start time and end time
        //we get working hours start time
        // after manipulation start time looks like: 9
        String actualWorkingHoursStartTimeHour =actualWorkingHours.get(0).split(":", 2)[0].split("-")[0].split(":")[0].replaceAll(" ", "");

        //String actualWorkingHoursStartTimeHour=serviceDetailedPage.workHoursandDaysServiceDetailedPage.get(0).getText().split(":", 2)[1].split("-")[0].split(":")[0].replaceAll(" ", "");
        //we get working hours end time
        // after manipulation start time looks like: 21
        String actualWorkingHoursEndTimeHour  =actualWorkingHours.get(0).split(":", 2)[1].split("-")[1].split(":")[0].replaceAll(" ", "");
        //String actualWorkingHoursEndTimeHour = serviceDetailedPage.workHoursandDaysServiceDetailedPage.get(0).getText().split(":", 2)[1].split("-")[1].split(":")[0].replaceAll(" ", "");

        //open new tab and switch to it
        BrowserUtils.switchesToNewTab();

        //navigates to SP panel
        SPServiceandPackageDetailsNavigationsPage.navigatetoSPMyServiceDetailPage(serviceName);
        //navigates to agenda on SP Panel
        sp_myServicesPage.agendaTabonServiceDetail.click();
        BrowserUtils.waitFor(2);

        //expected working hours and days (coming from SP panel)
        String expectedStartTimeHour= sp_myServicesPage.ServiceWorkHoursStartTimeHourInputBox.getAttribute("value");
        String expectedEndTimeHour= sp_myServicesPage.ServiceWorkHoursEndTimeHourInputBox.getAttribute("value");

        List<String> expectedAvailableDays = new ArrayList<>();
        for (int i = 0; i <sp_myServicesPage.ServiceAvailableDaysCheckbox.size() ; i++) {
            if(sp_myServicesPage.ServiceAvailableDaysCheckbox.get(i).isSelected()){
                expectedAvailableDays.add(sp_myServicesPage.ServiceAvailableDays.get(i).getText());
            }
        }

        //available days comparison/validation
        Assert.assertTrue(expectedAvailableDays.containsAll(actualWorkingDays));

        //available hours comparison/validation
        Assert.assertTrue(expectedStartTimeHour.contains(actualWorkingHoursStartTimeHour));
        Assert.assertTrue(expectedEndTimeHour.contains(actualWorkingHoursEndTimeHour));

        // logout from SP account and close new tab
        userandSPLoginandRegistrationPage.logoutforSP();

        //Driver navigates back to service detailed back
        BrowserUtils.switchesToPreviousTab();

    }

    @Then("service address should be visible if {string} service prefers to reveal it on service detailed page")
    public void service_address_should_be_visible_if_service_prefers_to_reveal_it_on_service_detailed_page(String serviceName) {

        log.info("service address should be visible if the service prefers to reveal it on service detailed page");

        String actualPostalCode=null;
        String expectedPostalCode;
        boolean flag =true;

        try {
            if (serviceDetailedPage.servicePostalCodeServiceDetailedPage.isDisplayed());
            // actual code format: Postal Code:  3584CT
            //we need only zip
            actualPostalCode= serviceDetailedPage.servicePostalCodeServiceDetailedPage.getText().split(":")[1].trim();
        } catch (Exception exception) {
            flag=false;
            System.out.println("actualPostalCode is not displayed");
        }

        //open new tab and switch to it
        BrowserUtils.switchesToNewTab();
        //navigates to SP panel
        SPServiceandPackageDetailsNavigationsPage.navigatetoSPMyServiceDetailPage(serviceName);
        //navigates to address tab on SP Panel
        sp_myServicesPage.addressTabonServiceDetail.click();

        //if SP hide its address on SP panel,service postal code is not displayed on service Page
        if (!flag) {
            BrowserUtils.waitForVisibility(sp_myServicesPage.ServiceAddressAddressToggleButton,2);
            //verify that address status is hidden
            Assert.assertTrue(sp_myServicesPage.ServiceAddressAddressToggleButton.getText().equalsIgnoreCase("hide"));

        }
        //if SP shows its address on SP panel,service postal code is displayed on service Page
        else {
            BrowserUtils.waitForVisibility(sp_myServicesPage.ServiceAddressAddressToggleButton,2);
            //verify that address status is shown
            Assert.assertTrue(sp_myServicesPage.ServiceAddressAddressToggleButton.getText().equalsIgnoreCase("show"));
            expectedPostalCode = sp_myServicesPage.ServicePostalCodeInputBox.getAttribute("value");
            //verify that postal code are same on SP panel and service page
            Assert.assertEquals(actualPostalCode, expectedPostalCode);
        }
        // logout from SP account and close new tab
        userandSPLoginandRegistrationPage.logoutforSP();

        //Driver navigates back to service detailed back
        BrowserUtils.switchesToPreviousTab();

    }
//    @Then("contact provider button should be visible and clickable on service detailed page")
//    public void contact_provider_button_should_be_visible_and_clickable_on_service_detailed_page() {
//        BrowserUtils.waitForClickablility(serviceDetailedPage.requestaQuoteButtononServiceDetailedPage,1);
//
//    }





}

//
//    Examples:
//            | event types                    | categories                 |
//            | Gender Reveal                  | Venue                      |
//            | Engagement Party               | - Meeting Spaces           |
//            | Religious Party                | - Wedding Halls            |
//            | Meeting                        | - Conference Centers       |
//            | Wedding                        | - Castles & Villas         |
//            | Coronaproof Party              | - Sport Halls              |
//            | Birthday                       | - Business Centers         |
//            | Funeral                        | - Expo Centers             |
//            | Children Party                 | - Restaurants              |
//            | Theme Party                    | - Bars & Lounges           |
//            | Company Party / Company Outing | - Academic Spaces          |
//            | Teambuilding Activity          | - Stadiums & Arenas        |
//            | Family Day                     | - Parks & Gardens          |
//            | Bachelor Party                 | Food & Drinks              |
//            | Fairs                          | - World                    |
//            | Sport                          | - Mediterranean            |
//            | Babyshower                     | - Snacks                   |
//            | Festival / Concerts            | - Drinks                   |
//            | Online Event                   | - Brunch                   |
//            | Food Truck Festival            | - Cocktails                |
//            | Congress / Seminars            | - Food Truck               |
//            | Friends Party                  | Entertainment              |
//            | Workshops                      | - Activity                 |
//            | Outdoor Activities             | - Animation                |
//            | Sustainable & Ecological       | - Cabaret                  |
//            | Drinks / Receptions            | - Chairman of the day      |
//            | Seasonal Events                | - Dance                    |
//            | School Party                   | - DJ                       |
//            |                                | - Children                 |
//            |                                | - Magic                    |
//            |                                | - Music                    |
//            |                                | - Orchestra                |
//            |                                | - Show                     |
//            |                                | - Speaker                  |
//            |                                | - Wedding official         |
//            |                                | Services                   |
//            |                                | - Phototography            |
//            |                                | - Printing                 |
//            |                                | - Decoration               |
//            |                                | - Transportation           |
//            |                                | - Gifts                    |
//            |                                | - Equipment                |
//            |                                | - Host/Hostess             |
//            |                                | - Event Web Page           |
//            |                                | - Training                 |
//            |                                | - Miscellaneous - Training |

//            |                  | Venue                      |
//            |               | - Meeting Spaces           |
//            |               | - Wedding Halls            |
//            |                       | - Conference Centers       |
//            |                     | - Castles & Villas         |
//            |             | - Sport Halls              |
//            |                       | - Business Centers         |
//            |                       | - Expo Centers             |
//            |                | - Restaurants              |
//            |                    | - Bars & Lounges           |
//            | | - Academic Spaces          |
//            |         | - Stadiums & Arenas        |
//            |                     | - Parks & Gardens          |
//            |                 | Food & Drinks              |
//            |                          | - World                    |
//            |                           | - Mediterranean            |
//            |                    | - Snacks                   |
//            |            | - Drinks                   |
//            |                   | - Brunch                   |
//            |            | - Cocktails                |
//            |            | - Food Truck               |
//            |                  | Entertainment              |
//            |                       | - Activity                 |
//            |             | - Animation                |
//            |       | - Cabaret                  |
//            |            | - Chairman of the day      |
//            |               | - Dance                    |
//            |