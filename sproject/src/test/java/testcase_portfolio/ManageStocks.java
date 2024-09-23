package testcase_portfolio;

import java.time.Duration;

//import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Parameters;
//import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import parenttest.ParentTest;

public class ManageStocks extends ParentTest {
	
	//int modifiedQuantity =50;//should be same for every method
	@Parameters({ "action" })
	@Test
	public void modifyStock(String action, ITestContext context) {
		//first it will select that company then click on buy/sell button
		//
		//JSONObject data = (JSONObject) context.getAttribute("testData");
		
		String companyName = "YES Bank Ltd.";//(String) data.get("companyName");
		String modifiedQuantity = "50";//(String) data.get("stockQuantity");
		String stockPrice = "100";//(String) data.get("stockPrice");
		String selectionDate = "15-09-2024"; //(String) data.get("selectionDate");

		app.logInfo("Modifying Quantity : " + modifiedQuantity + " of Stock :: " + companyName);

		int quantityBeforeSelling = app.findCurrentStockQuantity(companyName);
		context.setAttribute("quantityBeforeSelling", quantityBeforeSelling);

		app.goToBuySell(companyName);//selecting the stock and clicked on buy/sell
		if(action.equals("addStock")) {
			app.selectByVisibleText("equityaction_id", "Buy");
		}else {
			app.selectByVisibleText("equityaction_id", "Sell");
		}
		
		app.click("buySellCalendar_id");
		app.selectDateFromCalendar(selectionDate);
		app.simpleType("buysellqty_id", modifiedQuantity);
		app.simpleType("buysellprice_id", stockPrice);
		app.click("buySellStockButton_id");
		app.waitforWebPageToLoad();
		app.logInfo("Stock :: " + companyName + " modified Successfully....");
	}

	@Parameters({ "action" })
	@Test
	public void verifyStockQuantity(String action, ITestContext context) {
		//JSONObject data = (JSONObject) context.getAttribute("testData");
		//quantityBeforeSelling contains quantity before modifying
		//which is used to verify
		
		String companyName = "YES Bank Ltd.";//(String) data.get("companyName");
		int modifiedQuantity = 50;//Integer.parseInt((String)data.get("stockQuantity"));
		int expectedModifiedQuantity = 0;
		
		app.logInfo("Verify Stock Quantity After Action :: " + action);
		int curentQuantity = app.findCurrentStockQuantity(companyName);

		int quantityBeforeSelling = (int) context.getAttribute("quantityBeforeSelling");
		app.logInfo("Earlier Stock Quantity : " + quantityBeforeSelling);
		if (action.equals("sellStock")) {
			expectedModifiedQuantity = quantityBeforeSelling - curentQuantity;
		} else if (action.equals("addStock")) {
			expectedModifiedQuantity = curentQuantity - quantityBeforeSelling;
		}

		
		app.logInfo("New Stock Quantity : " + curentQuantity);

		if (expectedModifiedQuantity != modifiedQuantity) {
			app.reportFailure("Expected Modified Quantity is not matching", true);
		}

		app.logInfo("Stock Quantity Changed as per expected :: " + expectedModifiedQuantity);

	}

	@Parameters({ "action" })
	@Test
	public void verifyTransactionHistory(String action, ITestContext context) {
		//JSONObject data = (JSONObject) context.getAttribute("testData");
		app.logInfo("called verifyTransactionHistory method");
		String companyName = "YES Bank Ltd.";//(String) data.get("companyName");
		String modifiedQuantity = "50";//(String) data.get("stockQuantity");

		app.logInfo("Verify Stock Transaction History After Operation :: " + action);
		app.openTrasactionHistory(companyName);
		String quantityDisplayed = app.getElement("trasactionTable_xpath").getText();
		if(action.equals("sellStock")){modifiedQuantity="-"+modifiedQuantity;}
		if(!modifiedQuantity.equalsIgnoreCase(quantityDisplayed)) {
			app.reportFailure("Got changed quantity in transaction history as " + quantityDisplayed, true);
		}

		if (action.equals("sellStock")) {
			quantityDisplayed = "-" + quantityDisplayed;
		}
		
		app.logInfo("Latest Change in Stock : " + companyName + " is :: " + quantityDisplayed);
	}

	@Test
	public void addStockTest(ITestContext context) {
		//JSONObject data = (JSONObject) context.getAttribute("testData");
		app.logInfo("called addStockTest method");
		String companyName = "YES Bank Ltd";//(String) data.get("companyName");
		String stockQuantity = "50";//(String) data.get("stockQuantity");
		String stockPrice = "200";//(String) data.get("stockPrice");
		String selectionDate = "12-09-2024";//(String) data.get("selectionDate");

		app.logInfo("Selecting Stocks in Portfolio");
		
		int quantityBeforeSelling = app.findCurrentStockQuantity(companyName);
		context.setAttribute("quantityBeforeSelling", quantityBeforeSelling);

		app.click("addStock_id");
		app.simpleType("addstockname_id", companyName);
		app.logInfo("typed stock name");
		try {
			Thread.sleep(Duration.ofSeconds(5));
		} catch (InterruptedException e) {
			app.logError("error in sleeping");
			app.reportFailure("error in sleeping",false);
			e.printStackTrace();
		}
		app.clickEnterKey("addstockname_id");
		app.click("stockPurchaseDate_id");
		app.selectDateFromCalendar(selectionDate);
		app.simpleType("addstockqty_id", stockQuantity);
		app.simpleType("addstockprice_id", stockPrice);
		app.click("addStockButton_id");
		app.waitforWebPageToLoad();

		app.logInfo("Stock Added Successfully....");

	}

	@Test
	public void verifyStockIsPresent(ITestContext context) {
		//JSONObject data = (JSONObject) context.getAttribute("testData");
		app.logInfo("verifying whether stock is present");
		String companyName = "YES Bank Ltd.";//(String) data.get("companyName");

		app.logInfo("Verifying Added Stock in Portfolio...");
		int rowNum = app.getRowNumWithCellData("stockTable_id", companyName);

		if (rowNum == -1) {
			app.reportFailure(companyName + " is not present in Stock List!!! ", true);
		}
		app.logInfo(companyName + " -- Found in Portfolio Stocks");
	}

}