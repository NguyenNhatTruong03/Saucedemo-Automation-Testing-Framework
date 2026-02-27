package com.company.utils;


import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListenerUtil implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        LogUtil.info("=== BẮT ĐẦU TEST CASE: " + result.getName() + " ===");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtil.info(">>> KẾT QUẢ: PASS");
        // Bạn có thể thêm logic ghi kết quả vào Excel ở đây nếu muốn tập trung hóa
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtil.error(">>> KẾT QUẢ: FAILED!");
        LogUtil.error("Lý do lỗi: " + result.getThrowable().getMessage());
        
        // TỰ ĐỘNG CHỤP ẢNH KHI FAIL
        AllureReportUtil.saveScreenshotPNGReport();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtil.warn(">>> KẾT QUẢ: SKIPPED");
    }

    @Override
    public void onStart(ITestContext context) {
        LogUtil.info("--- BẮT ĐẦU SUITE: " + context.getName() + " ---");
    }

    @Override
    public void onFinish(ITestContext context) {
        LogUtil.info("--- KẾT THÚC SUITE: " + context.getName() + " ---");
    }
}
