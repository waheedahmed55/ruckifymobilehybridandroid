package com.ruckify.mobile;

/**
 * @author Waheed Ahmed
 * @company Ruckify
 * Oct 01, 2019
 */

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Dimension;
import org.testng.annotations.BeforeClass;

import com.ruckify.mobile.locators.*;

public class UIBasicFunctions {
	private AppiumDriverBase appiumDB;
	private boolean flag;
	static Integer headsupCount;
	Dimension dimension;

	public UIBasicFunctions(AppiumDriverBase appiumDB) {
		this.appiumDB = appiumDB;
		dimension = appiumDB.getDimension();
		System.out.println("Screen Resolution: " + dimension.height + "x" + dimension.width);
	}

	/**
	 * Goto login Page
	 * 
	 * @return flag - step definition pass/fail
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean gotoLoginPage(String session) throws InterruptedException, IOException {
		if (session.equals("new")) {
			flag = appiumDB.waitElementByID(LoginPageLocators.ALLOW_BTN);
			if (flag) {
				appiumDB.clickBtnByID(LoginPageLocators.ALLOW_BTN);
				flag = appiumDB.waitElementByXPath(LoginPageLocators.MANAGE_TAB);
			}
		} else if (session.equals("old")) {
			flag = appiumDB.waitElementByXPath(LoginPageLocators.MANAGE_TAB);
		}
		if (flag) {
			appiumDB.tapElementByXPath(HomePageLocators.MESSAGE_TAB);
			flag = appiumDB.waitElementByXPath(HomePageLocators.SIGNIN_BTN_POP_UP);
			if (flag) {
				appiumDB.tapElementByXPath(HomePageLocators.SIGNIN_BTN_POP_UP);
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
					appiumDB.log("Successfully logged into the website");
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
		if (flag) {
			appiumDB.tapElementByXPath(HomePageLocators.RENTING_TAB);
			flag = appiumDB.waitElementByXPath(HomePageLocators.SIGNIN_BTN_POP_UP);
			if (flag)
				appiumDB.log("Verified sign in button while clicking the Renting tab without logging in to the app"
						+ "<br>");
			if (flag) {
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
			if (flag) {
				appiumDB.tapElementByXPath(HomePageLocators.MESSAGE_TAB);
				flag = appiumDB.waitElementByXPath(HomePageLocators.SIGNIN_BTN_POP_UP);
				if (flag)
					appiumDB.log("Verified sign in button while clicking the message tab without logging in to the app"
							+ "<br>");
				if (flag) {
					appiumDB.tapElementByXPath(HomePageLocators.SIGNIN_BTN_POP_UP);
					flag = login();
					if (flag)
						appiumDB.log("Logged in to the app from homepage" + "<br>");
					Thread.sleep(2000);
					appiumDB.tapElementByXPath(HomePageLocators.MESSAGE_TAB);
					if (flag) {
						flag = appiumDB.waitElementByXPath(HomePageLocators.MESSAGE_PAGE_TXT)
								&& appiumDB.waitElementByXPath(HomePageLocators.RENTING_TAB);
						if (flag) {
							appiumDB.log("Verified Renting tab in homepage" + "<br>");
							appiumDB.tapElementByXPath(HomePageLocators.RENTING_TAB);
							flag = appiumDB.waitElementByXPath(HomePageLocators.TIMEZONE_TXT)
									&& appiumDB.waitElementByXPath(HomePageLocators.POSTING_TAB);
							if (flag) {
								appiumDB.log("Verified Posting tab in homepage" + "<br>");
								appiumDB.tapElementByXPath(HomePageLocators.POSTING_TAB);
								flag = appiumDB.waitElementByXPath(HomePageLocators.REQUEST_TAB)
										&& appiumDB.waitElementByXPath(LoginPageLocators.MANAGE_TAB);
								if (flag)
									appiumDB.log("Verified manage tab in homepage");
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
	public boolean searchAProduct(boolean filter) throws IOException, InterruptedException {
		String res_id = null;
		flag = appiumDB.waitElementByXPath(LoginPageLocators.SEARCH_ICON);
		if (flag) {
			appiumDB.refreshPage();
			Thread.sleep(5000);
			// appiumDB.tapElementByXPath(LoginPageLocators.SEARCH_ICON);
			appiumDB.singleTap("search ");
			Thread.sleep(2000);
			flag = appiumDB.waitElementByXPath(HomePageLocators.SEARCH_TXT);
			if (flag) {
				appiumDB.sendTextByXPath(HomePageLocators.SEARCH_TXT, HomePageLocators.SEARCH_ITEM);
				Thread.sleep(5000);
				appiumDB.enterButton();
				Thread.sleep(5000);
				flag = appiumDB.waitElementByXPath(HomePageLocators.FIRST_SEARCH_RESULT);
				if (flag && filter)
					flag = verifyFilters();
				Thread.sleep(2000);
				if (flag) {
					Thread.sleep(2000);
					res_id = appiumDB.getAttributeXPath(HomePageLocators.FIRST_SEARCH_RESULT, "resource-id");
					System.out.println("Resource ID " + res_id);
					appiumDB.clickBtnByXPath(HomePageLocators.FIRST_SEARCH_RESULT);
					try {
						headsupCount = REST_CreateSession.getHeadsupHourByAPI(res_id);
					} catch (URISyntaxException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Thread.sleep(3000);
					flag = appiumDB.waitElementByXPath(RentPageLocators.RENT_NOW_BTN);
					if (flag)
						appiumDB.log("Searched a product and gone to the Rent page");
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean verifyFilters() throws IOException, InterruptedException {
		flag = verifyFilter();
		/*
		 * if(flag) { flag = verifyDateFilter(); }
		 */
		return flag;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean verifyFilter() throws IOException, InterruptedException {
		flag = appiumDB.waitElementByXPath(RentPageLocators.FILTER_BTN);
		if (flag) {
			appiumDB.clickBtnByXPath(RentPageLocators.FILTER_BTN);
			flag = appiumDB.waitElementByXPath(RentPageLocators.QUANTITY_BTN);
			if (flag) {
				appiumDB.tapByCoordinates(525, 1240);
				Thread.sleep(2000);
				appiumDB.tapByCoordinates(580, 1793);
				flag = appiumDB.waitElementByXPath(RentPageLocators.DAILY_RADIO_BTN);
				if (flag) {
					appiumDB.clickBtnByXPath(RentPageLocators.DAILY_RADIO_BTN);
					Thread.sleep(1500);
					appiumDB.clickBtnByXPath(RentPageLocators.OK_BTN);
					Thread.sleep(1500);
					appiumDB.clickBtnByXPath(RentPageLocators.LOW_PRICE_LABEL);
					flag = appiumDB.waitElementByXPath(RentPageLocators.LOW_RATE_TXTBOX);
					if (flag) {
						appiumDB.sendTextByXPath(RentPageLocators.LOW_RATE_TXTBOX, "2");
						Thread.sleep(1000);
						appiumDB.clickBtnByXPath(RentPageLocators.DONE_BTN);
						flag = appiumDB.waitElementByXPath(RentPageLocators.HIGH_PRICE_LABEL);
						if (flag) {
							appiumDB.clickBtnByXPath(RentPageLocators.HIGH_PRICE_LABEL);
							flag = appiumDB.waitElementByXPath(RentPageLocators.HIGH_RATE_TXTBOX);
							if (flag) {
								appiumDB.sendTextByXPath(RentPageLocators.HIGH_RATE_TXTBOX, "100");
								Thread.sleep(1000);
								appiumDB.clickBtnByXPath(RentPageLocators.DONE_BTN);
								flag = appiumDB.waitElementByXPath(RentPageLocators.QUANTITY_BTN);
								if (flag) {
									appiumDB.clickBtnByXPath(RentPageLocators.QUANTITY_BTN);
									flag = appiumDB.waitElementByXPath(RentPageLocators.ADD_CIRCLE_ICON);
									if (flag) {
										appiumDB.clickBtnByXPath(RentPageLocators.ADD_CIRCLE_ICON);
										Thread.sleep(1000);
										appiumDB.clickBtnByXPath(RentPageLocators.INSTANT_BOOKING_ONLY);
										Thread.sleep(1000);
										appiumDB.scrollDownWithValue(250, 1200);
										Thread.sleep(1000);
										appiumDB.refreshPage();
										Thread.sleep(3000);
										appiumDB.tapByCoordinates(0, 0);
										appiumDB.clickBtnByXPath(RentPageLocators.DELIVERY_RADIO_BTN);
										Thread.sleep(1000);
										appiumDB.clickBtnByXPath(RentPageLocators.APPLY_FILTER_BTN);
										flag = appiumDB.waitElementByXPath(HomePageLocators.FIRST_SEARCH_RESULT);
									}
								}
							}
						}
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean verifyDateFilter() throws IOException, InterruptedException {
		flag = appiumDB.waitElementByXPath(RentPageLocators.RUCKIFY_CALENDER_FILTER);
		if (flag) {
			Thread.sleep(2000);
			appiumDB.clickBtnByXPath(RentPageLocators.RUCKIFY_CALENDER_FILTER);
			flag = appiumDB.waitElementByXPath(RentPageLocators.EXACT_DAYS_BTN);
			if (flag) {
				appiumDB.tapByCoordinates(1023, 746); // (dimension.height - 900, dimension.width - 330); // 1023 746
				Thread.sleep(2000);
				flag = appiumDB.waitElementByXPath(RentPageLocators.WITHINA_RANGE_BTN);
				if (flag) {
					appiumDB.clickBtnByXPath(RentPageLocators.WITHINA_RANGE_BTN);
					Thread.sleep(1000);
					appiumDB.clickBtnByXPath(RentPageLocators.ADD_CIRCLE_ICON);
					flag = appiumDB.waitElementByXPath(RentPageLocators.APPLY_BTN);
					if (flag) {
						appiumDB.clickBtnByXPath(RentPageLocators.APPLY_BTN);
						flag = appiumDB.waitElementByXPath(HomePageLocators.FIRST_SEARCH_RESULT);
					}
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
		if (!flag) {
			flag = appiumDB.waitElementByXPath(RentPageLocators.REQUEST_RENT_NOW_BTN);
			if (flag) {
				Thread.sleep(2000);
				appiumDB.clickBtnByXPath(RentPageLocators.REQUEST_RENT_NOW_BTN);
				flag = appiumDB.waitElementByXPath(RentPageLocators.START_DATE_CAL);
			}
		} else if (flag) {
			Thread.sleep(2000);
			appiumDB.clickBtnByXPath(RentPageLocators.RENT_NOW_BTN);
			flag = appiumDB.waitElementByXPath(RentPageLocators.START_DATE_CAL);
		}
		if (flag) {
			Thread.sleep(10000);
			appiumDB.tapByCoordinates(0, 0);
			Thread.sleep(1500);
			appiumDB.tapElementByXPath(RentPageLocators.START_DATE_CAL);
			Thread.sleep(1500);
			flag = setDateAndTime(RentPageLocators.SET_DATE, day);
			if (flag)
				appiumDB.log("Searched a product and gone to the searched product page");
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
		String timePath = setTime("startdate", newPath, day);
		if (timePath.isBlank())
			flag = false;
		else {
			/*
			 * flag = appiumDB.waitElementByXPath(timePath);
			 * 
			 * if (!flag) { appiumDB.scrollDownWithValue(100, 250); flag =
			 * appiumDB.waitElementByXPath(timePath); }
			 * 
			 * if (flag) {
			 * 
			 * appiumDB.scrollDownWithValue(150, 500); Thread.sleep(5000);
			 * appiumDB.clickBtnByXPath(timePath);
			 * 
			 * appiumDB.tapByCoordinates(350, 450); Thread.sleep(2000);
			 * appiumDB.tapByCoordinates(350, 450); Thread.sleep(2000);
			 * //appiumDB.clickBtnByXPath(timePath); // Click the Set Start time button
			 */
			Thread.sleep(5000);
			flag = appiumDB.waitElementByXPath(RentPageLocators.SET_START_TIME_BTN); // appiumDB.clickFollowingSibling(newPath);
			if (flag) {
				Thread.sleep(1500);
				appiumDB.tapByCoordinates(700, 750);
				Thread.sleep(1500);
				appiumDB.tapByCoordinates(1040, 1730);
				// appiumDB.clickBtnByXPath(RentPageLocators.SET_START_TIME_BTN);
				// appiumDB.clickBtnByXPath(RentPageLocators.SET_START_TIME_BTN);
				Thread.sleep(1000);
				if (day.equals("sameDay")) {
					flag = appiumDB.waitElementByXPath(newPath);
					if (flag) {
						appiumDB.tapElementByXPath(newPath);
						// timePath = setTime("enddate", newPath, day);
					}
				} else if (day.equals("nextDay")) {
					/*
					 * flag = appiumDB.waitElementByXPath(RentPageLocators.PREVIOUS_MONTH_BTN);
					 * if(flag) appiumDB.tapElementByXPath(RentPageLocators.PREVIOUS_MONTH_BTN);
					 * appiumDB.clickFollowingSibling(newPath);
					 */
					newPath = setDate("enddate", newPath, day);
					Thread.sleep(2000);
					// timePath = setTime("enddate", newPath, day);
					// System.out.println("set end time");
				}
				// System.out.println(timePath);
				/*
				 * flag = appiumDB.waitElementByXPath(timePath); if (flag) { Thread.sleep(2000);
				 * // appiumDB.tapElementByXPath(timePath); appiumDB.tapByCoordinates(350, 350);
				 * //System.out.println("Clicked end time"); Thread.sleep(1000);
				 */
				flag = appiumDB.waitElementByXPath(RentPageLocators.SET_END_TIME_BTN);
				if (flag) {
					Thread.sleep(1500);
					appiumDB.tapByCoordinates(700, 750);
					Thread.sleep(2500);
					appiumDB.clickBtnByXPath(RentPageLocators.SET_END_TIME_BTN);
					flag = appiumDB.waitElementByXPath(RentPageLocators.SET_DATES_BTN);
					if (flag) {
						appiumDB.tapElementByXPath(RentPageLocators.SET_DATES_BTN);
						flag = appiumDB.waitElementByXPath(RentPageLocators.CHECKOUT_BTN);
						if (flag)
							appiumDB.log("Successfully assigned the start and end dates");
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
		String date = null;
		String newPath = path;
		if (validateDate.equals("startdate")) {
			date = appiumDB.getCurrentDate(headsupCount);
			newPath = path + "'" + date + "']";
		}
		// System.out.println(newPath);
		String checkStatus = null;
		if (validateDate.equals("startdate"))
			checkStatus = appiumDB.getAttribute(newPath, "enabled");
		else if (validateDate.equals("enddate"))
			checkStatus = appiumDB.getAttribute(newPath, "selected");
		if (checkStatus.equals("true") && validateDate.equals("startdate")) {
			appiumDB.tapElementByXPath(newPath);
			return newPath;
		} else if (checkStatus.equals("false") && validateDate.equals("startdate")) {
			newPath = appiumDB.nextAvailableDate(path, "enabled", headsupCount, validateDate);
			flag = appiumDB.waitElementByXPath(newPath);
		} else if (validateDate.equals("enddate")) {
			Thread.sleep(2000);
			newPath = setEndDate(newPath, path, day);
		}
		// System.out.println(newPath);
		Thread.sleep(1000);
		if (newPath != null)
			appiumDB.tapElementByXPath(newPath);
		Thread.sleep(1000);
		return newPath;
	}

	public String setEndDate(String newPath, String path, String day) throws IOException, InterruptedException {
		if (day.equals("nextDay")) {
			if (newPath.contains("Saturday")) {
				newPath = null;
				System.out.println(path);
				newPath = appiumDB.nextAvailableDate(path, "selected", headsupCount, "enddate");
			} else {
				appiumDB.clickFollowingSibling(newPath);
				return null;
			}
		} else if (day.equals("sameDay")) {
			return newPath;
		}
		return newPath;
	}

	/**
	 * Set Time and send the modified xpath
	 * 
	 * @param validateDate
	 * @param newPath
	 * @param day
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String setTime(String validateDate, String newPath, String day) throws IOException, InterruptedException {
		String timePath = null;
		appiumDB.tapByCoordinates(0, 0);
		if (day.equals("sameDay"))
			timePath = RentPageLocators.START_TIME_BTN + appiumDB.getTime(validateDate, "sameDay");
		else if (day.equals("nextDay"))
			timePath = RentPageLocators.START_TIME_BTN + appiumDB.getTime(validateDate, "nextDay");
		return timePath;
	}

	/**
	 * Checkout a product
	 * 
	 * @throws InterruptedException
	 */
	public boolean checkoutAProduct() throws IOException, InterruptedException {
		// String bookingID = null;
		// int bookingResponse = 0;
		if (flag) {
			appiumDB.clickBtnByXPath(RentPageLocators.CHECKOUT_BTN);
			flag = appiumDB.waitElementByXPath(RentPageLocators.CONFIRM_CHECKOUT_BTN);
			if (flag) {
				appiumDB.clickBtnByXPath(RentPageLocators.CONFIRM_CHECKOUT_BTN);
				flag = appiumDB.waitElementByXPath(RentPageLocators.ADD_NOTE_TXT);
				if (flag) {
					Thread.sleep(5000);
					// appiumDB.scrollDownWithAnElement("add note");
					// appiumDB.scrollDownWithAnElement("add note");
					appiumDB.scrollDownWithValue(250, 1200);
					Thread.sleep(1000);
					appiumDB.scrollDownWithValue(250, 1200);
					Thread.sleep(1000);
					appiumDB.scrollDownWithValue(250, 1200);
					Thread.sleep(12000);
					appiumDB.tapByCoordinates(0, 0);
					Thread.sleep(10000);
					appiumDB.refreshPage();
					Thread.sleep(1000);
					flag = appiumDB.waitElementByXPath(RentPageLocators.PAYMENT_BTN);
					if (!flag) {
						appiumDB.refreshPage();
						flag = appiumDB.waitElementByXPath(RentPageLocators.PAYMENT_BTN);
					}
					if (flag) {
						appiumDB.tapElementByXPath(RentPageLocators.PAYMENT_BTN);
						Thread.sleep(5000);
						flag = appiumDB.waitElementByXPath(RentPageLocators.OKAY_BTN);
						if (flag) {
							Thread.sleep(5000);
							appiumDB.tapElementByXPath(RentPageLocators.OKAY_BTN);
							Thread.sleep(5000);
							flag = appiumDB.waitElementByXPath(RentPageLocators.ITINERARY_TXT);
							if (flag) {
								Thread.sleep(5000);
								appiumDB.scrollDownWithValue(250, 1200);
								appiumDB.scrollUpWithValue(250, 1200);
								flag = appiumDB.waitElementByXPath(RentPageLocators.ARROW_BACK_BTN);
								if (flag) {
									Thread.sleep(3000);
									appiumDB.doubleTap("arrow back ");
									// appiumDB.tapElementByXPath(RentPageLocators.ARROW_BACK_BTN);
									flag = appiumDB.waitElementByXPath(HomePageLocators.CONFIRMED_TAB);
									/*
									 * appiumDB.tapByCoordinates(0, 0); if (flag) { bookingID =
									 * appiumDB.getAttributeXPath( HomePageLocators.CONFIRMED_TAB_FIRST_RESULT,
									 * "content-desc"); System.out.println(bookingID); if (bookingID == null) flag =
									 * false; else if (bookingID != null) { try { bookingResponse =
									 * REST_CreateSession.getHeadsupHourByAPI(bookingID); if (bookingResponse != 0)
									 * flag = true; } catch (URISyntaxException | ParseException e) { // TODO
									 * Auto-generated catch block e.printStackTrace(); } Thread.sleep(3000); } }
									 */
								}
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
			flag = appiumDB.waitElementByXPath(HomePageLocators.RENT_BTN)
					&& appiumDB.waitElementByXPath(HomePageLocators.POST_BTN);
			if (flag)
				appiumDB.log("Verified the Rental market and post button in Homepage" + "<br>");
			if (flag) {
				appiumDB.clickBtnByXPath(HomePageLocators.POST_BTN);
				flag = appiumDB.waitElementByXPath(HomePageLocators.SIGN_IN_BTN);
				if (flag)
					appiumDB.log("Verified sign in button in Post page" + "<br>");
				if (flag) {
					appiumDB.scrollDownWithValue(250, 1200);
					flag = appiumDB.waitElementByXPath(HomePageLocators.SIGN_UP_BTN);
					if (flag)
						appiumDB.log("Verified sign up button in Post page" + "<br>");
					if (flag) {
						appiumDB.scrollUpWithValue(250, 1200);
						flag = appiumDB.waitElementByXPath(HomePageLocators.RENT_BTN);
						if (flag) {
							appiumDB.clickBtnByXPath(HomePageLocators.RENT_BTN);
							flag = appiumDB.waitElementByXPath(HomePageLocators.SEARCH_ICON);
							if (flag)
								appiumDB.log("Verified rental market buuton and came back to homepage" + "<br>");
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
	public boolean logout() throws IOException, InterruptedException {
		appiumDB.tapElementByXPath(HomePageLocators.HOME_TAB);
		Thread.sleep(2000);
		flag = appiumDB.waitElementByXPath(LoginPageLocators.MANAGE_TAB);
		if (flag) {
			appiumDB.tapElementByXPath(LoginPageLocators.MANAGE_TAB);
			Thread.sleep(5000);
			appiumDB.refreshPage();
			appiumDB.scrollDownWithValue(250, 1200);
			Thread.sleep(3000);
			appiumDB.scrollDownWithValue(250, 1200);
			Thread.sleep(3000);
			appiumDB.scrollDownWithValue(250, 1200);
			Thread.sleep(12000);
			appiumDB.refreshPage();
			flag = appiumDB.waitElementByXPath(HomePageLocators.SIGNOUT_BTN);
			if (!flag) {
				Thread.sleep(12000);
				appiumDB.refreshPage();
				flag = appiumDB.waitElementByXPath(HomePageLocators.SIGNOUT_BTN);
			}
			if (flag) {
				appiumDB.clickBtnByXPath(HomePageLocators.SIGNOUT_BTN);
				flag = appiumDB.waitElementByXPath(HomePageLocators.OKAY_BTN);
				if (flag) {
					appiumDB.clickBtnByXPath(HomePageLocators.OKAY_BTN);
					flag = appiumDB.waitElementByXPath(HomePageLocators.SEARCH_ICON);
				}
			}
		}
		return flag;
	}
}