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

public class FunctionalTest {

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
	@Test
	public void VerifyLoginWithValidCredential() throws InterruptedException, IOException {
		flag = uibasicfunctions.gotoLoginPage("new");
		if (flag) {
			flag = uibasicfunctions.login();
			if(flag)
				appiumDB.log("---Test Case: Verify Login With Valid Credential, is passed---");
		}
		Assert.assertTrue(flag);
	}

	/**
	 * Search and Rent a product
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test(dependsOnMethods = "LoginAndroid")
	public void VerifySearchAndRentForNextday() throws InterruptedException, IOException {
		flag = uibasicfunctions.searchAProduct();
		if (flag) {
			flag = uibasicfunctions.rentAProduct("nextDay");
			if (flag) {
				flag = uibasicfunctions.checkoutAProduct();
				if (flag) {
					flag = uibasicfunctions.logout();
					if(flag)
						appiumDB.log("---Test Case: Verify Search And Rent For Next day, is passed---");
				}
			}
		}
		Assert.assertTrue(flag);
	}

	@AfterClass(alwaysRun = true)
	public void teardown() throws InterruptedException {
		appiumDB.teardown();
		appiumDB.stopServer();
	}
}
