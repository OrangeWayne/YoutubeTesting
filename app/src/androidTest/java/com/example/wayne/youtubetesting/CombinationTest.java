package com.example.wayne.youtubetesting;

import org.junit.After;
import org.junit.Before;

import android.content.Intent;
import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiCollection;
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

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
/**
 * Created by ChihChunLiu on 2017/6/14.
 */

@RunWith(AndroidJUnit4.class)
public class CombinationTest extends UiAutomatorTestCase   {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.google.android.youtube";
    private static final int LAUNCH_TIMEOUT = 10000;
    private UiDevice mDevice;

    @Before
    public void startYoutubeMainActivityFromHomeScreen() throws UiObjectNotFoundException {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        try {
            if(mDevice.pressRecentApps()) {
                sleep(1000);
                int height = mDevice.getDisplayHeight();
                int width = mDevice.getDisplayWidth();
                mDevice.swipe(width/2,height/2, width*2, height/2, 10);
            }
        } catch (RemoteException e) {

        }

        // Start from the home screen
        mDevice.pressHome();

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
        UiObject toolbar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/toolbar"));
        UiObject toolbarItem = toolbar.getChild(new UiSelector().index(1));
        UiObject moreOption = toolbarItem.getChild((new UiSelector().index(1)));
        moreOption.click();

        UiObject listView = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject listViewItem = listView.getChild(new UiSelector().index(4));
        UiObject listViewItemChild = listViewItem.getChild(new UiSelector().index(0));
        UiObject login = listViewItemChild.getChild(new UiSelector().index(0));
        login.click();

        UiObject loginListView = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject loginListViewItem = loginListView.getChild(new UiSelector().index(1));
        UiObject loginListViewItemChild = loginListViewItem.getChild(new UiSelector().index(0));
        UiObject loginAccount = loginListViewItemChild.getChild(new UiSelector().index(0));
        loginAccount.click();
    }
/*
    @Test
    public void test_0_SettingLogin() throws UiObjectNotFoundException {
        UiObject toolbar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/toolbar"));
        UiObject toolbarItem = toolbar.getChild(new UiSelector().index(1));
        UiObject moreOption = toolbarItem.getChild((new UiSelector().index(1)));
        moreOption.click();

        UiObject listView = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject listViewItem = listView.getChild(new UiSelector().index(4));
        UiObject listViewItemChild = listViewItem.getChild(new UiSelector().index(0));
        UiObject login = listViewItemChild.getChild(new UiSelector().index(0));
        login.click();

        UiObject loginListView = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject loginListViewItem = loginListView.getChild(new UiSelector().index(1));
        UiObject loginListViewItemChild = loginListViewItem.getChild(new UiSelector().index(0));
        UiObject loginAccount = loginListViewItemChild.getChild(new UiSelector().index(0));
        loginAccount.click();
    }
*/
    @Test
    public void SwitchTab() throws UiObjectNotFoundException {
        UiObject tabBar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/tabs_bar"));
        UiObject tabBarLayout = tabBar.getChild(new UiSelector().index(0));
        UiObject hotVideo = tabBarLayout.getChild(new UiSelector().index(1));
        hotVideo.click();
    }

    @After
    public void Logout() throws UiObjectNotFoundException {
        UiObject toolbar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/toolbar"));
        UiObject toolbarItem = toolbar.getChild(new UiSelector().index(1));
        UiObject moreOption = toolbarItem.getChild((new UiSelector().index(1)));
        moreOption.click();

        UiObject listView = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject listViewItem = listView.getChild(new UiSelector().index(4));
        UiObject listViewItemChild = listViewItem.getChild(new UiSelector().index(0));
        UiObject logout = listViewItemChild.getChild(new UiSelector().index(0));
        logout.click();
    }

}
