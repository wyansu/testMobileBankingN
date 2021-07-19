package testMobileBankingN.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.offset.ElementOption;

@RunWith(ConfigRunner.class)
public class RegistrationTest {
	
	public static AndroidDriver adDriver;
	
	@Config
	public static MBTestConfiguration config = new MBTestConfiguration(
													"src/test/resources/RegistrationTest.xls", //config test
													"c:/temp/RegistrationTest_result.xls", 	//test result
													"RegistrationTest");						//sheet name in file in
	
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
	public void registrationTest( ) {
		try{
			registration();
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
	
	
	private void registration() {
		
		final String clearButtonId = "com.android.systemui:id/clear_all";
		
		final String registrationButtonXPath ="//android.widget.ImageView[@content-desc='"+ config.getResourceValue("registration_button")+"']";
		final String checkBoxApproveXPath ="//android.widget.CheckBox[@content-desc='"+ config.getResourceValue("tc_registration_checkbox")+"']";
		final String approveButtonXPath ="//android.widget.Button[@content-desc='"+ config.getResourceValue("approve_registration_button")+"']";
		final String inputAccountXPath ="//android.view.View[@content-desc='"+config.getResourceValue("account_registration_input")+"']//following-sibling::android.widget.EditText";
		final String nextButtonAccount ="//android.widget.Button[@content-desc='"+config.getResourceValue("next_registration_button")+"']";
		final String inputKTPXPath = "//android.view.View[@content-desc='"+config.getResourceValue("id_customer_registration_button")+"']//following-sibling::android.widget.EditText";
		final String inputEmailXPath ="//android.view.View[@content-desc='"+config.getResourceValue("email_registration_input")+"']//following-sibling::android.widget.EditText";
		final String OTPXPath ="//android.view.View[contains(@content-desc,'"+config.getResourceValue("otp_registration_message")+"')]//following-sibling::android.view.View";
		final String inputUsernameXPath ="//android.view.View[@content-desc='"+config.getResourceValue("username_registration_input")+"']//following-sibling::android.widget.EditText";
		final String inputPasswordXPath ="//android.view.View[@content-desc='"+config.getResourceValue("password_registration_input")+"']//following-sibling::android.widget.EditText";
		final String registrationLastPageXPath ="//android.view.View[@content-desc='"+config.getResourceValue("registration_successfull_message")+"']";
		
		//clearNotif
		adDriver.openNotifications();
		try {
			AndroidElement clearButton  = (AndroidElement) new WebDriverWait(adDriver, 5).until(
				ExpectedConditions.elementToBeClickable(By.id(clearButtonId)));
			
			clearButton.click();
		} catch(Exception e) {
			adDriver.pressKey(new KeyEvent(AndroidKey.BACK));
		}
		
		
		//wait until page loaded
		AndroidElement registrationButton = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(registrationButtonXPath)));
		
		registrationButton.click();
		
		//wait until TC page loaded
		AndroidElement tCCheckBox  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(checkBoxApproveXPath)));
		
		if(!config.getParameterValue("Accept").toUpperCase().equals("NO")) {
			tCCheckBox.click();
		}
		
		adDriver.findElementByXPath(approveButtonXPath).click();
		
		//wait username page loaded
		AndroidElement inputAccount  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(inputAccountXPath)));
		
		inputAccount.click();
		inputAccount.sendKeys(config.getParameterValue("Account"));
		//keyNum(testParam.get("Account"));

		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//next
		adDriver.findElementByXPath(nextButtonAccount).click();

		//wait id page loaded
		AndroidElement inputKTP  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(inputKTPXPath)));
		
		inputKTP.click();
		inputKTP.sendKeys(config.getParameterValue("ID"));
		//keyNum(testParam.get("ID"));
		
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		//next
		adDriver.findElementByXPath(nextButtonAccount).click();
		
		//wait email page loaded
		AndroidElement inputEmail  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(inputEmailXPath)));
		
		inputEmail.click();
		inputEmail.sendKeys(config.getParameterValue("Email"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//next
		adDriver.findElementByXPath(nextButtonAccount).click();
		
		//wait for OTP screen
		AndroidElement inputOTP  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(OTPXPath)));
		
		String otp;
		if(config.getParameterValue("OTP").toUpperCase().equals("AUTO") ) {
			otp = CommonTest.getOTPFromNotification(adDriver);
		} else {
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
		action.tap(ElementOption.element(inputOTP,10,10));
		action.perform();

		CommonTest.keyNum(adDriver,otp);
		
		//next
		adDriver.findElementByXPath(nextButtonAccount).click();
		
		//username
		AndroidElement inputUsername  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(inputUsernameXPath)));
		
		inputUsername.click();
		inputUsername.sendKeys(config.getParameterValue("Username"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//next
		adDriver.findElementByXPath(nextButtonAccount).click();
		
		//input password
		AndroidElement inputPassword  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(inputPasswordXPath)));
		
		List<AndroidElement> inputPwds = adDriver.findElementsByXPath(inputPasswordXPath);
		
		//pwd element
		AndroidElement inputMainPwd = inputPwds.get(0);
		inputMainPwd.click();
		inputMainPwd.sendKeys(config.getParameterValue("Password"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//pwd confirmpwd
		String confirmPwd;
		if(config.getParameterValue("ConfirmPwd").isBlank()) {
			confirmPwd = config.getParameterValue("Password");
		}
		else {
			confirmPwd = config.getParameterValue("ConfirmPwd");
		}
		
		AndroidElement inputConfirmPwd = inputPwds.get(1);
		inputConfirmPwd.click();
		inputConfirmPwd.sendKeys(confirmPwd);
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//next last step
		adDriver.findElementByXPath(nextButtonAccount).click();
		

		
		//make PIN
		makePIN(config.getResourceValue("mobile_pin_registration_input"),config.getParameterValue("PIN"));
		//confirmation PIN
		makePIN(config.getResourceValue("confirmation_mobile_pin_registration_input"), config.getParameterValue("ConfirmPIN"));
		
		//finally
		AndroidElement title  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
						ExpectedConditions.presenceOfElementLocated(By.xpath(registrationLastPageXPath)));
		
	}
	
	private void makePIN(String id, String pin) {

		String makePinTitleXPath = "//android.view.View[@content-desc='"+ id +"']";
		String pinKeyboardXPath = "//android.view.View[@content-desc='"+ id +"']//following-sibling::android.widget.Button";
		
		AndroidElement title  = (AndroidElement) new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.presenceOfElementLocated(By.xpath( makePinTitleXPath)));

		List<AndroidElement> keys = adDriver.findElementsByXPath(pinKeyboardXPath);
	
		char[] pins = pin.toCharArray();
		for(char pinEntry : pins) {
			AndroidElement keyElement;
			if(pinEntry == '0') {
				keyElement = keys.get(10);
			}
			else {
				keyElement = keys.get(Integer.parseInt(String.valueOf(pinEntry)) - 1);
			}
			
			keyElement.click();
			

		}
		
		//click confirm
		keys.get(11).click();
		
	}
	

}
