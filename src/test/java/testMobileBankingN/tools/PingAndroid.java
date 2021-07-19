package testMobileBankingN.tools;

import io.appium.java_client.android.AndroidDriver;

/*
 * this class used to ping appium android to avoid appium hangup due to inactive session for more than 60s
 * */
public class PingAndroid extends Thread{
	
	private AndroidDriver adDriver;
	
	public PingAndroid(AndroidDriver adDriver) {
		this.adDriver = adDriver;
	}
	
	public void run() {
		while(true) {
			adDriver.getOrientation();
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
