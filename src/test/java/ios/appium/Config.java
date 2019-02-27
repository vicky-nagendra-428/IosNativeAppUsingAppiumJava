package ios.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.io.IOException;

public class Config {

    public static int currentPortNumber;

    public AppiumDriverLocalService startAppium(String device) {

        AppiumDriverLocalService service = null;

        currentPortNumber = Integer.parseInt(Utils.getValueFromYaml(System.getProperty("user.dir") + "/resources/device_specs.yaml", device, "PortNumber"));
        killProcessOnPort(currentPortNumber);

        String nodePath = System.getenv("NODE_PATH");
        String appiumJsPath = System.getenv("APPIUM_JS_PATH");
        String deviceName = Utils.getValueFromYaml(System.getProperty("user.dir") + "/resources/device_specs.yaml", device, "DeviceName");

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingDriverExecutable(new File(nodePath))
                .withAppiumJS(new File(appiumJsPath))
                .withIPAddress("0.0.0.0").usingPort(currentPortNumber)
//                .withArgument(GeneralServerFlag.ROBOT_ADDRESS, deviceName)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "debug")
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE);

        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        Utils.waitForSeconds(3); //giving time to appium to start
        System.out.println("Starting Appium - starting successful : on port number : " + currentPortNumber + " - " + service.isRunning());

        return service;

    }

    private static void killProcessOnPort(int portNumber) {
        Runtime runTime = Runtime.getRuntime();
        try {
            runTime.exec("kill $(lsof -t -i:" + portNumber + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopAppium(AppiumDriverLocalService service) {
        service.stop();
        System.out.println("Stop Appium successful : " + service.isRunning());
    }
}

