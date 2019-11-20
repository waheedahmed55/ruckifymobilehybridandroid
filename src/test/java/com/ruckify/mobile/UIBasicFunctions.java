package com.ruckify.mobile;

/**
 * @author Waheed Ahmed
 * @company Ruckify
 * Oct 01, 2019
 */

import java.io.IOException;

import org.testng.Reporter;

import com.ruckify.mobile.locators.*;

public class UIBasicFunctions {
	private AppiumDriverBase appiumDB;
	private boolean flag;

	public UIBasicFunctions(AppiumDriverBase appiumDB) {
		this.appiumDB = appiumDB;
	}

	/**
	 * Goto login Page
	 * 
	 * @return flag - step definition pass/fail
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean gotoLoginPage(String session) throws InterruptedException, IOException {
		if(session.equals("new")) {
			flag = appiumDB.waitElementByID(LoginPageLocators.ALLOW_BTN);
			if (flag) {
				appiumDB.clickBtnByID(LoginPageLocators.ALLOW_BTN);
				flag = appiumDB.waitElementByXPath(LoginPageLocators.MANAGE_TAB);
			}
		}
		else if(session.equals("old")) {
			flag = appiumDB.waitElementByXPath(LoginPageLocators.MANAGE_TAB);
		}
		if (flag) {
			appiumDB.tapElementByXPath(LoginPageLocators.MANAGE_TAB);
			flag = appiumDB.waitElementByXPath(LoginPageLocators.SIGN_IN_BTN);
			if (flag) {
				appiumDB.clickBtnByXPath(LoginPageLocators.SIGN_IN_BTN);
				flag = appiumDB.waitElementByXPath(LoginPageLocators.EMAIL_ID_TXT);
			}
		}
		return flag;
	}

	/**
	 * Login to the app
	 * 
	 * @return flag - step definition pass/fail
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean login() throws InterruptedException, IOException {
		flag = appiumDB.waitElementByXPath(LoginPageLocators.EMAIL_ID_TXT);
		if (flag) {
			appiumDB.sendTextByXPath(LoginPageLocators.EMAIL_ID_TXT, LoginPageLocators.USERNAME);
			appiumDB.sendTextByXPath(LoginPageLocators.PASSWORD_TXT, LoginPageLocators.PASSWORD);
			flag = appiumDB.waitElementByXPath(LoginPageLocators.LOGIN_BTN);
			if (flag) {
				appiumDB.clickBtnByXPath(LoginPageLocators.LOGIN_BTN);
				flag = appiumDB.waitElementByXPath(LoginPageLocators.SEARCH_ICON);
				if (flag)
					Reporter.log("Successfully logged into the website");
			}
		}
		return flag;
	}
	
	/**
	 * Verify Login from by clicking the rent tab
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean verifyLoginFromRentingTab() throws IOException, InterruptedException {
		flag = appiumDB.waitElementByXPath(HomePageLocators.RENTING_TAB);
		if(flag) {
			appiumDB.tapElementByXPath(HomePageLocators.RENTING_TAB);
			flag = appiumDB.waitElementByXPath(HomePageLocators.SIGNIN_BTN_POP_UP);
			if (flag)
				Reporter.log("Verified sign in button while clicking the Renting tab without logging in to the app"+"<br>");
			if(flag) {
				appiumDB.tapElementByXPath(HomePageLocators.SIGNIN_BTN_POP_UP);
				flag = login();
			}
		}
		return flag;
	}
	
	/**
	 * Verify the Homepage
	 * 
	 * @return boolean
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public boolean verifyHomePage() throws InterruptedException, IOException {
		if (flag) {
			flag = appiumDB.waitElementByXPath(HomePageLocators.MESSAGE_TAB);
			if(flag) {
				appiumDB.tapElementByXPath(HomePageLocators.MESSAGE_TAB);
				flag = appiumDB.waitElementByXPath(HomePageLocators.SIGNIN_BTN_POP_UP);
				if (flag)
					Reporter.log("Verified sign in button while clicking the message tab without logging in to the app"+"<br>");
				if(flag) {
					appiumDB.tapElementByXPath(HomePageLocators.SIGNIN_BTN_POP_UP);
					flag = login();
					if (flag)
						Reporter.log("Logged in to the app from homepage"+ "<br>");
					Thread.sleep(2000);
					appiumDB.tapElementByXPath(HomePageLocators.MESSAGE_TAB);
					if(flag) {
						flag = appiumDB.waitElementByXPath(HomePageLocators.MESSAGE_PAGE_TXT) && 
								appiumDB.waitElementByXPath(HomePageLocators.RENTING_TAB);
						if(flag) {
							Reporter.log("Verified Renting tab in homepage"+"<br>");
							appiumDB.tapElementByXPath(HomePageLocators.RENTING_TAB);
							flag = appiumDB.waitElementByXPath(HomePageLocators.REQUEST_TAB) &&
									appiumDB.waitElementByXPath(HomePageLocators.POSTING_TAB);
							if(flag) {
								Reporter.log("Verified Posting tab in homepage"+"<br>");
								appiumDB.tapElementByXPath(HomePageLocators.POSTING_TAB);
								flag = appiumDB.waitElementByXPath(HomePageLocators.TIMEZONE_TXT) &&
										appiumDB.waitElementByXPath(LoginPageLocators.MANAGE_TAB);
								if (flag)
									Reporter.log("Verified manage tab in homepage");
							}
						}
					}
				}
			}
		}
		return flag;
	}

	/**
	 * Search for a product, select the product and go to rent page
	 * 
	 * @return flag - step definition pass/fail
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean searchAProduct() throws IOException, InterruptedException {
		flag = appiumDB.waitElementByXPath(LoginPageLocators.SEARCH_ICON);
		if (flag) {
			appiumDB.refreshPage();
			Thread.sleep(5000);
			appiumDB.tapElementByXPath(LoginPageLocators.SEARCH_ICON);
			flag = appiumDB.waitElementByXPath(HomePageLocators.SEARCH_TXT);
			if (flag) {
				appiumDB.sendTextByXPath(HomePageLocators.SEARCH_TXT, HomePageLocators.SEARCH_ITEM);
				Thread.sleep(2000);
				appiumDB.enterButton();
				Thread.sleep(5000);
				flag = appiumDB.waitElementByXPath(HomePageLocators.MONITOR_NAME);
				if (flag) {
					appiumDB.clickBtnByXPath(HomePageLocators.MONITOR_NAME);
					Thread.sleep(5000);
					flag = appiumDB.waitElementByXPath(RentPageLocators.RENT_NOW_BTN);
					if (flag)
						Reporter.log("Searched a product and gone to the Rent page");
				}
			}
		}
		return flag;
	}

	/**
	 * Rent a product by selecting the corresponding date and time
	 * 
	 * @return flag - step definition pass/fail
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean rentAProduct(String day) throws IOException, InterruptedException {
		flag = appiumDB.waitElementByXPath(RentPageLocators.RENT_NOW_BTN);
		if (flag) {
			Thread.sleep(2000);
			appiumDB.clickBtnByXPath(RentPageLocators.RENT_NOW_BTN);
			flag = appiumDB.waitElementByXPath(RentPageLocators.START_DATE_CAL);
			if (flag) {
				Thread.sleep(2000);
				appiumDB.tapElementByXPath(RentPageLocators.START_DATE_CAL);
				flag = setDateAndTime(RentPageLocators.SET_DATE, day);
				if (flag)
					Reporter.log("Searched a product and gone to the searched product page");
			}
		}
		return flag;
	}

	/**
	 * Set date and time to checkout the product
	 * 
	 * @param path
	 * @return flag - step definition pass/fail
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean setDateAndTime(String path, String day) throws IOException, InterruptedException {
		String newPath = setDate("startdate", path, day);
		String timePath  = setTime("startdate", newPath, day);
		if (timePath.isBlank())
			flag = false;
		else {
			flag = appiumDB.waitElementByXPath(timePath);
			if (flag) {
				Thread.sleep(5000);
				appiumDB.clickBtnByXPath(timePath);
				flag = appiumDB.waitElementByXPath(RentPageLocators.SET_START_TIME_BTN);
				if (flag) {
					Thread.sleep(5000);
					appiumDB.tapElementByXPath(RentPageLocators.SET_START_TIME_BTN);
					if(day.equals("sameDay")) {
						flag = appiumDB.waitElementByXPath(newPath);
						if(flag) {
							appiumDB.tapElementByXPath(newPath);
							timePath  = setTime("enddate", newPath, day);
						}
					}
					else if(day.equals("nextDay")) {
						newPath = setDate("enddate", path, day);
						timePath  = setTime("enddate", newPath, day);
					}
					System.out.println(timePath);
					flag = appiumDB.waitElementByXPath(timePath);
					if (flag) {
						Thread.sleep(3000);
						appiumDB.tapElementByXPath(timePath);
						Thread.sleep(2000);
						flag = appiumDB.waitElementByXPath(RentPageLocators.SET_END_TIME_BTN);
						if (flag) {
							appiumDB.clickBtnByXPath(RentPageLocators.SET_END_TIME_BTN);
							flag = appiumDB.waitElementByXPath(RentPageLocators.SET_DATES_BTN);
							if (flag) {
								appiumDB.tapElementByXPath(RentPageLocators.SET_DATES_BTN);
								flag = appiumDB.waitElementByXPath(RentPageLocators.CHECKOUT_BTN);
								if (flag)
									Reporter.log("Successfully assigned the start and end dates");
							}
						}
					}
				}
			}
		}
		return flag;
	}

	/**
	 * Set the date by framing the xpath with date and time Get the
	 * attribute(enabled/selected) of the product and select the date accordingly
	 * Get the next available date if the current date is blocked
	 * 
	 * @param validateDate
	 * @param path
	 * @return flag - step definition pass/fail
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String setDate(String validateDate, String path, String day) throws IOException, InterruptedException {
		String date = appiumDB.getCurrentDate();
		String newPath = path + "'" + date + "']";
		String checkStatus = null;
		if (validateDate.equals("startdate"))
			checkStatus = appiumDB.getAttribute(newPath, "enabled");
		else if (validateDate.equals("enddate"))
			checkStatus = appiumDB.getAttribute(newPath, "selected");
		if (checkStatus.equals("true") && validateDate.equals("startdate"))
			return newPath;
		else if (checkStatus.equals("true") && validateDate.equals("enddate")) {
			Thread.sleep(2000);
			newPath = appiumDB.getModifiedDatePath(path, 0);
		} else {
			if (validateDate.equals("startdate"))
				newPath = appiumDB.nextAvailableDate(path, "enabled");
			else if (validateDate.equals("enddate"))
				newPath = appiumDB.nextAvailableDate(path, "selected");
			flag = appiumDB.waitElementByXPath(newPath);
		}
		System.out.println(newPath);
		Thread.sleep(2000);
		appiumDB.tapElementByXPath(newPath);
		return newPath;
	}
	
	/**
	 * Set Time and send the modified xpath
	 * @param validateDate
	 * @param newPath
	 * @param day
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String setTime(String validateDate, String newPath, String day) throws IOException, InterruptedException {
		String timePath = null;
		if(day.equals("sameDay"))
			timePath = RentPageLocators.START_TIME_BTN + appiumDB.getTime(validateDate, "sameDay");
		else if(day.equals("nextDay"))
			timePath = RentPageLocators.START_TIME_BTN + appiumDB.getTime(validateDate, "nextDay");
		return timePath;
	}

	/**
	 * Checkout a product
	 * 
	 * @throws InterruptedException
	 */
	public boolean checkoutAProduct() throws IOException, InterruptedException {
		if (flag) {
			appiumDB.clickBtnByXPath(RentPageLocators.CHECKOUT_BTN);
			flag = appiumDB.waitElementByXPath(RentPageLocators.CONFIRM_CHECKOUT_BTN);
			if (flag) {
				appiumDB.clickBtnByXPath(RentPageLocators.CONFIRM_CHECKOUT_BTN);
				flag = appiumDB.waitElementByXPath(RentPageLocators.ADD_NOTE_TXT);
				if (flag) {
					appiumDB.scrollDownWithAnElement("add note");
					appiumDB.scrollDownWithAnElement("add note");
					appiumDB.scrollDownWithValue(250, 1200);
					Thread.sleep(12000);
					appiumDB.refreshPage();
					flag = appiumDB.waitElementByXPath(RentPageLocators.PAYMENT_BTN);
					if(!flag) {
						appiumDB.refreshPage();
						flag = appiumDB.waitElementByXPath(RentPageLocators.PAYMENT_BTN);
					}
					if(flag) {
						appiumDB.tapElementByXPath(RentPageLocators.PAYMENT_BTN);
						Thread.sleep(5000);
						flag = appiumDB.waitElementByXPath(RentPageLocators.OKAY_BTN);
						if (flag) {
							Thread.sleep(5000);
							appiumDB.tapElementByXPath(RentPageLocators.OKAY_BTN);
							Thread.sleep(5000);
							flag = appiumDB.waitElementByXPath(RentPageLocators.ITINERARY_TXT);
							Thread.sleep(5000);
							appiumDB.scrollDownWithValue(250, 1200);
							appiumDB.scrollUpWithValue(250, 1200);
							flag =  appiumDB.waitElementByXPath(RentPageLocators.ARROW_BACK_BTN);
							if(flag) {
								Thread.sleep(3000);
								appiumDB.doubleTap("arrow back ");
								//appiumDB.tapElementByXPath(RentPageLocators.ARROW_BACK_BTN);
								flag = appiumDB.waitElementByXPath(HomePageLocators.HOME_TAB);
							}
						}
					}
				}
			}
		}
		return flag;
	}

	public boolean verifyRentalPage() throws IOException, InterruptedException {
		flag = appiumDB.waitElementByID(LoginPageLocators.ALLOW_BTN);
		if (flag) {
			appiumDB.clickBtnByID(LoginPageLocators.ALLOW_BTN);
			flag = appiumDB.waitElementByXPath(HomePageLocators.RENT_BTN) &&
					appiumDB.waitElementByXPath(HomePageLocators.POST_BTN);
			if (flag)
				Reporter.log("Verified the Rental market and post button in Homepage"+"<br>");
			if(flag) {
				appiumDB.clickBtnByXPath(HomePageLocators.POST_BTN);
				flag = appiumDB.waitElementByXPath(HomePageLocators.SIGN_IN_BTN);
				if (flag)
					Reporter.log("Verified sign in button in Post page"+"<br>");
				if(flag) {
					appiumDB.scrollDownWithValue(250, 1200);
					flag = appiumDB.waitElementByXPath(HomePageLocators.SIGN_UP_BTN);
					if (flag)
						Reporter.log("Verified sign up button in Post page"+"<br>");
					if(flag) {
						appiumDB.scrollUpWithValue(250, 1200);
						flag = appiumDB.waitElementByXPath(HomePageLocators.RENT_BTN);
						if(flag) {
							appiumDB.clickBtnByXPath(HomePageLocators.RENT_BTN);
							flag = appiumDB.waitElementByXPath(HomePageLocators.SEARCH_ICON);
							if (flag)
								Reporter.log("Verified rental market buuton and came back to homepage"+"<br>");
						}
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * Logout from the App
	 * 
	 * @return boolean
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean logout() throws IOException, InterruptedException{
		appiumDB.tapElementByXPath(LoginPageLocators.MANAGE_TAB);
		flag = appiumDB.waitElementByXPath(HomePageLocators.MY_INVENTORY_BTN);
		if(flag) {
			appiumDB.scrollDownWithValue(250, 1100);
			Thread.sleep(12000);
			appiumDB.scrollDownWithValue(250, 1600);
			appiumDB.refreshPage();
			flag = appiumDB.waitElementByXPath(HomePageLocators.SIGNOUT_BTN);
			if(flag) {
				appiumDB.clickBtnByXPath(HomePageLocators.SIGNOUT_BTN);
				flag = appiumDB.waitElementByXPath(HomePageLocators.OKAY_BTN);
				if(flag) {
					appiumDB.clickBtnByXPath(HomePageLocators.OKAY_BTN);
					flag = appiumDB.waitElementByXPath(HomePageLocators.SEARCH_ICON);
				}
			}
		}
		return flag;
	}
}
