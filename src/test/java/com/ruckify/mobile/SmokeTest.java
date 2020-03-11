package com.ruckify.mobile;

/**
 * @author Waheed Ahmed
 * @company Ruckify
 * Oct 01, 2019
 */

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SmokeTest {

	private AppiumDriverBase appiumDB;
	private UIBasicFunctions uibasicfunctions;
	boolean flag = false;

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		appiumDB = new AppiumDriverBase();
		uibasicfunctions = new UIBasicFunctions(appiumDB);
	}

	/**
	 * Login to the APP
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test(priority = 1)
	public void VerifyRentalAndPostItemPage() throws InterruptedException, IOException {
		flag = uibasicfunctions.verifyRentalPage();
		if(flag)
			appiumDB.log("---Test Case: Verify Rental And Post ItemPage, is passed---");
		Assert.assertTrue(flag);
	}

	/**
	 * Search and Rent a product
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test(priority = 2)
	public void VerifyHomePage() throws InterruptedException, IOException {
		flag = uibasicfunctions.verifyHomePage();
		if (flag) {
			flag = uibasicfunctions.logout();
			if(flag)
				appiumDB.log("---Test Case: Verify HomePage, is passed---");
		}
		Assert.assertTrue(flag);
	}

	/**
	 * 
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test(priority = 3)
	public void VerifyRentingAProductForSameDay() throws InterruptedException, IOException {
		flag = uibasicfunctions.verifyLoginFromRentingTab();
		if (flag) {
			flag = uibasicfunctions.searchAProduct(false);
			if (flag) {
				flag = uibasicfunctions.rentAProduct("sameDay");
				if (flag) {
					flag = uibasicfunctions.checkoutAProduct();
					if (flag) {
						flag = uibasicfunctions.logout();
						if(flag)
							appiumDB.log("---Test Case: Verify Renting A Product For SameDay, is passed---");
					}
				}
			}
		}
		Assert.assertTrue(flag);
	}

	@AfterClass(alwaysRun = true)
	public void teardown() throws InterruptedException {
		appiumDB.teardown();
		//appiumDB.stopServer();
	}
}
