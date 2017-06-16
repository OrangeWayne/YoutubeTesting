package com.example.wayne.youtubetesting;

import org.junit.After;
import org.junit.Before;

import android.content.Intent;
import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Thread.sleep;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;

import org.junit.FixMethodOrder;

/**
 * Created by wayne on 2017/6/13.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Testcase {

    private static final String BASIC_SAMPLE_PACKAGE
            = "com.google.android.youtube";
    private static final int LAUNCH_TIMEOUT = 3000;
    private UiDevice mDevice;

    @Test
    public void Step1_qwe() throws UiObjectNotFoundException, InterruptedException {
        LaunchApp();
        Login();
        Log.d("YoutubeTest", "test1");
    }

    @Test
    public void Step2_abc() throws UiObjectNotFoundException, InterruptedException, RemoteException {
        Log.d("YoutubeTest", "test2");
        Logout();
    }

    @Ignore
    public void LaunchApp() throws UiObjectNotFoundException, InterruptedException {

        Log.d("YoutubeTest", "LaunchApp");
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();

        try {
            if(mDevice.pressRecentApps()) {
                Thread.sleep(1000);
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
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);
    }

    @Ignore
    public void Login() throws UiObjectNotFoundException {
        Log.d("YoutubeTest","Login");
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

    @Ignore
    public void Logout() throws UiObjectNotFoundException, InterruptedException {
        Log.d("YoutubeTest","Logout");
        // initialize component
        UiObject toolbar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/toolbar"));
        UiObject toolbarItem = toolbar.getChild(new UiSelector().index(1));
        UiObject moreOption = toolbarItem.getChild((new UiSelector().index(1)));

        Thread.sleep(1000);
        moreOption.click();

        UiObject listView = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject listViewItem = listView.getChild(new UiSelector().index(4));
        UiObject listViewItemChild = listViewItem.getChild(new UiSelector().index(0));
        UiObject logout = listViewItemChild.getChild(new UiSelector().index(0));
        logout.click();
    }

}
