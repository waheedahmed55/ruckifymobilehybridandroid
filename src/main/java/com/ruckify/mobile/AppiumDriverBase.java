package com.ruckify.mobile;

import java.io.BufferedReader;

/**
 * @author Waheed Ahmed
 * @company Ruckify
 * Oct 01, 2019
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

import com.ruckify.mobile.locators.RentPageLocators;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;

import org.apache.commons.lang3.text.WordUtils;

@SuppressWarnings("deprecation")
public class AppiumDriverBase {

	private AppiumDriver driver;
	private SoftAssert softAssert;
	private Assertion hardAssert;
	protected WebDriverWait wait;
	String platformName = System.getProperty("platformName");
	String formattedDate = null;
	int difference = 0;

	public AppiumDriverBase() {

		// relative path to apk file
		final File appDir = new File("C:\\Automation\\Appium\\ruckify.mobile\\App");
		final File app = new File(appDir, "RC 3-0-18 (7) 32bit.apk");

		/*
		 * Runtime runtime = Runtime.getRuntime(); try { //
		 * runtime.exec("cmd.exe /c start cmd.exe /k \"taskkill /PID 4723 /F");
		 * runtime.exec(
		 * "cmd.exe /c start cmd.exe /k \"appium -a 127.0.0.1 -p 4732 --session-override -dc \"{\"\"noReset\"\": \"\"false\"\"}\"\""
		 * ); Thread.sleep(10000); } catch (IOException | InterruptedException e) {
		 * e.printStackTrace(); }
		 */

		/**
		 * Desired capability
		 */
		try {
			DesiredCapabilities caps = new DesiredCapabilities();
			if (platformName.equals("Android")) {

				caps.setCapability("platform", platformName);
				caps.setCapability("platformVersion", "7.0");
				caps.setCapability("deviceName", "02157df29091da1d");
				caps.setCapability("app", app.getAbsolutePath());
				caps.setCapability("clearSystemFiles", true);
				caps.setCapability("newCommandTimeout", 30);
				caps.setCapability("automationName", "UiAutomator2");
				caps.setCapability("fullReset", true);

				/*
				 * caps.setCapability("platform", platformName);
				 * caps.setCapability("platformVersion", "9.0");
				 * caps.setCapability("deviceName", "4b4a31335a383498");
				 * caps.setCapability("app", app.getAbsolutePath());
				 * caps.setCapability("clearSystemFiles", true);
				 * caps.setCapability("newCommandTimeout", 30);
				 * caps.setCapability("automationName", "UiAutomator2");
				 * caps.setCapability("fullReset", true);
				 * 
				 * caps.setCapability("platform", platformName);
				 * caps.setCapability("platformVersion", "9.0");
				 * caps.setCapability("deviceName", "Galaxy MI A3"); caps.setCapability("app",
				 * app.getAbsolutePath()); caps.setCapability("clearSystemFiles", true);
				 * caps.setCapability("newCommandTimeout", 30);
				 * caps.setCapability("automationName", "UiAutomator2");
				 * caps.setCapability("fullReset", true);
				 */

				// Initializing driver object
				driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);

				if (((AndroidDriver) driver).isDeviceLocked()) {
					((AndroidDriver) driver).unlockDevice();
				}
			}

			else if (platformName.equals("iOS")) {
				caps.setCapability("deviceName", "iPhone X");
				caps.setCapability("platform", platformName);
				caps.setCapability("platformVersion", "11.3");
				caps.setCapability("deviceName", "iOS");
				caps.setCapability("app", app.getAbsolutePath());
				caps.setCapability("clearSystemFiles", true);
				caps.setCapability("newCommandTimeout", 15);
				caps.setCapability("automationName", "UiAutomator2");

				// Initializing driver object
				driver = new IOSDriver(new URL("http://127.0.0.1:4728/wd/hub"), caps);
			}

			this.wait = new WebDriverWait(this.driver, 30);

			// init
			softAssert = new SoftAssert();
			hardAssert = new Assertion();
		} catch (Exception e) {
		}
	}

	/**
	 * Wait For an Element By ID
	 * 
	 * @param element
	 * @throws IOException
	 */
	public boolean waitElementByID(String element) throws IOException {
		try {
			this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element)));
		} catch (Exception e) {
			Reporter.log("Element is not found in the page " + element + " Exception " + e, true);
			getScreenshot(element);
			return false;
		}
		return true;
	}

	/**
	 * Wait For an Element By XPath
	 * 
	 * @param element
	 * @throws IOException
	 */
	public boolean waitElementByXPath(String element) throws IOException {
		try {
			this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
		} catch (Exception e) {
			Reporter.log("Element is not found in the page " + element + " Exception " + e, true);
			getScreenshot(element);
			return false;
		}
		return true;
	}

	/**
	 * Click Button By ID
	 * 
	 * @param element
	 * @throws IOException
	 */
	public void clickBtnByID(String element) throws IOException {
		try {
			driver.findElementById(element).click();
		} catch (Exception e) {
			Reporter.log("Unable to click the element in the page " + element + " Exception " + e, true);
			getScreenshot(element);
		}
	}

	/**
	 * Click Button By XPath
	 * 
	 * @param element
	 * @throws IOException
	 */
	public void clickBtnByXPath(String element) throws IOException {
		try {
			driver.findElementByXPath(element).click();
		} catch (Exception e) {
			Reporter.log("Unable to click the element in the page: " + element + " Exception: " + e, true);
			getScreenshot(element);
		}
	}

	/**
	 * Send Text By ID
	 * 
	 * @param element
	 * @param text
	 * @throws IOException
	 */
	public void sendTextByID(String element, String text) throws IOException {
		try {
			driver.findElementById(element).sendKeys(text);
		} catch (Exception e) {
			Reporter.log("Unable to send the text: " + element + " Exception: " + e, true);
			getScreenshot(element);
		}
	}

	/**
	 * Send Text By XPath
	 * 
	 * @param element
	 * @param text
	 * @throws IOException
	 */
	public void sendTextByXPath(String element, String text) throws IOException {
		try {
			driver.findElementByXPath(element).sendKeys(text);
		} catch (Exception e) {
			Reporter.log("Unable to send the text: " + element + " Exception: " + e, true);
			getScreenshot(element);
		}
	}

	/**
	 * Tap an element by xpath
	 * 
	 * @param element
	 * @throws IOException
	 */
	public void tapElementByXPath(String element) throws IOException {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(By.xpath(element)));
			action.click();
			action.perform();
		} catch (Exception e) {
			Reporter.log("Unable to tap the element by xpath: " + element + "Exception: " + e);
			getScreenshot(element);
		}
	}

	public void doubleTap(String locator) throws InterruptedException {
		Thread.sleep(2000);
		// driver.findElementByAccessibilityId(ele).click();
		MobileElement element = (MobileElement) new WebDriverWait(driver, 30)
				.until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId(locator)));
		Thread.sleep(1000);
		Point source = element.getCenter();
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
		Sequence tap = new Sequence(finger, 1);
		tap.addAction(
				finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), source.x, source.y));
		tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		tap.addAction(new Pause(finger, Duration.ofMillis(200)));
		tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
		tap.addAction(new Pause(finger, Duration.ofMillis(40)));
		tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		tap.addAction(new Pause(finger, Duration.ofMillis(200)));
		tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
		driver.perform(Arrays.asList(tap));
		Thread.sleep(2000);
	}

	public void singleTap(String locator) throws InterruptedException {
		Thread.sleep(2000);
		// driver.findElementByAccessibilityId(ele).click();
		MobileElement element = (MobileElement) new WebDriverWait(driver, 30)
				.until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId(locator)));
		Thread.sleep(1000);
		Point source = element.getCenter();
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
		Sequence tap = new Sequence(finger, 1);
		tap.addAction(
				finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), source.x, source.y));
		tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		tap.addAction(new Pause(finger, Duration.ofMillis(200)));
		tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
		driver.perform(Arrays.asList(tap));
		Thread.sleep(2000);
	}

	public void longPress() throws InterruptedException {

		Thread.sleep(5000);
		driver.findElementByAccessibilityId("sign out").click();
		MobileElement longpress = (MobileElement) new WebDriverWait(driver, 30)
				.until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("sign out")));
		new Actions(driver).clickAndHold(longpress).perform();
		Thread.sleep(5000);
	}

	/**
	 * Tap an element by ID
	 * 
	 * @param element
	 * @throws IOException
	 */
	public void tapElementByID(String element) throws IOException {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(By.id(element)));
			action.click();
			action.perform();
		} catch (Exception e) {
			Reporter.log("Unable to tap the element by id: " + element + "Exception: " + e);
			getScreenshot(element);
		}
	}

	/**
	 * Tap By Coordinates By Locating Nearby Element
	 * 
	 * @param nearbyElmt
	 * @param x
	 * @param y
	 */
	public void tapByCoordinatesByLocatingNearbyElement(String nearbyElmt, int x, int y) {
		MobileElement element = (MobileElement) driver.findElementByAccessibilityId(nearbyElmt);
		Point location = element.getLocation();
		System.out.println(location);
		TouchAction touchAction = new TouchAction(driver);
		touchAction.tap(PointOption.point(location.x + x, location.y + y)).perform();
	}

	/**
	 * Tap By Coordinates By Locating Nearby Element
	 * 
	 * @param x
	 * @param y
	 */
	public void tapByCoordinates(int x, int y) {
		TouchAction touchAction = new TouchAction(driver);
		touchAction.tap(PointOption.point(x, y)).perform();
	}

	/**
	 * Press back button
	 */
	public void backButton() {
		driver.navigate().back();
	}

	/**
	 * Press enter button
	 * 
	 * @throws InterruptedException
	 */
	public void enterButton() throws InterruptedException {
		((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
		Thread.sleep(1500);
	}

	/**
	 * Refresh the page content
	 */
	public void refreshPage() {
		driver.getPageSource();
	}

	/**
	 * Generates Username and append time stamp (yyyyMMddHHmmss) with it for signup
	 * purpose
	 * 
	 * @return username
	 */
	public String generateUsername() {
		String username = "test" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "@ruckify.com";
		// System.out.println(username);
		return username;
	}

	/**
	 * Press hide keyboard
	 */
	public void hideKeyboard() {
		driver.hideKeyboard(); // Works with Android only
	}

	/**
	 * Get the screenshot
	 * 
	 * @param name
	 * @throws IOException
	 */
	public void getScreenshot(String name) throws IOException {
		String date = getDate();
		name = name.replace("'", "");
		name = name.replace(" ", "");
		name = name.replace("\"", "");
		name = name.replace("[", "");
		name = name.replace("]", "");
		name += "_" + date;
		File scrFile = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
		File newFile = null;
		try {
			FileUtils.copyFile(scrFile,
					newFile = new File(System.getProperty("user.dir") + "/target/surefire-reports/" + name + ".png"));
			Reporter.log("<a href='" + newFile + "'> " + "<img src='" + newFile + "' height='250' width='175'/> </a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the current date (Screenshot dependent)
	 */
	private String getDate() {
		int day, month, year;
		int second, minute, hour;
		GregorianCalendar date = new GregorianCalendar();

		day = date.get(Calendar.DAY_OF_MONTH);
		month = date.get(Calendar.MONTH);
		year = date.get(Calendar.YEAR);
		second = date.get(Calendar.SECOND);
		minute = date.get(Calendar.MINUTE);
		hour = date.get(Calendar.HOUR);

		String dateTime = +day + "_" + (month + 1) + "_" + year + "_" + hour + "_" + minute + "_" + second;

		return dateTime;
	}

	/**
	 * Get text by xpath
	 * 
	 * @param element
	 */
	public String getTextByXPath(String element) {
		return driver.findElement(By.xpath(element)).getText();
	}

	/**
	 * Get the current date to build the xpath
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public String getCurrentDate(Integer headsupCount) throws IOException, InterruptedException {
		LocalDate date = LocalDate.now();
		int i = headsupCount / 24;
		difference = checkDate(headsupCount);
		int date1 = date.plusDays(i + difference).getDayOfMonth();
		String day = date.plusDays(i + difference).getDayOfWeek().toString();
		String month = date.getMonth().toString();
		String formatedDate = null;
		if (i > 0) {
			if (difference > 0)
				month = date.plusMonths(1).getMonth().toString();
			formatedDate = WordUtils.capitalizeFully(day) + ", " + WordUtils.capitalizeFully(month) + " " + date1 + " ";
		} else
			formatedDate = "Today" + ", " + WordUtils.capitalizeFully(day) + ", " + ""
					+ WordUtils.capitalizeFully(month) + " " + date1 + " ";
		return formatedDate;
	}

	/**
	 * Get the next available date
	 * 
	 * @param path
	 * @param attribute - enabled/selected
	 */
	public String nextAvailableDate(String path, String attribute, Integer headsupCount, String day)
			throws IOException, InterruptedException {
		String newPath = null;
		boolean flag = false;
		String checkEnabled = null;
		try {
			for (int i = 0; i < 10; i++) {
				if (difference > 0)
					flag = true;
				else
					flag = false;
				if (day.equals("startdate")) {
					newPath = getModifiedDatePath(path, difference + i, flag, headsupCount);
					checkEnabled = getAttribute(newPath, attribute);
					System.out.println("Path: " + newPath + "Status: " + checkEnabled);
				} else if (day.equals("enddate"))
					checkEnabled = getAttribute(path, attribute);
				if (checkEnabled.equals("true") && attribute.equals("enabled"))
					break;
				else if (checkEnabled.equals("true") && attribute.equals("selected")) {
					if (difference > 0)
						flag = true;
					newPath = getModifiedDatePath(RentPageLocators.SET_DATE, difference + i + 1, flag, headsupCount);
					checkEnabled = getAttribute(newPath, "enabled");
					if (checkEnabled.equals("true"))
						break;
					else {
						for(int k= 2; k < 10; k++) {
						newPath = getModifiedDatePath(path, difference + i + k, flag, headsupCount);
						checkEnabled = getAttribute(newPath, "enabled");
						if (checkEnabled.equals("true"))
							break;
						else
							continue;
						}
						break;
					}
				} else
					continue;
			}
		} catch (Exception e) {
			Reporter.log("Unable to get the next available date");
			getScreenshot("NXT_Date");
		}
		return newPath;
	}

	public int checkDate(int headsupCount) throws IOException, InterruptedException {
		boolean flag = false;
		int difference = 0;
		int i = headsupCount / 24;
		LocalDate date = LocalDate.now();
		int date1 = date.getDayOfMonth();
		if (date1 + i >= 25) {
			difference = 31 - date1;
			difference++;
			if ((difference) > 0) {
				Thread.sleep(5000);
				refreshPage();
				Thread.sleep(5000);
				tapByCoordinates(0, 0);
				flag = waitElementByXPath(RentPageLocators.NEXT_MONTH_BTN);
				if (flag)
					tapElementByXPath(RentPageLocators.NEXT_MONTH_BTN);
				Thread.sleep(5000);
				refreshPage();
			}
		}
		return difference;
	}

	/**
	 * Get attribute of an element by xpath
	 * 
	 * @param path
	 * @param i    - nth element
	 * @throws IOException
	 */
	public String getModifiedDatePath(String path, int i, boolean flag, Integer headsupCount) throws IOException {
		LocalDate date = LocalDate.now();
		String newPath = null;
		String month = null;
		int j = headsupCount / 24;
		int date1 = date.plusDays(i + j).getDayOfMonth();
		String day = date.plusDays(i + j).getDayOfWeek().toString();
		if (flag)
			month = date.plusMonths(1).getMonth().toString();
		else
			month = date.getMonth().toString();
		formattedDate = WordUtils.capitalizeFully(day) + ", " + WordUtils.capitalizeFully(month) + " " + date1 + " ";
		newPath = path + "'" + formattedDate + "']";
		System.out.println(newPath);
		return newPath;
	}

	/**
	 * Get attribute of an element by xpath
	 * 
	 * @param element
	 * @param attribute
	 */
	public String getAttribute(String element, String attribute) throws InterruptedException, IOException {
		boolean flag = false;
		String status = null;
		try {
			flag = waitElementByXPath(element);
			Thread.sleep(5000);
			if (flag && attribute.equals("enabled"))
				status = driver.findElement(By.xpath(element)).getAttribute("enabled");
			if (flag && attribute.equals("selected"))
				status = driver.findElement(By.xpath(element)).getAttribute("selected");
		} catch (Exception e) {
			Reporter.log("Unable to get the attribute for the element: " + element);
			getScreenshot("Get_Time");
		}
		return status;
	}

	/**
	 * Get attribute of an element by xpath
	 * 
	 * @param element
	 * @param attribute
	 */
	public String getAttributeXPath(String element, String attribute) throws InterruptedException, IOException {
		boolean flag = false;
		String status = null;
		try {
			flag = waitElementByXPath(element);
			Thread.sleep(5000);
			if (flag)
				status = driver.findElement(By.xpath(element)).getAttribute(attribute);
		} catch (Exception e) {
			Reporter.log("Unable to get the attribute for the element: " + element);
			getScreenshot("Get_Time");
		}
		return status;
	}

	/**
	 * Get the modified time path(xpath)
	 * 
	 * @param status
	 * @throws IOException
	 */
	public String getTime(String validateDate, String status) throws IOException {
		String time = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String[] currentTime = dateFormat.format(cal.getTime()).split(":");
			int hour = Integer.parseInt(currentTime[0]);
			String hourSTR = null;
			if (hour > 12) {
				hour = hour - 12;
				++hour;
				if (validateDate.equals("enddate"))
					hour = hour + 2;
				if (hour < 10)
					hourSTR = "0" + hour;
				else
					hourSTR = "" + hour;
				System.out.println(hourSTR);
				if (status.equals("sameDay") && validateDate.equals("startdate"))
					time = hourSTR + ":00 pm']";
				else if (status.equals("sameDay") && validateDate.equals("enddate"))
					time = hourSTR + ":00 pm']";
				else if (status.equals("nextDay") && validateDate.equals("startdate"))
					time = hourSTR + ":00 pm']";
				else if (status.equals("nextDay") && validateDate.equals("enddate"))
					time = hourSTR + ":00 pm']";
			} else {
				if (status.equals("sameDay") && validateDate.equals("startdate"))
					time = "12:00 pm']";
				else if (status.equals("sameDay") && validateDate.equals("enddate"))
					time = "02:00 pm']";
				else if (status.equals("nextDay") && validateDate.equals("startdate"))
					time = "02:00 pm']";
				else if (status.equals("nextDay") && validateDate.equals("enddate"))
					time = "02:00 pm']";
			}
		} catch (Exception e) {
			Reporter.log("Unable to get the time");
			getScreenshot("Get_Time");
		}
		return time;
	}

	/**
	 * Click the following sibling's in xpath
	 * 
	 * @param path
	 */
	public boolean clickFollowingSibling(String path) {
		boolean flag = false;
		try {
			flag = waitElementByXPath(path + "/following-sibling::*");
			if (flag) {
				Thread.sleep(1500);
				driver.findElement(By.xpath(path + "/following-sibling::*")).click();
			}
		} catch (Exception e) {
			Reporter.log("Unable to click the next element: " + path);
		}
		return flag;
	}

	/**
	 * Scroll Down With An Element
	 * 
	 * @param locator
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void scrollDownWithAnElement(String locator) throws InterruptedException, IOException {
		try {
			Thread.sleep(2000);
			MobileElement slider = (MobileElement) driver.findElementByAccessibilityId(locator);
			Point source = slider.getCenter();
			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence dragNDrop = new Sequence(finger, 1);
			dragNDrop.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(),
					source.getX() / 2, source.getY() + 400));
			dragNDrop.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
			dragNDrop.addAction(finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(),
					source.getX() / 2, source.getY() / 2));
			dragNDrop.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
			driver.perform(Arrays.asList(dragNDrop));
		} catch (Exception e) {
			Reporter.log("Unable to go to the element" + locator);
			getScreenshot(locator);
		}
	}

	/**
	 * Scroll Down With the value
	 * 
	 * @param locator
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void scrollDownWithValue(int x, int y) throws InterruptedException, IOException {
		try {
			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence dragNDrop = new Sequence(finger, 1);
			dragNDrop.addAction(
					finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, y + 500));
			dragNDrop.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
			dragNDrop.addAction(
					finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), x, y / 2));
			dragNDrop.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
			driver.perform(Arrays.asList(dragNDrop));
		} catch (Exception e) {
			Reporter.log("Unable to scroll down by value" + x + ' ' + y);
			getScreenshot("ScrollByValue");
		}
	}

	/**
	 * Scroll Up With the value
	 * 
	 * @param locator
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void scrollUpWithValue(int x, int y) throws InterruptedException, IOException {
		try {
			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence dragNDrop = new Sequence(finger, 1);
			dragNDrop.addAction(
					finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, y / 2));
			dragNDrop.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
			dragNDrop.addAction(
					finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), x, y + 500));
			dragNDrop.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
			driver.perform(Arrays.asList(dragNDrop));
		} catch (Exception e) {
			Reporter.log("Unable to scroll down by value" + x + ' ' + y);
			getScreenshot("ScrollByValue");
		}
	}

	/**
	 * Print in TestNG report and in the console log
	 * 
	 * @param string
	 */
	public void log(String string) {
		Reporter.log(string + "<br>");
		System.out.println(string);
	}

	/**
	 * Get Dimension
	 * 
	 * @return resolution/dimension of the screen
	 */
	public Dimension getDimension() {
		Dimension dimension = driver.manage().window().getSize();
		return dimension;
	}

	/**
	 * Quit the driver session
	 */
	public void teardown() {
		driver.quit();

	}

	/**
	 * Stop the Apppium Server Session
	 * 
	 * @throws InterruptedException
	 */
	public void stopServer() throws InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("taskkill /F /IM node.exe");
			runtime.exec("taskkill /F /IM cmd.exe");
			Thread.sleep(10000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Reporter.log("Stop Appium Server...");
	}
}
