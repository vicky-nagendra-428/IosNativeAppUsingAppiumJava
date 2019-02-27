package ios.pages;

import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.iOSFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

    public HomePage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    @iOSFindBy(accessibility = "IntegerA")
    private IOSElement textField1;

    @iOSFindBy(accessibility = "IntegerB")
    private IOSElement textField2;

    @iOSFindBy(accessibility = "ComputeSumButton")
    private IOSElement computeSumButton;

    @iOSFindBy(xpath = "//*[@name='Answer']")
    private IOSElement answer;

    @iOSFindBy(className = "XCUIElementTypeSlider")
    private IOSElement slider;

    public boolean checkHomePageIsLoadedSuccessfully() {
        return isElementDisplayed(textField1);
    }

    public void setDataInTextField1(String value) {
        enterDataTo(textField1, value);
    }

    public void setDataInTextField2(String value) {
        enterDataTo(textField2, value);
    }

    public void clickOnComputeSum() {
        bringTheKeyboardDown();
        clickOnElement(computeSumButton);
    }

    public String getTheAnswer() {
        return getText(answer);
    }

    public void scrollTheBarTo(int percentageValue) {
        int scrollBarWidth = slider.getSize().width;
        int scrollBarHeight = slider.getSize().height;

        int startY = scrollBarHeight / 2;
        int currentlyAt = Integer.parseInt(getAttribute(slider, "value").replace("%", ""));

        int currentX = slider.getLocation().x + (scrollBarWidth * currentlyAt / 100);
        int endX = slider.getLocation().x + (scrollBarWidth * percentageValue / 100);

        int currentY = slider.getLocation().y + startY;

        System.out.println("currentX : " + currentX + "\n" + "endX : " + endX + "\n" + "currentY : " + currentY);

        swipe(slider, currentX, currentY, endX, currentY);

    }

}
