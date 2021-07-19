package testMobileBankingN.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

@RunWith(ConfigRunner.class)
public class ScrollTest {
	
	@Config
	public static MBTestConfiguration config = new MBTestConfiguration(
			"src/test/resources/parametertest.xls", //config test
			"c:/temp/parametertest_result.xls", 	//test result
			"parametertest");						//sheet name in file in
	
	public static AndroidDriver adDriver;

	@BeforeClass
	public static void before() throws MalformedURLException {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities(); 
		desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, config.getCapabilities().get("MobileCapabilityType.AUTOMATION_NAME"));
		desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, config.getCapabilities().get("MobileCapabilityType.PLATFORM_NAME"));
		desiredCapabilities.setCapability("dontStopAppOnReset", config.getCapabilities().get("dontStopAppOnReset"));
		desiredCapabilities.setCapability("appActivity", config.getCapabilities().get("appActivity"));
		desiredCapabilities.setCapability("appPackage", config.getCapabilities().get("appPackage"));
		desiredCapabilities.setCapability("autoGrantPermissions", config.getCapabilities().get("autoGrantPermissions"));
		//Tambahan capability agar junit junit tidak mereset aplikasi setelah test selesai
		desiredCapabilities.setCapability("noReset", "true");
	//	desiredCapabilities.setCapability("language", "en");
	//	desiredCapabilities.setCapability("locale", "US");
		
		
		URL url = new URL("http://localhost:4723/wd/hub");
		adDriver = new AndroidDriver<AndroidElement>(url, desiredCapabilities); 
		
	}
	
	@AfterClass
	public static void after() {
		config.close();

	}
	
	@Test
	public void scrollTest() throws  IOException {
		
		//PropertyResourceBundle propertyResourceBundle = new PropertyResourceBundle(new FileReader(new File("./src/test/resources/lang/String.properties")));
		ResourceBundle propertyResourceBundle = ResourceBundle.getBundle("lang/String", new Locale("en", "ID"));	
	
		System.out.println(propertyResourceBundle.getString("next_button"));
	}
	
	/**
	 * THis method search from scrollable el in view with content-desc is name. Scroll down is default.
	 * */
	
	private void selectElementByScroll(AndroidElement el, String name, int maxTryToScroll) {
		
	
		Rectangle rec = el.getRect();
		int scrollCount=0;
		
		while(scrollCount < maxTryToScroll) {
		
			try {	
			
				AndroidElement targetEl = (AndroidElement) new WebDriverWait(adDriver, 2).until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.View[@content-desc='"+ name+"']")));
				targetEl.click();
				return;
			
			} catch(Exception e) {
							
			}
			
			TouchAction action = new TouchAction(adDriver);
			action.press(PointOption.point( rec.getX() + rec.getWidth()/2, rec.getY() + rec.getHeight()-30))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(8000)))
					.moveTo(PointOption.point(rec.getX() + rec.getWidth()/2, rec.getY()+30))
					.release();
			action.perform();
			scrollCount++;	
		}
		
	}

}
