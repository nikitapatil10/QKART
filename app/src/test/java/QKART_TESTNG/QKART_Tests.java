package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;
import static org.junit.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@Listeners(ListenerClass.class)
public class QKART_Tests {

    static RemoteWebDriver driver;
    public static String lastGeneratedUserName;

    @BeforeSuite(alwaysRun = true)
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        System.out.println("createDriver()");
    }

    /*
     * Testcase01: Verify a new user can successfully register
     */

    @Test(priority = 1,groups = {"Sanity_test"})
    @Parameters({"username","password"})
    public void TestCase01(String username, String password) throws InterruptedException {
        Boolean status;
        // logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");
        // takeScreenshot(driver, "StartTestCase", "TestCase1");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser(username, password, true);
        assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, password);
        // logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        assertTrue(status, "Failed to login with registered user");

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();

        // logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status
        // ? "PASS" : "FAIL");
        // takeScreenshot(driver, "EndTestCase", "TestCase1");
    }

    @AfterSuite
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    @Test(priority = 2,groups = {"Sanity_test"})
    @Parameters({"username","password"})
    public void TestCase02(String username, String password) throws InterruptedException {
        Boolean status;
        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser(username, password, true);
        assertTrue(status, "Failed to register user");
        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, password, false);

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        assertFalse(status, "The registration with same user is passed");
    }

    @Test(priority = 3,groups = {"Sanity_test"})
    @Parameters({"TC3_product1", "TC3_product2"})
    public void TestCase03(String product1, String product2) throws InterruptedException {
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product
        status = homePage.searchForProduct(product1);
        assertTrue(status, "Unable to search for product");

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available
       
        // sa.assertEquals(searchResults.size(), 0,"The actual size is not match with expected
        // size");

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            assertEquals(elementText, "YONEX Smash Badminton Racquet",
                    "The actual result contains un-expected values");
        }
        // Search for product
        status = homePage.searchForProduct(product2);
        assertFalse(false, "The result found for garbasge value.");
        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        assertEquals(searchResults.size(), 0, "The actual size and expected size not match");
        // System.out.println(homePage.isNoResultFound());
        status = homePage.isNoResultFound();
        assertTrue(status, "The product is found for garbage value");

    }

    @Test(priority = 4,groups = {"Regression_Test"})
    @Parameters({"TC4_productName"})
    public void TestCase04(String productName) throws InterruptedException {
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and get card content element of search results
        status = homePage.searchForProduct(productName);
        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result
            SoftAssert sa = new SoftAssert();
            status = result.verifySizeChartExists();
            sa.assertTrue(status, "The size chart is not exists");

            // Verify if size dropdown exists
            status = result.verifyExistenceofSizeDropdown(driver);
            sa.assertTrue(status, "The size dropdown is not exists.");

            // Open the size chart
            status = result.openSizechart();
            sa.assertTrue(status, "The size chart is not open");
            // Verify if the size chart contents matches the expected values
            sa.assertTrue(result.validateSizeChartContents(expectedTableHeaders, expectedTableBody,
                    driver), "The size chart contents are not matched.");

            // Close the size chart modal
            status = result.closeSizeChart(driver);
            sa.assertTrue(status, "The size chart is not closed.");
            sa.assertAll();
        }
    }



    @Test(priority = 5,groups = {"Sanity_test"})
    @Parameters({"TC5_product3", "TC5_product4", "TC5_address"})
    public void TestCase05(String product3, String product4, String address)
            throws InterruptedException {
        Boolean status;
        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "The registration is failed");

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "The login is failed.");

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct(product3);
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct(product4);
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(address);
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        // Place the order
        checkoutPage.placeOrder();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        // Go to the home page
        homePage.navigateToHome();

        // Log out the user
        homePage.PerformLogout();
    }


    @Test(priority = 6,groups = {"Regression_Test"})
    @Parameters({"TC6_product5", "TC6_product6"})
    public void TestCase06(String product5, String product6) throws InterruptedException {
        Boolean status;
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "The user is not registered successfully");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "The login is not successful");

        homePage.navigateToHome();
        status = homePage.searchForProduct(product5);
        assertTrue(status, "Unable to find the product");
        homePage.addProductToCart("Xtend Smart Watch");

        status = homePage.searchForProduct(product6);
        assertTrue(status, "Unable to find the product");
        homePage.addProductToCart("Yarine Floor Lamp");

        // update watch quantity to 2
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);

        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);

        // update watch quantity again to 1
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();

        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(
                    ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        } catch (TimeoutException e) {
            System.out.println("Error while placing order in: " + e.getMessage());
        }

        status = driver.getCurrentUrl().endsWith("/thanks");
        assertTrue(status, "Checkout not successful");

        homePage.navigateToHome();
        homePage.PerformLogout();
    }

  
    @Test(priority = 7,groups = {"Regression_Test"})
    @Parameters({"TC7_ProductName"})
    public void TestCase07(String TC7_ProductName) throws InterruptedException {
        Boolean status = false;
        List<String> expectedResult = Arrays.asList(TC7_ProductName.split(";"));

        Register registration = new Register(driver);
        Login login = new Login(driver);
        Home homePage = new Home(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "The user is not registered successfully");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "The login is not successfull");

        homePage.navigateToHome();
        status = homePage.searchForProduct(expectedResult.get(0));
        assertTrue(status, "Product is not found");
        homePage.addProductToCart(expectedResult.get(0));

        status = homePage.searchForProduct(expectedResult.get(1));
        assertTrue(status, "Product is not found");
        homePage.addProductToCart(expectedResult.get(1));

        homePage.PerformLogout();

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "The login is not successful");
        status = homePage.verifyCartContents(expectedResult);
        assertTrue(status, "The cart doesn't content expected result");

        homePage.PerformLogout();
    }

    @Test(priority = 8,groups = {"Sanity_test"})
    @Parameters({"TC8_product7", "TC8_quantity"})
    public void TestCase08(String product7, int quantity) throws InterruptedException {
        Boolean status;
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "The user is not registered successfully");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "The Login is not successful");
        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct(product7);
        assertTrue(status, "The product is not present");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");

        homePage.changeProductQuantityinCart(product7, quantity);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();
        assertTrue(status, "The balance is sufficient");

    }

    @Test(dependsOnMethods = {"TestCase10"},priority = 10,groups = {"Regression_Test"})
    public void TestCase09() throws InterruptedException {
        Boolean status = false;
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "The user is not registered successfully");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "The login is not successfull");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX");
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(status, "The product is not present");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        String currentURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);
        sa.assertTrue(status, "The cart not contains expected result.");

        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
    }

    @Test(priority = 9,groups = {"Regression_Test"})
    public void TestCase10() throws InterruptedException {
        Boolean status = false;
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "The user is not registered successfully");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "The Login is not successfull");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(status, "The url is not matched");

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");
        sa.assertTrue(status, "The url is not matched");

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();

        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = TOSHeading.getText().equals("Terms of Service");
        sa.assertTrue(status, "The url is not matched");

        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
    }

    @Test(priority = 11,groups = {"Regression_Test"})
    @Parameters({"TC11_UserName", "TC11_EmailId", "TC11_QueryContent"})
    public void TestCase11(String UserName, String EmailId, String QueryContent)
            throws InterruptedException {
        Boolean status;
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        driver.findElement(By.xpath("//*[text()='Contact us']")).click();
        WebElement contactBox = driver.findElement(By.xpath("//div[@class='card-block']"));
        status = contactBox.isDisplayed();
        assertTrue(status, "The contact box is not opened");
        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        name.sendKeys(UserName);
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        email.sendKeys(EmailId);
        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        message.sendKeys(QueryContent);

        WebElement contactUs = driver.findElement(By.xpath(
                "/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));
        contactUs.click();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        status = wait.until(ExpectedConditions.invisibilityOf(contactUs));
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(status, "The contact box is displayed");
    }

    @Test(priority = 12,groups = {"Sanity_test"})
    @Parameters({"TC12_product8", "TC12_address1"})
    public void TestCase12(String product8, String address1) throws InterruptedException {
        Boolean status = false;
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "The registration is not successful.");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "The login is not successfull");
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct(product8);
        assertTrue(status, "The product is not present");
        homePage.addProductToCart(product8);
        homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(address1);
        checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
        checkoutPage.placeOrder();
        Thread.sleep(3000);

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        status = Advertisements.size() == 3;
        assertTrue(status, "Advertisement count is not match.");

        WebElement Advertisement1 =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(status, "The advertisement 1 is not clickable");
        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        sa.assertTrue(status, "The advertisement 2 is not clickable");
        sa.assertAll();
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            File theDir = new File("/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
