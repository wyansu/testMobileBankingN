package testMobileBankingN.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
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
public class TransferOtherBankTest {
	
	public static AndroidDriver adDriver;
	
	@Config
	public static MBTestConfiguration config = new MBTestConfiguration(
													"src/test/resources/TransferOtherBankTest.xls", //config test
													"c:/temp/TransferOtherBankTest_result.xls", 	//test result
													"TransferOtherBankTest");						//sheet name in file in
	
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
	
	@Test
	public void onlineTest() throws NoSuchFieldException {
		
		final String transferXPath = "//android.widget.ImageView[@content-desc='"+ config.getResourceValue("transfer_icon")+"']";
		final String transferOtherBankXPath = "//android.widget.ImageView[@content-desc='"+ config.getResourceValue("transfer_online_menu")+"']";
		final String newTransferXPath = "//android.view.View[@content-desc='"+ config.getResourceValue("new_transfer_transfer_other_bank_button")+"']";
		final String selectButtonXPath = "//android.view.View[@content-desc='"+ config.getResourceValue("select_account_transfer_other_bank_menu")+"']";
		final String selectSumberDanaXPath = "//android.view.View[@content-desc='"+ config.getResourceValue("account_selection_dropdown_transfer_other_bank_menu")+"']";
		final String sumberDanaXPath = "//android.widget.ImageView[contains(@content-desc, '" +config.getParameterValue("AccountFrom") +"')]";
		final String selectDestinationBankXPath ="//android.view.View[@content-desc='"+ config.getResourceValue("destination_bank_dropdown_menu")+"']";
		final String accountToXPath = "//android.view.View[@content-desc='"+ config.getResourceValue("account_to_transfer_other_bank_input")+"']//following-sibling::android.widget.EditText[1]";
		final String amountXPath = "//android.view.View[@content-desc='"+ config.getResourceValue("amount_transfer_transfer_other_bank_input")+"']//following-sibling::android.widget.EditText[1]";
		final String beritaXPath = "//android.view.View[@content-desc='"+ config.getResourceValue("news_transfer_transfer_other_bank_input")+"']//following-sibling::android.widget.EditText[1]";
		final String reffXPath = "//android.view.View[@content-desc='"+  config.getResourceValue("customer_reff_transfer_other_bank_input")+"']//following-sibling::android.widget.EditText[1]";
		final String transferConfirmationXPath = "//android.widget.Button[contains(@content-desc,'Confirmation') or contains(@content-desc,'Konfirmasi')]";
		
		if(CommonTest.needPin(adDriver)) {
			CommonTest.inputPin(adDriver, config.getResourceValue("login_after_locked_page"), config.getParameterValue("PIN"));
		}
	
	
		if(!CommonTest.isAtHome(adDriver))
			CommonTest.backToHome(adDriver);

		//wait until home page loaded
		AndroidElement transferEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(transferXPath)));
		
		transferEl.click();
		
		//wait until transfer menu loaded
		AndroidElement transferOtherBankEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(transferOtherBankXPath)));
		
		TouchAction action = new TouchAction(adDriver);
		action.tap(ElementOption.element(transferOtherBankEl,20,50));
		action.perform();
		
		if(config.getParameterValue("IfNewTransfer").toUpperCase().equals("YES")) {
			
			AndroidElement newTransferEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.elementToBeClickable(By.xpath(newTransferXPath)));
			
			newTransferEl.click();
			
		}
		
		//wait untuk transfer form loaded
		AndroidElement selectButtonEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(selectButtonXPath)));
		
		selectButtonEl.click();
		
		AndroidElement selectSumberDanaEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.presenceOfElementLocated(By.xpath(selectSumberDanaXPath)));
		
		AndroidElement sumberDanaEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(sumberDanaXPath)));
		sumberDanaEl.click();
		
		//next
		adDriver.findElementByXPath("//android.widget.Button[@content-desc='"+ config.getResourceValue("account_selection_dropdown_transfer_other_bank_button")+"']").click();
		
		//wait until 
		AndroidElement selectDestinationBankEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(selectDestinationBankXPath)));
		
		Rectangle rec = selectDestinationBankEl.getRect();
		
		action.tap(ElementOption.element(selectDestinationBankEl,rec.getWidth() -10,5)).perform();

		
		//wait until search bank page loaded
		AndroidElement destinationBankEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.presenceOfElementLocated (By.xpath("//android.view.View[@content-desc='"+config.getResourceValue("navigation_title_destination_bank_dropdown_menu")+"']")));
		
		if(config.getParameterValue("IfBankDestinationFromSearch").toUpperCase().equals("YES")) {
			AndroidElement searchEl = (AndroidElement) adDriver.findElementByXPath("//android.widget.EditText");
			searchEl.click();
			searchEl.sendKeys(config.getParameterValue("DestinationBank"));
			if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
			
			AndroidElement bankEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.elementToBeClickable(By.xpath("//android.view.View[@content-desc='"+ config.getParameterValue("DestinationBank")+"']")));
			bankEl.click();
		}
		else {
			//select scrollable bank list
			AndroidElement listBankEl = (AndroidElement) adDriver.findElementByClassName("android.widget.ScrollView");
			
			if(!CommonTest.selectElementByScroll(adDriver, listBankEl, config.getParameterValue("DestinationBank"), 8)) {
				throw new NoSuchFieldException("Bank destination not found");
			}
			
		}
		

	
		//Select destination account
		AndroidElement accountToEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(accountToXPath)));
		accountToEl.click();
		accountToEl.sendKeys(config.getParameterValue("AccountTo"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//select amount
		AndroidElement amountEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(amountXPath)));
		amountEl.click();
		amountEl.sendKeys(config.getParameterValue("Amount"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//select berita
		AndroidElement beritaEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(beritaXPath)));
		beritaEl.click();
		beritaEl.sendKeys(config.getParameterValue("Berita"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		//reff
		AndroidElement reffEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.elementToBeClickable(By.xpath(reffXPath)));
		reffEl.click();
		reffEl.sendKeys(config.getParameterValue("Reff"));
		if(((HasOnScreenKeyboard) adDriver).isKeyboardShown()) adDriver.hideKeyboard();
		
		switch(config.getParameterValue("Frequency")) {
			case "Onetime" :
				break;
				
			case "Weekly" :
				break;
			case "Monthly" :
				break;
			
			default:
				break;
		}

		
		adDriver.findElementByXPath(transferConfirmationXPath).click();
		
	}
	
}
