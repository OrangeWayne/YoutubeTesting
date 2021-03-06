package com.example.wayne.youtubetesting;

import org.junit.After;
import org.junit.Before;

import android.content.Intent;
import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.EventCondition;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.test.uiautomator.UiAutomatorTestCase;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

import com.example.wayne.youtubetesting.AppMonitor;
import com.example.wayne.youtubetesting.MonitorCallBack;

/**
 * Created by ChihChunLiu on 2017/6/14.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PerformanceTest extends UiAutomatorTestCase   {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.google.android.youtube";
    private static final int LAUNCH_TIMEOUT = 20000;
    private UiDevice mDevice;

    @Before
    public void startYoutubeMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        try {
            if(mDevice.pressRecentApps()) {
                sleep(1000);
                UiObject dismissButton = new UiObject(new UiSelector().descriptionContains("Dismiss YouTube."));
                dismissButton.click();
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(6)),
                LAUNCH_TIMEOUT);
    }

    @Test
    public void Step1_testVideoPlayTime1() throws UiObjectNotFoundException{
        UiObject videoList = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/results"));
        UiObject secondVideo = videoList.getChild(new UiSelector().index(1));
        UiObject playVideo = secondVideo.getChild(new UiSelector().resourceId("com.google.android.youtube:id/thumbnail_layout"));
        UiObject videoFrame = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/player_fragment_container"));
        UiObject stopButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/fast_forward_rewind_triangles"));
        UiObject currentTime = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/time_bar_current_time"));
        UiObject totalTime = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/time_bar_total_time"));

        try {
            playVideo.click();
            sleep(1000);
            if (mDevice.hasObject(By.textContains("略過廣告")) || mDevice.hasObject(By.textContains("skip"))){
                sleep(7000);
                UiObject skipButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/skip_ad_button_container"));
                skipButton.click();
            }

            sleep(10000);
            videoFrame.click();
            stopButton.click();

            String[] current = currentTime.getText().split(":");
            String[] total = totalTime.getText().split(":");
            int second = Integer.valueOf(current[1]);
            int totalSecond = Integer.valueOf(total[1]);

            if (totalSecond >= 8 ){
                Assert.assertTrue("PlayTime",second >= 6 && second <= 14);
            }else {
                Assert.assertTrue(true);
            }

        } catch (UiObjectNotFoundException e) {
            fail("UiObject Not Found" + e.getMessage());
        }
    }

    @Test
    public void Step2_testVideoPlayTime2() throws UiObjectNotFoundException{
        UiObject videoList = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/results"));
        UiObject secondVideo = videoList.getChild(new UiSelector().index(1));
        UiObject playVideo = secondVideo.getChild(new UiSelector().resourceId("com.google.android.youtube:id/thumbnail_layout"));
        UiObject videoFrame = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/player_fragment_container"));
        UiObject stopButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/fast_forward_rewind_triangles"));
        UiObject currentTime = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/time_bar_current_time"));
        UiObject totalTime = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/time_bar_total_time"));


        try {
            playVideo.click();
            sleep(1000);
            if (mDevice.hasObject(By.textContains("略過廣告")) || mDevice.hasObject(By.textContains("skip"))){
                sleep(7000);
                UiObject skipButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/skip_ad_button_container"));
                skipButton.click();
            }

            sleep(20000);
            videoFrame.click();
            stopButton.click();

            String[] current = currentTime.getText().split(":");
            String[] total = totalTime.getText().split(":");
            int second = Integer.valueOf(current[1]);
            int totalSecond = Integer.valueOf(total[1]);

            if (totalSecond >= 16 ){
                Assert.assertTrue("PlayTime",second >= 14 && second <= 26);
            }else {
                Assert.assertTrue(true);
            }

        } catch (UiObjectNotFoundException e) {
            fail("UiObject Not Found" + e.getMessage());
        }
    }

    @Test
    public void Step3_testCPUAndMemory() throws UiObjectNotFoundException{
        UiObject videoList = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/results"));
        UiObject secondVideo = videoList.getChild(new UiSelector().index(1));
        UiObject playVideo = secondVideo.getChild(new UiSelector().resourceId("com.google.android.youtube:id/thumbnail_layout"));
        UiObject videoFrame = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/player_fragment_container"));
        UiObject stopButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/fast_forward_rewind_triangles"));

        Context context = InstrumentationRegistry.getContext();
        CallBack cb = new MonitorCallBack(context);
        AppMonitor am = new AppMonitor(context);
        am.StartMonitor(cb);

        try {
            playVideo.click();
            sleep(1000);
            if (mDevice.hasObject(By.textContains("略過廣告")) || mDevice.hasObject(By.textContains("skip"))){
                sleep(7000);
                UiObject skipButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/skip_ad_button_container"));
                skipButton.click();
            }

            sleep(10000);
            videoFrame.click();
            stopButton.click();
            sleep(2000);
            am.CloseMonitor();

            Assert.assertTrue(true);
        } catch (UiObjectNotFoundException e) {
            fail("UiObject Not Found" + e.getMessage());
        }
    }
}
