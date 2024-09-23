package testCases;

import org.testng.annotations.Test;

import keywords.*;

public class CreatePortfolioTest extends ApplicationKeywords{
	@Test
	public void createPortfolio() throws InterruptedException{
		/*
		 * 1. open target webpage
		 * 2. click on sign in button
		 * 3. enter login button
		 * 4. click on submit button
		 * 5. verify you are on portfolio page after login
		 * 6. click create portfolio link
		 * 7. enter portfolio name
		 * 8. click on create portfolio link*/
		
		ApplicationKeywords app = new ApplicationKeywords();
		
		app.enterCaptcha("captcha_xpath");
		app.click("signin_xpath");
		Thread.sleep(5000);
		app.verifyTitle("portfolio_title");
		app.quitBrowser();
		
	}
}
