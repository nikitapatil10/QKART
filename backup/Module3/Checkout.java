package QKART_SANITY_LOGIN.Module1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */
            WebElement newaddress = driver.findElement(By.id("add-new-btn"));
            newaddress.click();
            WebElement addingaddress = driver.findElement(By.xpath("//textarea[@class='MuiOutlinedInput-input MuiInputBase-input MuiInputBase-inputMultiline css-u36398']"));
            addingaddress.sendKeys(addresString);
            driver.findElement(By.xpath("//button[contains(@class,'MuiButtonBase-root  css-177pwqq')]")).click();
            WebDriverWait wait = new WebDriverWait(driver, 3);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='address-item not-selected MuiBox-root css-0']/div/p")));
            return false;
        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }

    /*
     * Return Boolean denoting the status of selecting an available address
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */

            List<WebElement> selectaddress = driver.findElements(By.xpath("//div[@class='address-item not-selected MuiBox-root css-0']/div/p"));
            for(int i = 0; i < selectaddress.size(); i++)
            {
                WebElement selectAddressElement = selectaddress.get(i);
                String selectAddressText = selectAddressElement.getText();
                if(selectAddressText.equals(addressToSelect))
                {
                    selectAddressElement.click();
                    return true;
                }
            }
            
            System.out.println("Unable to find the given address");
            return false;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }

    }

    /*
     * Return Boolean denoting the status of place order action
     */
    public Boolean placeOrder() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find the "PLACE ORDER" button and click on it
            driver.findElement(By.xpath("//button[contains(@class,'MuiButtonBase-root  css-177pwqq')]")).click();
            WebDriverWait wait = new WebDriverWait(driver, 3);
            wait.until(ExpectedConditions.or(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks" ),ExpectedConditions.visibilityOf(driver.findElement(By.id("notistack-snackbar")))));
            return false;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 08: MILESTONE 7
            
            String balanceMessage = driver.findElement(By.id("notistack-snackbar")).getText();
            String actualMessage = "You do not have enough balance in your wallet for this purchase";
            if(actualMessage.equals(balanceMessage)) {
              return true; 
            }
               
                
      

            return false;
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}
