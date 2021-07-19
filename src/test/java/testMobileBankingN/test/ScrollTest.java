package testMobileBankingN.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
	public void scrollTest() {
		
		String datetoSeek = "19/10/2021";
		Locale locale = new Locale("id", "ID");
		
		DateTimeFormatter yearOnly = DateTimeFormatter.ofPattern("yyyy", locale);
		DateTimeFormatter monthOnly = DateTimeFormatter.ofPattern("LLLL", locale);
		DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("d", locale);
		
		
		LocalDate dateToSeekInLocalDate = LocalDate.parse(datetoSeek,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String year = dateToSeekInLocalDate.format(yearOnly).toString();
		String month = dateToSeekInLocalDate.format(monthOnly).toString();
		String date = dateToSeekInLocalDate.format(dateOnly).toString();
		
		LocalDate now = LocalDate.now();
		
		int yearDiff = dateToSeekInLocalDate.getYear() - now.getYear();
		if(yearDiff > 0) {
			
			AndroidElement calendarEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.SeekBar[1]")));
			int i =0;
			while(i < yearDiff) {

				
				Rectangle rec = calendarEl.getRect();
				
				TouchAction action = new TouchAction(adDriver);
				action.press(PointOption.point( rec.getX() + rec.getWidth()/2, rec.getY() + (rec.getHeight()/2)+30))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
						.moveTo(PointOption.point(rec.getX() + rec.getWidth()/2, rec.getY() +(rec.getHeight()/2)-30 ))
						.release();
				action.perform();
				
				i++;
				
			}

		}
		else if(yearDiff < 0) {
			throw new IllegalArgumentException("Date is behind from now");
		}
		
		int monthDiff = dateToSeekInLocalDate.getMonthValue() - now.getMonthValue();
		
		if(monthDiff > 0) {
			
			AndroidElement calendarEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.SeekBar[2]")));
			int i =0;
			while(i< monthDiff ) {
				
				Rectangle rec = calendarEl.getRect();
				
				TouchAction action = new TouchAction(adDriver);
				action.press(PointOption.point( rec.getX() + rec.getWidth()/2, rec.getY() + (rec.getHeight()/2)+30))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
						.moveTo(PointOption.point(rec.getX() + rec.getWidth()/2, rec.getY() +(rec.getHeight()/2)-30 ))
						.release();
				action.perform();
				
				i++;
				
			}
		
		}
		else if(yearDiff == 0 && monthDiff < 0) {
			throw new IllegalArgumentException("Date is behind from now");
		}
		
		else if (monthDiff < 0) {
			
			AndroidElement calendarEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.SeekBar[2]")));
			int i =monthDiff;
			while(i< 0 ) {
				
				Rectangle rec = calendarEl.getRect();
				
				TouchAction action = new TouchAction(adDriver);
				action.press(PointOption.point( rec.getX() + rec.getWidth()/2, rec.getY() + (rec.getHeight()/2)-30))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
						.moveTo(PointOption.point(rec.getX() + rec.getWidth()/2, rec.getY() +(rec.getHeight()/2)+30 ))
						.release();
				action.perform();
				
				i++;
				
			}
			
		}
		
		int dateDiff = dateToSeekInLocalDate.getDayOfMonth() - now.getDayOfMonth();
		if(yearDiff==0 && monthDiff==0 && dateDiff<0) {
			throw new IllegalArgumentException("Date is behind from now");
		}
		else if(dateDiff < 0) {
			
			AndroidElement calendarEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.SeekBar[3]")));
			int i =dateDiff;
			while(i< 0 ) {
				
				Rectangle rec = calendarEl.getRect();
				
				TouchAction action = new TouchAction(adDriver);
				action.press(PointOption.point( rec.getX() + rec.getWidth()/2, rec.getY() + (rec.getHeight()/2)-30))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
						.moveTo(PointOption.point(rec.getX() + rec.getWidth()/2, rec.getY() +(rec.getHeight()/2)+30 ))
						.release();
				action.perform();
				
				i++;
				
			}
			
		}else if(dateDiff > 0) {
			
			AndroidElement calendarEl  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.SeekBar[3]")));
			int i =0;
			while(i< dateDiff ) {
				
				Rectangle rec = calendarEl.getRect();
				
				TouchAction action = new TouchAction(adDriver);
				action.press(PointOption.point( rec.getX() + rec.getWidth()/2, rec.getY() + (rec.getHeight()/2)+30))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
						.moveTo(PointOption.point(rec.getX() + rec.getWidth()/2, rec.getY() +(rec.getHeight()/2)-30 ))
						.release();
				action.perform();
				
				i++;
				
			}
			
		}
		
		AndroidElement selectButton  = (AndroidElement) new WebDriverWait(adDriver, config.getElementSearchTimeout()).until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Button[@content-desc='"+ config.getResourceValue("select_new_calendar_button")+"']")));
		selectButton.click();
	}
	
	
	private void selectDateFromCalendar() {
		
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
