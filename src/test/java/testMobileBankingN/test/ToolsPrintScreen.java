package testMobileBankingN.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;

@RunWith(ConfigRunner.class)
public class ToolsPrintScreen {
	
	public static AndroidDriver adDriver;
	
	@Config
	public static MBTestConfiguration config = new MBTestConfiguration(
													"src/test/resources/PrintScreen.xls", //config test
													"c:/temp/PrintScreen_result.xls", 	//test result
													"PrintScreen");						//sheet name in file in
	
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
		
		URL url = new URL("http://localhost:4723/wd/hub");
		adDriver = new AndroidDriver<AndroidElement>(url, desiredCapabilities); 
		
	}
	
	@AfterClass
	public static void close() {
		config.close();
	}
	
	@Test
	public void screenshot() {
		
		System.out.println("This handy script is used for taking screenshot");
		
		Scanner scanner = new Scanner(System.in);

		String input = "";
		int col = 1;
		while(true) {
			System.out.println("Enter to take screenshot, q + Enter to quit");
			input = scanner.nextLine();
			if(input.toUpperCase().equals("Q")) {
				scanner.close();
				return;
			}
				

			byte[] srcFile = ((TakesScreenshot)adDriver).getScreenshotAs(OutputType.BYTES);
			config.saveScreenshot(col, srcFile);
			
			System.out.println("Screenshot saved");
			
			col++;
			
		}
		
	}

}
