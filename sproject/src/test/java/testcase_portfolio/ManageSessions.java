package testcase_portfolio;

import org.testng.ITestContext;
import org.testng.annotations.Test;



import parenttest.ParentTest;

public class ManageSessions extends ParentTest {

	@Test
	public void doLogin(ITestContext con) throws InterruptedException {
		app.openBrowser("browser_name");
		app.openURL("URL");
		app.click("signIn_xpath");
		app.type("email_xpath", "email");
		app.type("passwd_xpath", "email_password");
		app.waitforWebPageToLoad();
		app.waitForElement("captcha_image_xpath");
		//app.passCaptcha2("captcha_image_xpath","captcha_enter_xpath");
		Thread.sleep(30000);
		app.click("signin_xpath");
		app.waitforWebPageToLoad();
		app.verifyTitle("portfolio_title");
		//app.reportAll();for using listnermytestnglistener.java=>should be included in every test case
		//its mentioned in after method
	}
	@Test
	public void doLogout() {
		
	}
}
