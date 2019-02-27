package ios.pages;

import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import ios.appium.WebDriverPool;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    public IOSDriver getDriver() {
        return WebDriverPool.getDriver();
    }

    public void waitForElementVisibility(IOSElement element, int timeToWait) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeToWait);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementClickability(IOSElement element, int timeToWait) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeToWait);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean isElementDisplayed(IOSElement element) {
        try {
            waitForElementVisibility(element, 20);
            return element.isDisplayed();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clickOnElement(IOSElement element) {
        try {
            waitForElementClickability(element, 20);
            element.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enterDataTo(IOSElement element, String dataToEnter) {
        element.sendKeys(dataToEnter);
    }

    public String getText(IOSElement element) {
        return element.getText();
    }

    public void bringTheKeyboardDown() {
        if (getDriver().isKeyboardShown()) {
            getDriver().hideKeyboard();
        }
    }

    public String getAttribute(IOSElement element, String attributeName) {
        return element.getAttribute(attributeName);
    }

    public void swipeUp(IOSElement element) {

        int startX = element.getCenter().x;
        int startY = element.getCenter().y;

        TouchAction ta = new TouchAction(getDriver());
        ta.press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(0, startY - 200)).release().perform();
    }

    public void swipeDown(IOSElement element) {

        int startX = element.getCenter().x;
        int startY = element.getCenter().y;

        TouchAction ta = new TouchAction(getDriver());
        ta.press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(0, startY + 200)).release().perform();
    }

    public void swipe(IOSElement element, int startX, int startY, int endX, int endY) {
        TouchAction touchAction = new TouchAction(getDriver());
        touchAction.press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(endX, endY))
                .release()
                .perform();
    }

}

