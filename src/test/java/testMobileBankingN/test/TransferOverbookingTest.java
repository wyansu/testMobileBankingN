package testMobileBankingN.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.junit.AfterClass;
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
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.offset.ElementOption;

@RunWith(ConfigRunner.class)
public class TransferOverbookingTest {
	
	@Config
	public static MBTestConfiguration config = new MBTestConfiguration(
			"src/test/resources/TransferOverbookingTest.xls", //config test
			"c:/temp/TransferOverbookingTest_result.xls", 	//test result
			"TransferOverbookingTest");						//sheet name in file in
	
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
		
		URL url = new URL("http://localhost:4723/wd/hub");
		adDriver = new AndroidDriver<AndroidElement>(url, desiredCapabilities); 
		
	}
	
	@AfterClass
	public static void after() {
		config.close();
	}
	
	@Test
	public void transferOverbookingTest() {
		try{
			transferTest();
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
	
	
	private void transferTest()  {
		//start this test from home
		
		final String homeTranferXPath ="//android.widget.ImageView[@content-desc='" + config.getResourceValue("transfer_icon") +"']";
		final String transferOverbookingXPath = "//android.widget.ImageView[@content-desc='"+ config.getResourceValue("transfer_overbooking_menu") +"']";
		final String transferBaruXPath ="//android.view.View[@content-desc='"+ config.getResourceValue("new_transfer_button")+"']";
		final String pilihAccountXPath ="//android.view.View[@content-desc='"+ config.getResourceValue("select_account_menu")+"']";
		final String sumberDanaXPath ="//android.view.View[@content-desc='"+config.getResourceValue("account_selection_dropdown_menu") +"']";
		final String inputRekeningTujuanXPath ="//android.view.View[@content-desc='"+config.getResourceValue("account_to_input")+"']//following-sibling::*[1][self::android.widget.EditText]";
		final String inputNominalXPath ="//android.view.View[@content-desc='"+config.getResourceValue("amount_transfer_input") +"']//following-sibling::*[1][self::android.widget.EditText]";
		final String beritaXPath ="//android.view.View[@content-desc='"+ config.getResourceValue("news_transfer_input")+"']//following-sibling::*[1][self::android.widget.EditText]";
		final String tanggalXPath ="//android.view.View[@content-desc='"+ config.getResourceValue("transfer_date_select")+"']//following-sibling::android.widget.ImageView";
		final String calendarXPath ="//android.widget.Button[@content-desc='"+ config.getResourceValue("input_date_manually_calendar_icon")+ "']";
		final String inputDateXPath ="//android.view.View[contains(@content-desc,'"+ config.getResourceValue("input_date_manually_input")+"')]//android.widget.EditText";
		final String transferConfirmationXPath = "//android.widget.Button[@content-desc='"+config.getResourceValue("transfer_confirmation_button")+"']";
		final String passwordNeededXPath = " //android.view.View[@content-desc='"+ config.getResourceValue("transfer_password_confirmation_page")+"']//following-sibling::android.widget.EditText";
		final String transferStatusXPath ="//android.view.View[@content-desc='"+config.getResourceValue("proof_successfull_overbooking_message")+"']";
		
		
		if(CommonTest.needPin(adDriver)) {
			CommonTest.inputPin(adDriver, config.getResourceValue("login_after_locked_page"), config.getParameterValue("PIN"));
		}
		
		
		if(!CommonTest.isAtHome(adDriver))
			CommonTest.backToHome(adDriver);
		
		AndroidElement transfer  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(homeTranferXPath)));
		
		transfer.click();
		//adDriver.findElementByXPath(homeTranferXPath).click();
		
		//wait overbook loaded
			AndroidElement transferOverbookingEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.elementToBeClickable(By.xpath(transferOverbookingXPath)));
			
			TouchAction action = new TouchAction(adDriver);
			action.tap(ElementOption.element(transferOverbookingEl,20,50));
			action.perform();
			
		
		AndroidElement transferBaruButton  = (AndroidElement) new WebDriverWait(adDriver,config.getElementSearchTimeout()).until(
					ExpectedConditions.elementToBeClickable(By.xpath(transferBaruXPath)));
		
		transferBaruButton.click();

		//wait tfr page loaded
		AndroidElement piliAccountButton  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(pilihAccountXPath)));
		piliAccountButton.click();
		
		//wait sumber dana tab loaded
		AndroidElement sumberDana  = (AndroidElement) new WebDriverWait(adDriver,config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(sumberDanaXPath)));
		
		AndroidElement chosenAccountEl = (AndroidElement) adDriver.findElementByXPath("//android.widget.ImageView[contains(@content-desc, '"+config.getParameterValue("AccountFrom") +"')]");
		chosenAccountEl.click();
		
		//click pilih button
		adDriver.findElementByXPath("//android.widget.Button[@content-desc='"+ config.getResourceValue("account_selection_dropdown_button")+"']").click();
		
		//input rekening tujuan
		AndroidElement rekeningTujuanEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(inputRekeningTujuanXPath)));
		
		rekeningTujuanEl.click();
		
		//CommonTest.keyNum(adDriver, testParam.get("AccountTo"));
		rekeningTujuanEl.sendKeys(config.getParameterValue("AccountTo"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//input amount
		AndroidElement nominalEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(inputNominalXPath)));
		
		nominalEl.click();

		nominalEl.sendKeys(config.getParameterValue("Amount"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//berita transfer
		AndroidElement beritaEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(beritaXPath)));
		beritaEl.click();
		
		beritaEl.sendKeys(config.getParameterValue("Berita"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		adDriver.findElementByXPath("//android.widget.Button[@content-desc='"+config.getParameterValue("Frekuensi")+ "']").click();
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		
		/*if(config.getParameterValue("DateFromCalendar").toUpperCase().equals("YES")) {
			adDriver.findElementByXPath(tanggalXPath).click();
			CommonTest.selectDateFromCalendar(adDriver, config.getParameterValue("Date"), new Locale("in", "ID"));
		
		
		}else{
			adDriver.findElementByXPath(tanggalXPath).click();
			
			AndroidElement calendarEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.elementToBeClickable(By.xpath(calendarXPath)));
			calendarEl.click();
			
			AndroidElement inputDateEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.elementToBeClickable(By.xpath(inputDateXPath)));
			
			inputDateEl.click();
			inputDateEl.sendKeys(config.getParameterValue("Date"));	
			
			adDriver.findElementByXPath("//android.widget.Button[@content-desc='"+ config.getResourceValue("ok_calendar_button") +"']").click();
			
		} */
		
		if(config.getParameterValue("DateFromCalendar").toUpperCase().equals("YES")) {
			adDriver.findElementByXPath(tanggalXPath).click();
			CommonTest.seekDateFromCalendar(adDriver, config.getParameterValue("Date"), config, config.getLocale());
			
		}
		
		
		//selanjutnya		
		adDriver.findElementByXPath("//android.widget.Button[@content-desc='"+config.getResourceValue("next_step_transfer_button")+"']").click();
		
		//wait for transfer confirmation page loaded
		AndroidElement transferEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(transferConfirmationXPath)));
		
		transferEl.click();
		
		//wait screen password if needed
		if(!config.getParameterValue("Password").isBlank()) {
			
			AndroidElement passwordEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.elementToBeClickable(By.xpath(passwordNeededXPath)));
			passwordEl.click();
			passwordEl.sendKeys(config.getParameterValue("Password"));
			if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
			
			adDriver.findElementByXPath("//android.widget.Button[@content-desc='"+config.getResourceValue("next_step_transfer_button")+"']").click();
		}
		
		//bukti transaksi loaded
		AndroidElement buktiTransaksiEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(transferStatusXPath)));
		
	}

}
