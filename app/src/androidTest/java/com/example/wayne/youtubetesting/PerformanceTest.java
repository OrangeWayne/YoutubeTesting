package com.example.wayne.youtubetesting;

import org.junit.After;
import org.junit.Before;

import android.content.Intent;
import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.test.uiautomator.UiAutomatorTestCase;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
/**
 * Created by ChihChunLiu on 2017/6/14.
 */

@RunWith(AndroidJUnit4.class)
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
//                int height = mDevice.getDisplayHeight();
//                int width = mDevice.getDisplayWidth();
//                mDevice.swipe(width/2,height/2, width*2, height/2, 10);
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
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);
//        mDevice.wait(Until.hasObject(By.res("android.widget.LinearLayout")), LAUNCH_TIMEOUT);
//        sleep(1000);
    }

    @Test
    public void testVideoPlayTime(){
        UiObject firstVideo = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/thumbnail_layout"));
        UiObject videoFrame = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/player_fragment_container"));
        UiObject stopButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/fast_forward_rewind_triangles"));
        UiObject currentTime = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/time_bar_current_time"));

        try {
            firstVideo.click();
            sleep(10000);
            videoFrame.click();
            stopButton.click();
            String[] time = currentTime.getText().split(":");
            int second = Integer.valueOf(time[1]);
            Assert.assertTrue("PlayTime OK",second > 8 && second < 12);
        } catch (UiObjectNotFoundException e) {
            fail("UiObject Not Found");
        }

    }
}
