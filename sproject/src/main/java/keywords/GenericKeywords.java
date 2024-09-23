package keywords;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import reports.ExtendManager;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.safari.SafariDriver;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import net.sourceforge.tess4j.*;

/*
 * open browser-click-type-select-getText-navigate-acceptAlert_dismissAlert
 * */
@SuppressWarnings("unused")
public class GenericKeywords {
	public WebDriver driver = null;
	public Properties prop = null;
	public ExtentTest test;
	public SoftAssert softAssert;//this is initialized in applicationKeywords

	public void openBrowser(String browserName) {	
		String browserKey=prop.getProperty(browserName);
		
		logInfo("Opening Browser -- " + browserName);
		if (browserKey.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized", "--disable-infobars");
			options.addArguments("--disable-extensions");
			options.addArguments("--disable-notifications");
			driver = new ChromeDriver(options);
		} 
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	}

	public void openURL(String URL) {
		driver.get(prop.getProperty(URL));
		logInfo("Opening Web Url : " + URL);	
	}

	public void click(String locator) {
		getElement(locator).click();
		logInfo("clicked on locator : "+locator);
	}

	public void type(String locator, String text) {	
	    getElement(locator).sendKeys(prop.getProperty(text));
	    logInfo("typed the "+text+ " on locator : "+locator);
	}
	public void simpleType(String locator, String text) {	
	    getElement(locator).sendKeys(text);
	    logInfo("typed the "+text+ " on locator : "+locator);
	}
	public void enterCaptcha(String locator) {
		try (Scanner scan = new Scanner(System.in)) {
			System.out.println("enter captcha");
			getElement(locator).sendKeys(scan.nextLine());
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	public void clear(String locatorKey) {
		logInfo("clearing default portfolio name: "+locatorKey);
		getElement(locatorKey).clear();
	}
	
	public void selectByVisibleText(String locatorKey,String value) {
		logInfo("Selecting the portfolio ");
		Select dropDown = new Select(getElement(locatorKey));
		dropDown.selectByVisibleText(value);
	}
	public void acceptAlert() {
		logInfo("accepting the alert");
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(60));
		wait.until(ExpectedConditions.alertIsPresent());
		
		driver.switchTo().alert().accept();
	}
	public void waitforWebPageToLoad1() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int i = 0;

		while (i != 10) {
			String state = (String) js.executeScript("return document.readyState;");
			//System.out.println(state);

			if (state.equals("complete"))
				break;
			else
				try {
					wait(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			i++;
		}

		// check for jQuery status
		i = 0;
		while (i != 10) {

			Long d = (Long) js.executeScript("return jQuery.active;");
			//System.out.println(d);
			if (d.longValue() == 0)
				break;
			else
				try {
					wait(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			i++;

		}
	}
	public void waitforWebPageToLoad() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    int i = 0;

	    // Wait for document.readyState to be complete
	    while (i != 10) {
	        String state = (String) js.executeScript("return document.readyState;");
	        //System.out.println(state);

	        if (state.equals("complete"))
	            break;
	        else
	            try {
	                // Pause for 2 seconds
	                Thread.sleep(2000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }

	        i++;
	    }

	    // Check for jQuery active requests
	    i = 0;
	    while (i != 10) {
	        Long d = (Long) js.executeScript("return jQuery.active;");
	        System.out.println(d);
	        if (d.longValue() == 0)
	            break;
	        else
	            try {
	                // Pause for 2 seconds
	                Thread.sleep(2000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }

	        i++;
	    }
	}
	
	public void clickEnterKey(String locatorKey) {
		logInfo("Pressing Enter Key");
		getElement(locatorKey).sendKeys(Keys.ENTER);
	}
	
	public void quitBrowser() {
		driver.quit();
	}
	
	
	//***************Failure Handling*****************//
	//acceptance failure->softassertion, critical failure->assertAll, unexpected failure=>ITestListner
	public void setReport(ExtentTest test) {//its called in while executing every test as per xml file
		this.test=test;
	}
	
	public void reportFailure(String msg, boolean isCriticalErr) {//called when there existed an exception
		logError(msg);
		takeScreenShot();
		softAssert.fail(msg);
		if(isCriticalErr) {
			Reporter.getCurrentTestResult().getTestContext().setAttribute("isCriticalFailure","true");
			//if you dont add the above line, test method like doLogin will terminate but create portfolio will not terminate
			//so it should be checked in beforeMethod
			reportAll();//it stops the execution of current method	
		}
			logError(msg);
			softAssert.fail(msg);
		
	}
	public void reportFailure(String msg) {
		reportFailure(msg,false);//softAssert should be initialized
	}
	
	public void reportAll() {// its called after every method
		softAssert.assertAll();//softAssert should be initialized
	}
	
	public void passCaptcha(String locator, String target) {
		String locatorKey =  prop.getProperty(locator);
		WebElement imageCaptcha = getElement(locator);
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // Wait up to 10 seconds
	    wait.until(ExpectedConditions.visibilityOf(imageCaptcha)); // Wait for captcha to be visible

		
		File src = imageCaptcha.getScreenshotAs(OutputType.FILE);
		
		String path = "C:\\Users\\Maddy\\OneDrive\\Desktop\\projects\\DataDrivenFramework\\imageCaptcha\\captchaimage.png";
		
		try {
			FileHandler.copy(src, new File(path));
		} catch (IOException e) {
			logError("captcha error");
			reportFailure("captcha error", true);
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			logError("slept in in passCaptcha");
			e.printStackTrace();
		}
		
		ITesseract image = new Tesseract();
		
		String str ="";
		try {
			str = image.doOCR(new File(path));
			logInfo("captcha is :"+str);
		} catch (Exception e) {
			logError("error in ocr passCaptcha");
			reportFailure("error in ocr passCaptcha");
			e.printStackTrace();
		}
		
		getElement(target).sendKeys(str);
		
		
	}
	
	

	public void passCaptcha2(String locator, String target) {
	    // Selenium code to capture the captcha image
	    WebElement imageCaptcha = getElement(locator);
	    

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // Wait up to 10 seconds
	    wait.until(ExpectedConditions.visibilityOf(imageCaptcha)); // Wait for captcha to be visible

	    File src = imageCaptcha.getScreenshotAs(OutputType.FILE);
	    String path = "C:\\Users\\Maddy\\OneDrive\\Desktop\\projects\\DataDrivenFramework\\imageCaptcha\\captchaimage.png";
	    
	    try {
	        FileHandler.copy(src, new File(path));
	    } catch (IOException e) {
	    	logError("exception in FileHandler");
	        e.printStackTrace();
	    }

	    // Preprocess the image using OpenCV
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    Mat img = Imgcodecs.imread(path, Imgcodecs.IMREAD_GRAYSCALE); // Convert to grayscale
	    Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY); // Apply thresholding
	    Imgproc.GaussianBlur(img, img, new Size(3, 3), 0); // Reduce noise

	    String processedPath = "C:\\Users\\Maddy\\OneDrive\\Desktop\\projects\\DataDrivenFramework\\imageCaptcha\\captchaimage_processed.png";
	    Imgcodecs.imwrite(processedPath, img); // Save the preprocessed image

	    // Use Tesseract for OCR
	    ITesseract tesseract = new Tesseract();
	    String result = "";
	    try {
	        result = tesseract.doOCR(new File(processedPath)); // Read the preprocessed image
	    } catch (TesseractException e) {
	    	logError("error in ocr passCaptcha");
			reportFailure("error in ocr passCaptcha");
	        e.printStackTrace();
	    }

	    // Enter the result into the target element
	    getElement(target).sendKeys(result);
	}
	
	public void waitForElement(String locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // Wait up to 10 seconds
	    wait.until(ExpectedConditions.visibilityOf(getElement(locator))); // Wait for captcha to be visible
	}

		
	
	// **********************HELPING FUCNTIONS***********************//

	public WebElement getElement(String locator) {
		//locator= prop.getProperty(locator);
		//isElementVisible(prop.getProperty(locator));
		return driver.findElement(getLocator(locator));
	}
	public By getLocator(String locatorkey) {
		By by = null;

		if (locatorkey.endsWith("_id"))
			by = By.id(prop.getProperty(locatorkey));
		else if (locatorkey.endsWith("_xpath"))
			by = By.xpath(prop.getProperty(locatorkey));
		else if (locatorkey.endsWith("_css"))
			by = By.cssSelector(prop.getProperty(locatorkey));
		else if (locatorkey.endsWith("_linkText"))
			by = By.linkText(prop.getProperty(locatorkey));
		else if (locatorkey.endsWith("_partialLinkText"))
			by = By.partialLinkText(prop.getProperty(locatorkey));
		else if (locatorkey.endsWith("_name"))
			by = By.name(prop.getProperty(locatorkey));
		else if (locatorkey.endsWith("_className"))
			by = By.className(prop.getProperty(locatorkey));
		else if (locatorkey.endsWith("_name"))
			by = By.name(prop.getProperty(locatorkey));
		else if (locatorkey.endsWith("_tagName"))
			by = By.tagName(prop.getProperty(locatorkey));
		return by;
	}
	
	public void logInfo(String msg) {test.log(Status.INFO, msg);}
	public void logError(String msg) {test.log(Status.FAIL, msg);}
	public void logWarning(String msg) {test.log(Status.WARNING, msg);}
	public void logSkip(String msg) {test.log(Status.SKIP, msg);}
	
	
	
	public void takeScreenShot() {
	    // Get the current date
	    Date current_date = new Date();

	    // Format the date and time for the screenshot file name
	    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	    String formatted_date = date_format.format(current_date);
	    String screenshotFile = formatted_date + ".png"; // Screenshot file name

	    // Take the screenshot
	    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

	    try {
	        // Ensure the screenshot is saved in the correct path with the correct name
	        File targetFile = new File(ExtendManager.screenShotPath + "/" + screenshotFile);
	        FileUtils.copyFile(srcFile, targetFile);

	        // Log the screenshot in the report in starting
	       // test.log(Status.INFO, "Screenshot-- " + test.addScreenCaptureFromPath(targetFile.getAbsolutePath()));
	        
	        test.log(Status.FAIL, MarkupHelper.createLabel("Screenshot", ExtentColor.RED));

	        // Log the screenshot image with the HTML <img> tag for better viewing in the report
	        test.log(Status.FAIL, "<img src='" + targetFile.getAbsolutePath() + "' style='width: 100%;'>");

	        // Output the screenshot path to the console
	        System.out.println("Screenshot saved at: " + targetFile.getAbsolutePath());

	    } catch (IOException e) {
	        // Handle file IO exceptions
	        e.printStackTrace();
	        test.log(Status.FAIL, "Failed to save screenshot: " + e.getMessage());
	    }
	}

	
/*	public boolean isElementVisible(String locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
		} catch (Exception e) {
			reportFailure("Unable to locate Element with locator"+locator);
			reportFailure(e.getMessage());
			return false;
		}
		return true;
	}
/*
	public boolean isElementPresent(String locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
		} catch (Exception e) {
			
			return false;
		}
		return true;
	}
	public By getLocator(String locator){
		it can be found by key in properties file.
		if ends with _path->By.xpath
		_id,_linkText,_partialLinkText,_name,_classname,_tagname
	}
	*/
}