package ios.pages;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import ios.appium.Config;
import ios.appium.Utils;
import ios.appium.WebDriverPool;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class AppInitialization {

    public static void launchApp(String deviceName, String deviceType) {

        IOSDriver<IOSElement> driver = null;

        //device will be chosen based on the tags given to specification
        HashMap desiredCaps = Utils.getSegment(System.getProperty("user.dir") + "/resources/device_specs.yaml", deviceName);

        //Device Id is important when we have multiple devices with same OS
        //So we are getting device id, after the emulator is started
        String deviceIdToGet = checkEmulatorIsRunning(desiredCaps.get("DeviceName").toString(), desiredCaps.get("PlatformName").toString(), desiredCaps.get("PlatformVersion").toString());
        if (deviceIdToGet.isEmpty()) {
            startEmulator(desiredCaps.get("DeviceId").toString());
            deviceIdToGet = checkEmulatorIsRunning(desiredCaps.get("DeviceName").toString(), desiredCaps.get("PlatformName").toString(), desiredCaps.get("PlatformVersion").toString());
        }

        if (deviceIdToGet.isEmpty()) {
            return;
        }

        //defining the capapbilities for starting the device
        DesiredCapabilities ds = new DesiredCapabilities();
        ds.setCapability(MobileCapabilityType.APPIUM_VERSION, System.getenv("APPIUM_VERSION"));
        ds.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        ds.setCapability(MobileCapabilityType.PLATFORM_VERSION, desiredCaps.get("PlatformVersion"));
        ds.setCapability(MobileCapabilityType.DEVICE_NAME, desiredCaps.get("DeviceName"));
        ds.setCapability(MobileCapabilityType.NO_RESET, true);
        ds.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
        ds.setCapability(MobileCapabilityType.UDID, deviceIdToGet.toString());
        ds.setCapability("wdaLocalPort", desiredCaps.get("WdaPort"));
        ds.setCapability(MobileCapabilityType.APP, desiredCaps.get("App"));
        ds.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);


        try {
            String host = System.getenv("APPIUM_HOST").replace("PORT_NUMBER", String.valueOf(Config.currentPortNumber));
            System.out.println("Current Host : " + host);
            URL appiumHost = new URL(host);
            driver = new IOSDriver<IOSElement>(appiumHost, ds);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        WebDriverPool.setDriver(driver);
    }

    private static boolean startEmulator(String deviceId) {

        try {
            System.out.println("Trying to start : " + deviceId);
            Process process = Runtime.getRuntime().exec("xcrun simctl boot " + deviceId);
            Utils.waitForSeconds(3); //giving 3 seconds grace time to start the device, if your computer is low end configuration, device takes time to start

//            BufferedReader bre = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            if (bre.lines().count() > 0) {
//                return false;
//            }

            System.out.println("Emulator command is executed sucessfully");
            return checkBootingIsCompleted(deviceId);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkBootingIsCompleted(String deviceUdid) {
        boolean isBooted = false;

        try {
            System.out.println("Trying to start : " + deviceUdid);

            Process process = Runtime.getRuntime().exec("xcrun simctl bootstatus " + deviceUdid);
            Utils.waitForSeconds(3); //giving 3 seconds grace time to start the device, if your computer is low end configuration, device takes time to start
            String consoleLine = "";

            for (int i=1; i<=30; i++) {
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

                while((consoleLine = br.readLine()) != null) {
                    if (consoleLine.contains("Finished")) {
                        isBooted = true;
                        break;
                    }
                }

                if (isBooted) {
                    break;
                }
                Utils.waitForSeconds(1);
                br.close();
            }
            System.out.println("Emulator command is executed sucessfully");

        } catch (IOException e) {
            e.printStackTrace();
            isBooted = false;

        }

        return isBooted;
    }

    private static String checkEmulatorIsRunning(String deviceIdentifier, String platformName, String platformVersion) {

        String deviceId = "";
        Runtime rt = Runtime.getRuntime();
        String commandResult = null;

        try {

            Process process = rt.exec("xcrun simctl list devices -j");
            Utils.waitForSeconds(2);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder sb = new StringBuilder();

            while((commandResult = br.readLine()) != null) {
                sb.append(commandResult);
            }

            JsonParser jsonParser = new JsonParser();
            JsonObject response = (JsonObject) jsonParser.parse(sb.toString());
            JsonArray devices = response.getAsJsonObject("devices").getAsJsonArray(platformName + " " + platformVersion);

            for (JsonElement eachDevice : devices) {
                JsonObject currentDevice = eachDevice.getAsJsonObject();
                String currentDeviceName = currentDevice.get("name").toString().replaceAll("\"", "");
                String currentDeviceState = currentDevice.get("state").toString().replaceAll("\"", "");;
                String currentDeviceUdid = currentDevice.get("udid").toString().replaceAll("\"", "");;

                if (currentDeviceState.equalsIgnoreCase("Booted") && currentDeviceName.equalsIgnoreCase(deviceIdentifier)) {
                    deviceId = currentDeviceUdid;
                    break;
                }
            }

            System.out.println("Reading XCRUN SIMCTL LIST DEVICES output completed : device id is : [" + deviceId + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deviceId;
    }

    public static void closeTheApp() {
        WebDriverPool.getDriver().quit();
    }
}

