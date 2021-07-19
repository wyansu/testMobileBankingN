package testMobileBankingN.test;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import io.appium.java_client.TouchAction;

public class CommonTest {
	

	/*
	 * Prform login to app
	 * All screen until Beranda
	 * **/
	public static void login(String username, String password, AndroidDriver driver) throws Exception {
		
		final String otpXpath = "//android.view.View[@content-desc='Masukkan kode Aktivasi yang telah dikirimkan ke nomor ']//following-sibling::android.view.View";
		
		//wait for Login button
		AndroidElement el = (AndroidElement) new WebDriverWait(driver, 30).until(
				ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Log in")));
		el.click();
		
		//wait for login form ready
		AndroidElement form = (AndroidElement) new WebDriverWait(driver, 30).until(
				ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
		
		List<AndroidElement> els = driver.findElementsByClassName("android.widget.EditText");
		
		els.get(0).click();
		els.get(0).sendKeys(username);
		
		els.get(1).click();
		els.get(1).sendKeys(password);
		if(((HasOnScreenKeyboard) driver).isKeyboardShown()) driver.hideKeyboard();
		
		AndroidElement login = (AndroidElement) new WebDriverWait(driver, 30).until(
				ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("MASUK")));
		login.click();
		
		//wait for OTP screen ready
		AndroidElement otp = (AndroidElement) new WebDriverWait(driver, 30).until(
				ExpectedConditions.elementToBeClickable(By.xpath(otpXpath)));
		
		TouchAction action = new TouchAction(driver);
		action.tap(ElementOption.element(otp,10,10));
		action.perform();
		
		//please dont repeat me
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_0));
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_1));
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_2));
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_3));
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_4));
		driver.pressKey(new KeyEvent(AndroidKey.DIGIT_5));

		
		AndroidElement selanjutnya = (AndroidElement) new WebDriverWait(driver, 30).until(
				ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Selanjutnya")));

		selanjutnya.click();
		
		//always end test with verifying end screen state, this enable us to get clean screenshot when needed at the end of the test
		AndroidElement myAccount = (AndroidElement) new WebDriverWait(driver, 30).until(
				ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Rekening Saya")));
	}


	
	
	public static String getOTPFromNotification(AndroidDriver adDriver) {

		String smsFromBTPNSXpath = "//android.widget.TextView[@resource-id='android:id/conversation_text' and contains(@text, 'BTPNSYARIAH')]//parent::*[self::android.widget.LinearLayout]//following-sibling::*//android.widget.TextView[@resource-id='android:id/message_text']";
		
		adDriver.openNotifications();


		List<WebElement> smsFrom  = new WebDriverWait(adDriver, 30).until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(smsFromBTPNSXpath)));
		

		
		String latestSms = smsFrom.get(smsFrom.size() -1).getText();
		Pattern sixDigit = Pattern.compile("[\\d]{6}");
		Matcher match = sixDigit.matcher(latestSms);
		
		String otp = match.find()?match.group():"000000";
		
		//close notif
		adDriver.pressKey(new KeyEvent(AndroidKey.BACK));
		
		return otp;
	}
	
	
	public static void inputPin(AndroidDriver adDriver, String id, String pin) {
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

public static void keyNum(AndroidDriver adDriver,String text) {
	char[] keys = text.toCharArray();
	
	for(char key: keys) {
		String num = "DIGIT_" + key;
		adDriver.pressKey(new KeyEvent(AndroidKey.valueOf(num)));
	}
}

public static void clearNotif(AndroidDriver adDriver) {
	String clearButtonId = "com.android.systemui:id/clear_all";
	adDriver.openNotifications();
	try {
		AndroidElement clearButton  = (AndroidElement) new WebDriverWait(adDriver, 5).until(
			ExpectedConditions.elementToBeClickable(By.id(clearButtonId)));
		
		clearButton.click();
	} catch(Exception e) {
		adDriver.pressKey(new KeyEvent(AndroidKey.BACK));
	}
}

public static void sleep(AndroidDriver adDriver, int milis) {
	
	int inSecond = milis/1000;
	
	for(int i =0; i< inSecond; i++) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		//hit the driver to avoid session hangup
		adDriver.getOrientation();
	}
}

public static boolean needPin(AndroidDriver adDriver) {
	//check if there is screen PIN
	boolean need = false;
	String pinScreenXPath = "//android.view.View[@content-desc='Masukkan MPIN Anda']";
	
	try {
		AndroidElement inputDateEl  = (AndroidElement) new WebDriverWait(adDriver, 10).until(
			ExpectedConditions.presenceOfElementLocated (By.xpath(pinScreenXPath)));
		
		need = true;
	}catch(Exception e) {
		//may be not
	}
	
	return need;	
}

public static boolean backToHome(AndroidDriver adDriver) {
	
	boolean atHome = false;
	//take best effort back to home
	//if transaction stop because of message close the message, next try click button back until home is found
	
	String messagePopupXPath = "//android.view.View[@content-desc='Message']";
	String oKButtonXPath = "//*[contains(@content-desc, 'OK')" +
			 "or contains(@content-desc, 'Ok')][1]";
	
	String cancelButtonXPath = "//*[contains(@content-desc, 'BATAL')" +
			"or contains(@content-desc, 'Cancel')" +
			 "or contains(@content-desc, 'CANCEL')" +
			 "or contains(@content-desc, 'Batal')" +
			"][1]";
	
	String buttonBackXPath = "//android.view.View[contains(@content-desc,'Transfer') or contains(@content-desc, 'Overbooking') or contains(@content-desc, 'SKN') or contains(@content-desc, 'RTGS') or contains(@content-desc, 'Bank')]//preceding::*[1][self::android.widget.Button]";
	String tabPagingXPath ="//android.view.View[@clickable=\"true\"]";
	
	
	try {
		//try to click OK
		AndroidElement messageButton  = (AndroidElement) new WebDriverWait(adDriver, 2).until(
				ExpectedConditions.elementToBeClickable(By.xpath(oKButtonXPath)));
		messageButton.click();
	} catch(Exception e) {
		
	}
	
	try {
		//try to click Cancel
		AndroidElement messageButton  = (AndroidElement) new WebDriverWait(adDriver, 2).until(
				ExpectedConditions.elementToBeClickable(By.xpath(cancelButtonXPath)));
		messageButton.click();
	} catch(Exception e) {
		
	}
	
	
	
	try {
		while(true) {
			AndroidElement backButton  = (AndroidElement) new WebDriverWait(adDriver, 2).until(
					ExpectedConditions.elementToBeClickable(By.xpath(buttonBackXPath)));
			backButton.click();
		}
	}catch(Exception e) {
		//may be we have arrived at home
		
	}
	
	try {
		AndroidElement bankHome  = (AndroidElement) new WebDriverWait(adDriver, 2).until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.View[contains(@content-desc,\"Assalamu'alaikum\")]")));
		atHome = true;
	}catch(Exception e) {
		//we fail
	}
	
	return atHome;
	
}

public static boolean isAtHome(AndroidDriver adDriver) {
	
	boolean atHome = false;
	
	try {
		AndroidElement bankHome  = (AndroidElement) new WebDriverWait(adDriver, 2).until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.View[contains(@content-desc,\"Assalamu'alaikum\")]")));
		
		atHome = true;
	}catch(Exception e) {

	}
	
	return atHome;
}

/**
 * THis method search from scrollable el in view with content-desc is name. Scroll down is default.
 * 	scroll performed by, starting touch from bottom element with offset from element dimension OFFSET_EDGE
 *	return false when method failed to find name in element
 * */

public static boolean selectElementByScroll(AndroidDriver adDriver, AndroidElement el, String name, int maxTryToScroll) {

	
	final int OFFSET_EDGE = 30;

	Rectangle rec = el.getRect();
	int scrollCount=0;
	
	while(scrollCount < maxTryToScroll) {
	
		try {	
		
			AndroidElement targetEl = (AndroidElement) new WebDriverWait(adDriver, 2).until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.View[@content-desc='"+ name+"']")));
			targetEl.click();
			return true;
		
		} catch(Exception e) {
						
		}
		
		TouchAction action = new TouchAction(adDriver);
		action.press(PointOption.point( rec.getX() + rec.getWidth()/2, rec.getY() + rec.getHeight()-OFFSET_EDGE))
			.waitAction(WaitOptions.waitOptions(Duration.ofMillis(8000)))
				.moveTo(PointOption.point(rec.getX() + rec.getWidth()/2, rec.getY()+OFFSET_EDGE))
				.release();
		action.perform();
		scrollCount++;	
	}
	
	return false;
	
}



/**
 * Select date from calendar widget, transferDate format dd/mm/yyy
 * */

public static void selectDateFromCalendar(AndroidDriver adDriver, String transferDate, Locale locale) {
	
	//String transferDate = "18/01/2022";
	

	
	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	DateTimeFormatter calendarFormat;
	
	if(locale.getCountry() == "ID") {
		calendarFormat = DateTimeFormatter.ofPattern("dd, EEEE, dd LLLL yyyy");
		calendarFormat = calendarFormat.withLocale(locale);
	}
	else {
		calendarFormat = DateTimeFormatter.ofPattern("dd, EEEE, LLLL dd, yyyy");
	}
	DateTimeFormatter yearCalendarFormat = DateTimeFormatter.ofPattern("yyyy");
	LocalDate localDate = LocalDate.now();
	LocalDate transferDateInLocalDate = LocalDate.parse(transferDate, dateFormat);
	
	int yearDiff = transferDateInLocalDate.getYear() - localDate.getYear();
	
	if(yearDiff ==0 ) {
		
	}else if(yearDiff > 0) {
		adDriver.findElementByXPath("//android.widget.Button[contains(@content-desc,'Select year') or contains(@content-desc,'Pilih tahun')]").click();
		
		AndroidElement targetEl = (AndroidElement) new WebDriverWait(adDriver, 5).until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.View[@content-desc='"+transferDateInLocalDate.format(yearCalendarFormat) +"']")));
		targetEl.click();
		
		//adDriver.findElementByXPath("//android.widget.Button[@content-desc='OK']").click();
		
	}else if(yearDiff < 0) {
		//do nothing
		
	}
	
	int monthDiff = transferDateInLocalDate.getMonthValue() - localDate.getMonthValue();
	if(monthDiff == 0) {
		AndroidElement targetEl = (AndroidElement) new WebDriverWait(adDriver, 5).until(
				ExpectedConditions.elementToBeClickable(By.xpath("//android.view.View[@content-desc='"+ transferDateInLocalDate.format(calendarFormat) +"']")));
		targetEl.click();
	}
	else if(monthDiff > 0) {
		for(int i =0; i< monthDiff; i++) {
			AndroidElement targetEl = (AndroidElement) new WebDriverWait(adDriver, 5).until(
					ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.Button[contains(@content-desc,'Next month') or contains(@content-desc,'Bulan berikutnya')]")));
			targetEl.click();
			
		}
		
		AndroidElement targetEl = (AndroidElement) new WebDriverWait(adDriver, 5).until(
				ExpectedConditions.elementToBeClickable(By.xpath("//android.view.View[@content-desc='"+ transferDateInLocalDate.format(calendarFormat) +"']")));
		targetEl.click();
		
		
	} else {
		for(int i =monthDiff; i< 0 ; i++) {
			AndroidElement targetEl = (AndroidElement) new WebDriverWait(adDriver, 5).until(
					ExpectedConditions.elementToBeClickable(By.xpath("//android.view.View[contains(@content-desc,'SELECT DATE') or contains(@content-desc,'PILIH TANGGAL')]/android.widget.Button[3]")));
			targetEl.click();
			
		}
		
		AndroidElement targetEl = (AndroidElement) new WebDriverWait(adDriver, 5).until(
				ExpectedConditions.elementToBeClickable(By.xpath("//android.view.View[@content-desc='"+ transferDateInLocalDate.format(calendarFormat) +"']")));
		targetEl.click();
	}
	
		adDriver.findElementByXPath("//android.widget.Button[contains(@content-desc,'OK') or contains(@content-desc,'OKE')]").click();
}


	public static void seekDateFromCalendar(AndroidDriver adDriver, String transferDate, MBTestConfiguration config, Locale locale) {
		String datetoSeek = transferDate;
		//Locale locale = new Locale("id", "ID");
		
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

}
