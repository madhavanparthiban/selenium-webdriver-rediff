package keywords;

import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

/*
 * validateTitle,Text,ElementPresent,ElementClickable,ElementDisplayed*/
public class ValidationKeywords extends GenericKeywords{
	public void verifyTitle(String expTitle) {
		softAssert=new SoftAssert();
		logInfo("Expected title : "+expTitle);
		Assert.assertEquals(driver.getTitle(), prop.getProperty(expTitle));
	}
	
	public void validateSelectedValueInDropDown(String locatorKey, String value) {
		Select dropdown = new Select(getElement(locatorKey));
		String selectedValue = dropdown.getFirstSelectedOption().getText();
		logInfo(selectedValue+" this is selectedPortfolio name "+value);
		if(!selectedValue.equals(value)) {
			reportFailure("Entered "+value+" is not available in portfolio list",true);
		}
		logInfo("success on validation");
	}
	
	public void validateSelectedValueNotInDropDown(String locatorKey, String value) {
	
		Select dropdown = new Select(getElement(locatorKey));
		String selectedValue = dropdown.getFirstSelectedOption().getText();
		if(selectedValue.equals(value)) {
			reportFailure("Portfolio "+ value + " is  availble in Portfolio List", true);
		}
		logInfo("success on validation");
	}
}
