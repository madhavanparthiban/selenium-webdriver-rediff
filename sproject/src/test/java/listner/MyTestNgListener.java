package listner;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;

public class MyTestNgListener implements ITestListener {//to handle unexpected failure
	// to implement this, add this to xml file
	public void onTestFailure(ITestResult result) {
		//gets called as soon as when any type of  failure occured
		//set attribute done in parent class
		ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("extentTest");
		test.fail(result.getThrowable().getMessage());
	}

	public void onTestSuccess(ITestResult result) {
		ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("extentTest");
		test.pass("Test Success : " + result.getName());
		
	}
	
	public void onTestSkipped(ITestResult result) {
		ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("extentTest");
		test.skip(result.getName() + " :: Test Skipped Due to Critical Error in Previous Test");
	}
}
