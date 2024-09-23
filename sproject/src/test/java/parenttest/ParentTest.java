package parenttest;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import keywords.ApplicationKeywords;
import reports.ExtendManager;

public class ParentTest{

	//to share this all methods in one test case
	//each test should have unique applicationKeyword object
	//so using before and after test
	//solution is itestcontext, to use itestcontext just get it as parameter
	//call setAttribute("app",app) and getAttribute("app")
	public ApplicationKeywords app;
	public ExtentReports extentReport;
	public ExtentTest extentTest;
	@BeforeTest
	public void beforeTest(ITestContext context) {
	app= new ApplicationKeywords();
	context.setAttribute("app", app);
	
	//init report
	extentReport=ExtendManager.getReport();
	extentTest=extentReport.createTest(context.getCurrentXmlTest().getName());//current test name
	extentTest.log(Status.INFO, "Starting Test: "+context.getCurrentXmlTest().getName());
	
	app.setReport(extentTest);//giving this report to generic class

	context.setAttribute("extentReport", extentReport);
	context.setAttribute("extentTest", extentTest);
	
	}
	
	@AfterTest
	public void afterTest(ITestContext context) {
		app = (ApplicationKeywords)context.getAttribute("app");
		if(app!=null) {
			app.quitBrowser();
		}
		extentReport=(ExtentReports) context.getAttribute("extentReport");
		if(extentReport!=null) {
			extentReport.flush();
		}
		
	}
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(ITestContext context) {
		app = (ApplicationKeywords)context.getAttribute("app");
		extentReport=(ExtentReports) context.getAttribute("extentReport");
		extentTest=(ExtentTest) context.getAttribute("extentTest");
		
		
		//to look whether the critical failure occured in previous method
		String criticalFailure = (String)context.getAttribute("isCriticalFailure");
		if(criticalFailure!=null && criticalFailure.equals("true")) {
			//look Failure handling in generic class
			app.logSkip("Critical Failure in Previous Test Method");
			throw new SkipException("Critical Failure in Previous Test Method");
		}
			
			
		
	}
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod(ITestContext context) {
		app = (ApplicationKeywords)context.getAttribute("app");
		app.reportAll();
		
	}
}
