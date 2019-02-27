package ios.stepDefinitions;

import com.thoughtworks.gauge.Step;
import ios.pages.HomePage;

import static org.testng.AssertJUnit.assertTrue;

public class StepDefinitions {

    HomePage homePage = new HomePage();

    @Step ({"I ensure that test app is launched successfully", "When I am on App home page"})
    public void checkTheAppHomePageIsLoaded() {
        assertTrue(homePage.checkHomePageIsLoadedSuccessfully());
    }

    @Step ("I enter <value> in text field1")
    public void enterValueInTextField1(String value) {
        homePage.setDataInTextField1(value);
    }

    @Step ("I enter <value> in text field2")
    public void enterValueInTextField2(String value) {
        homePage.setDataInTextField2(value);
    }

    @Step ("I click on calculate sum button")
    public void clickOnCalcluateSumButton() {
        homePage.clickOnComputeSum();
    }

    @Step ("And I check the sum is <value>")
    public void checkTheSumIs(String value) {
        assertTrue(value.equalsIgnoreCase(homePage.getTheAnswer()));
    }

    @Step ("Then I swipe the scroll bar to <percentage> percentage")
    public void swipeTheScrollBarToLeft(int percentage) {
        homePage.scrollTheBarTo(percentage);
    }
}
