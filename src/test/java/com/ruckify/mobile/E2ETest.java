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

public class E2ETest {

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
	@Test(groups = "e2e")
	public void LoginAndroid() throws InterruptedException, IOException {
		flag = uibasicfunctions.gotoLoginPage("new");
		if (flag)
			flag = uibasicfunctions.login();
		Assert.assertTrue(flag);
	}

	/**
	 * Search and Rent a product
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test(dependsOnMethods = "LoginAndroid", groups = "e2e")
	public void SearchAndRent() throws InterruptedException, IOException {
		flag = uibasicfunctions.searchAProduct(true);
		if (flag) {
			flag = uibasicfunctions.rentAProduct("nextDay");
			if (flag) {
				flag = uibasicfunctions.checkoutAProduct();
				if(flag)
					flag = uibasicfunctions.logout();
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
