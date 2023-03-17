package QKART_TESTNG;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass extends QKART_Tests implements ITestListener{
    public void onTestStart(ITestResult result)
    {
        System.out.println("New Test Started "+result.getName());
        takeScreenshot(driver, "StartTestCase", result.getName());
    }

    public void onTestFailure(ITestResult result)
    {
        System.out.println("Test Failed : "+ result.getName()+" Taking Screenshot ! ");
        takeScreenshot(driver, "TestCaseFailed", result.getName());
    }

    public void onTestSuccess(ITestResult result)
    {
        System.out.println("Test Success : "+ result.getName()+" Taking Screenshot ! ");
        takeScreenshot(driver, "TestCasePassed", result.getName());
    }
}
