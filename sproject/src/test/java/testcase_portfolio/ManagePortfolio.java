package testcase_portfolio;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import parenttest.ParentTest;

public class ManagePortfolio extends ParentTest{
	@Test
	public void createPortfolio() {
		String portfolio_name="My_first_Portfolio2";
		app.logInfo("Creating portfolio : "+portfolio_name);
		app.click("createPorfolio_xpath");
		app.clear("porfolio_name_xpath");
		app.simpleType("porfolio_name_xpath", portfolio_name);
		app.click("createPorfolio_submit_xpath");
		app.waitforWebPageToLoad();
		app.validateSelectedValueInDropDown("portfolio_dropdown_xpath", portfolio_name);
		
		
		
		
	}
	
	@Test
	public void deletePortfolio() {
		String portfolio_name="My_first_Portfolio2";
		app.logInfo("deleting the portfolio : "+portfolio_name);
		app.selectByVisibleText("portfolio_dropdown_xpath", portfolio_name);
		app.waitforWebPageToLoad();
		app.click("deletePortfolio_xpath");
		app.acceptAlert();
		//app.waitforWebPageToLoad();
		app.validateSelectedValueNotInDropDown("portfolio_dropdown_xpath", portfolio_name);
		app.quitBrowser();
	}
	@Test
	public void selectPortfolio(ITestContext context) {
		
		String portfolioName ="MyPortfolio10";
		app.logInfo("Selecting Portfolio :: " + portfolioName);
		app.selectByVisibleText("portfolio_dropdown_xpath", portfolioName);
		app.waitforWebPageToLoad();
	}
	@Test
	public void renamePortfolio() {
		
	}
}
