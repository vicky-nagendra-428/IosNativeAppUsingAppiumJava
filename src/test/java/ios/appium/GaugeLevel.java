package ios.appium;

import com.thoughtworks.gauge.AfterSpec;
import com.thoughtworks.gauge.BeforeSpec;
import com.thoughtworks.gauge.ExecutionContext;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import ios.pages.AppInitialization;

import java.util.List;

public class GaugeLevel {

    AppiumDriverLocalService service = null;
    Config config = new Config();
    static DataStore ds = DataStoreFactory.getSpecDataStore();

    @BeforeSpec
    public void setup(ExecutionContext context) {

        System.out.println("===========Before Spec===========");

        List<String> specTags = context.getCurrentSpecification().getTags();
        String deviceInfo = "";
        String deviceType = "";

        for (String tag :  specTags) {
            System.out.println("Tag : " + tag);
            if (tag.contains("device")) {
                deviceInfo = tag.split("-")[2];
                deviceType = tag.split("-")[1];
                break;
            }
        }

        System.out.println("Starting Appium");
        service = config.startAppium(deviceInfo);
        ds.put(context.getCurrentSpecification().getName().toString(), service);

        System.out.println("Device Name: " + deviceInfo);
        System.out.println("Device Type: " + deviceType);
        AppInitialization.launchApp(deviceInfo, deviceType);

    }

    @AfterSpec
    public void closeApp(ExecutionContext context) {
        System.out.println("===========After Spec===========");
        AppInitialization.closeTheApp();
        System.out.println("Stopping Appium");
        config.stopAppium((AppiumDriverLocalService) ds.get(context.getCurrentSpecification().getName().toString()));
    }

}

