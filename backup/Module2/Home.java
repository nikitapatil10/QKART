package QKART_SANITY_LOGIN.Module1;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

            // Wait for Logout to Complete
            Thread.sleep(3000);

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any errors
     */
    public Boolean searchForProduct(String product) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Clear the contents of the search box and Enter the product name in the search
            // box

            WebElement searchproduct = driver.findElement(By.name("search"));
            searchproduct.clear();
            searchproduct.sendKeys(product);
            Thread.sleep(5000);

            return true;

        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {};
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            // search results
            searchResults = driver.findElements(By.xpath(
                    "//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-6 MuiGrid-grid-md-3 css-sycj1h']"));

            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false
            WebElement noproductavailable =
                    driver.findElement(By.xpath("//h4[text()=' No products found ']"));
            if (noproductavailable.isDisplayed()) {
                status = true;
            } else {
                status = false;
            }

            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through each product on the page to find the WebElement corresponding to the
             * matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */
            List<WebElement> addproducttocart = driver.findElements(By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 css-yg30e6']"));
            Thread.sleep(5000);
            List<WebElement> addingproducttocart =
                    driver.findElements(By.xpath("//button[text()='Add to cart']"));
            for (int i = 0; i < addproducttocart.size(); i++) {
                WebElement addproducttocartElement = addproducttocart.get(i);
                String actualProductText = addproducttocartElement.getText();
                if (actualProductText.equals(productName)) {
                    WebElement addingProductToCartElement = addingproducttocart.get(i);
                    addingProductToCartElement.click();
                    return true;
                }
            }

            System.out.println("Unable to find the given product");
            return false;
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
            WebElement checkout =
                    driver.findElement(By.xpath("//button[contains(@class,'css-177pwqq')]"));
            checkout.click();
            return status;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }

    /*
     * Return Boolean denoting the status of change quantity of product in cart operation
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

            // Find the item on the cart with the matching productName

            // Increment or decrement the quantity of the matching product until the current
            // quantity is reached (Note: Keep a look out when then input quantity is 0,
            // here we need to remove the item completely from the cart)
            List<WebElement> productNameElement =
                    driver.findElements(By.xpath("//div[@class='MuiBox-root css-1gjj37g']/div[1]"));
            List<WebElement> currentProductQuantity =
                    driver.findElements(By.xpath("//div[@data-testid='item-qty']"));
            List<WebElement> plusOperation =
                    driver.findElements(By.xpath("//div[@class='css-u4p24i']/button[2]"));
            List<WebElement> minusOperation =
                    driver.findElements(By.xpath("//div[@class='css-u4p24i']/button[1]"));
            for (int i = 0; i < productNameElement.size(); i++) {
                String productText = productNameElement.get(i).getText();
                if (productText.equals(productName)) {
                    while (true) {
                        String currentProuctQuantityText = currentProductQuantity.get(i).getText();
                        int currentQuantity = Integer.parseInt(currentProuctQuantityText);
                        if (currentQuantity > quantity) {
                            minusOperation.get(i).click();
                            Thread.sleep(2000);
                        } else if (currentQuantity < quantity) {
                            plusOperation.get(i).click();
                            Thread.sleep(2000);
                        } else if (currentQuantity == quantity) {
                            break;
                        }

                        if (currentQuantity == 1 && quantity == 0) {
                            break;
                        }
                    }
                }
            }

            return false;
        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println("exception occurred when updating cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6

            // Get all the cart items as an array of webelements

            // Iterate through expectedCartContents and check if item with matching product
            // name is present in the cart
            List<WebElement> expectedcartContentsElements = driver.findElements(By.xpath("//div[@class='MuiBox-root css-1gjj37g']/div[1]"));
            for(int i = 0; i< expectedCartContents.size(); i++)
            {
                String expectedCartContentsText = expectedCartContents.get(i);
                String expectedCartElement = expectedcartContentsElements.get(i).getText();
                if(!expectedCartContentsText.equals(expectedCartElement))
                {
                    return false;
                }
            }
            
            /*
               List<WebElement> tableHeaderElements = driver.findElements(By.xpath("//table//th"));
            for(int i = 0; i < expectedTableHeaders.size(); i++)
            {
                String expectedTableHeader = expectedTableHeaders.get(i);
                WebElement tableHeaderElement = tableHeaderElements.get(i);
                String actualTableHeader = tableHeaderElement.getText();
                if(!expectedTableHeader.equals(actualTableHeader))
                {
                    status = false;
                }
            }
             */

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}
