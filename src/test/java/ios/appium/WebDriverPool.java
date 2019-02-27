package ios.appium;

import io.appium.java_client.ios.IOSDriver;

public class WebDriverPool {

    private static ThreadLocal<IOSDriver> thread = new ThreadLocal<>();

    public static void setDriver(IOSDriver driverInstance) {
        thread.set(driverInstance);
    }

    public static IOSDriver getDriver() {
        return thread.get();
    }
}
