package testMobileBankingN.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.offset.ElementOption;

@RunWith(ConfigRunner.class)
public class LoginTest {
	
	public static AndroidDriver adDriver;
	
	@Config
	public static MBTestConfiguration config = new MBTestConfiguration(
													"src/test/resources/LoginTest.xls", //config test
													"c:/temp/LoginTest_result.xls", 	//test result
													"LoginTest");						//sheet name in file in
	
	@Before
	public static void before() throws MalformedURLException {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities(); 
		desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, config.getCapabilities().get("MobileCapabilityType.AUTOMATION_NAME"));
		desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, config.getCapabilities().get("MobileCapabilityType.PLATFORM_NAME"));
		desiredCapabilities.setCapability("dontStopAppOnReset", config.getCapabilities().get("dontStopAppOnReset"));
		desiredCapabilities.setCapability("appActivity", config.getCapabilities().get("appActivity"));
		desiredCapabilities.setCapability("appPackage", config.getCapabilities().get("appPackage"));
		desiredCapabilities.setCapability("autoGrantPermissions", config.getCapabilities().get("autoGrantPermissions"));
		//Tambahan capability agar junit junit tidak mereset aplikasi setelah test selesai
		//desiredCapabilities.setCapability("noReset", "true");
		
		URL url = new URL("http://localhost:4723/wd/hub");
		adDriver = new AndroidDriver<AndroidElement>(url, desiredCapabilities); 
		
	}
	
	@AfterClass
	public static void close() {
		config.close();
	}
	
	@Test
	public void loginTest() {
		
		try{
			login();
			config.writeToTestOuput("Status", "Success");
			
			byte[] srcFile = ((TakesScreenshot)adDriver).getScreenshotAs(OutputType.BYTES);
			config.saveScreenshot("Printscreen", srcFile);
		} catch(Exception e) {
			config.writeToTestOuput("Status", "Fail");
			config.writeToTestOuput("ErrorMessage", e.getMessage());
			
			byte[] srcFile = ((TakesScreenshot)adDriver).getScreenshotAs(OutputType.BYTES);
			config.saveScreenshot("Printscreen", srcFile);
			throw e;
		}
		
	}
	
	private void login() {
		
		final String loginButtonXPath ="//android.widget.Button[@content-desc='"+config.getResourceValue("login_button")+"']";
		final String confirmLoginButtonXPath ="//android.widget.Button[@content-desc='"+ config.getResourceValue("login_form_button")+ "']";
		final String otpInputXPath = "//android.view.View[contains(@content-desc, '"+ config.getResourceValue("otp_login_message")+"')]//following-sibling::android.view.View";
		
		
		CommonTest.clearNotif(adDriver);
		
		//wait login button loaded
		AndroidElement loginButton = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(loginButtonXPath)));
		
		loginButton.click();
		
		//wait form login loaded
		AndroidElement loginForm = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.className("android.widget.EditText")));
		
		List<AndroidElement> editTexts = adDriver.findElementsByClassName("android.widget.EditText");
		
		AndroidElement usernameInput = editTexts.get(0);
		usernameInput.click();
		usernameInput.sendKeys(config.getParameterValue("Username"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		AndroidElement passwordInput = editTexts.get(1);
		passwordInput.click();
		passwordInput.sendKeys(config.getParameterValue("Password"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		adDriver.findElementByXPath(confirmLoginButtonXPath).click();
	
		
		List<WebElement> otpElement =  new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(otpInputXPath)));
		
		
		AndroidElement otpInput = (AndroidElement) otpElement.get(0);
		
		String otp;
		
		if(config.getParameterValue("OTP").toUpperCase().equals("AUTO")) {
			 otp =  CommonTest.getOTPFromNotification(adDriver);
		}else {
			otp = config.getParameterValue("OTP");
		}
		
		if(config.getParameterValue("ExpiredOTP").toUpperCase().equals("YES")) {
		
			CommonTest.sleep(adDriver, 60000);
		}
		
		if(config.getParameterValue("ResendOTP").toUpperCase().equals("YES")) {
			CommonTest.clearNotif(adDriver);
			
			adDriver.findElementByXPath("//android.view.View[@content-desc='Kirim ulang']").click();
			otp =  CommonTest.getOTPFromNotification(adDriver);
		}
		
		TouchAction action = new TouchAction(adDriver);
		action.tap(ElementOption.element(otpInput,10,10));
		action.perform();
		
		CommonTest.keyNum(adDriver, otp);
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//next
		adDriver.findElementByXPath("//android.widget.Button[@content-desc='"+config.getResourceValue("next_login_button")+"']").click();
		
		//finally navigation bar loaded
		AndroidElement bankHome  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.View[contains(@content-desc,\"Assalamu'alaikum\")]")));
	}
	
	

}
