package listeners;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            Object currentClass = result.getInstance();
            Field pageField = getPageField(currentClass);

            if (pageField != null) {
                pageField.setAccessible(true);
                Page page = (Page) pageField.get(currentClass);

                if (page != null) {
                    byte[] screenshot = page.screenshot(
                            new Page.ScreenshotOptions().setFullPage(true)
                    );
                    // Attach Allure with type image/png
                    Allure.addAttachment(
                            "Screenshot on Failure - " + result.getName(),
                            "image/png",
                            new ByteArrayInputStream(screenshot),
                            ".png"
                    );
                    System.out.println("Screenshot attached for test: " + result.getName());
                } else {
                    System.err.println("Page object is null — cannot capture screenshot.");
                }
            } else {
                System.err.println("No field named 'page' found in test class hierarchy.");
            }

        } catch (Exception e) {
            System.err.println("Error capturing screenshot on failure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Field getPageField(Object instance) {
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                return clazz.getDeclaredField("page");
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSuccess(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}
