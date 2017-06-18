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
    public void Step1_LaunchApp() throws UiObjectNotFoundException, InterruptedException {

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

    @Test
    public void Step2_Login() throws UiObjectNotFoundException, InterruptedException {

        Log.d("YoutubeTest","Login");
        UiObject moreOption = new UiObject(new UiSelector().descriptionMatches("More options"));
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


        // Check user is the same as expected account
        UiObject accountTab = new UiObject(new UiSelector().descriptionContains("Account"));
        accountTab.click();

        UiObject accountName = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/account_name"));
        assertEquals("軟測", accountName.getText());
    }

    @Test
    public void Step3_RecordVideoAndUploadVideo() throws UiObjectNotFoundException, InterruptedException {

        Log.d("YoutubeTest", "RecordVideo");

        // Click Account Tab
        UiObject accountTab = new UiObject(new UiSelector().descriptionContains("Account"));
        accountTab.click();

        // Click Video Button
        UiObject videoButton = new UiObject(new UiSelector().descriptionMatches("Video"));
        videoButton.click();

        UiObject camera = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/camera_button_camera_image"));
        camera.click();

        Thread.sleep(1000);

        // Start record
        UiObject shutter = new UiObject(new UiSelector().descriptionMatches("Shutter"));
        shutter.click();
        // Record a movie for five seconds
        Thread.sleep(5000);
        // Stop record
        shutter.click();

        Thread.sleep(1000);

        // Check the video has been recorded
        UiObject doneButton = new UiObject(new UiSelector().descriptionMatches("Done"));
        doneButton.click();


        // Input video title
        UiObject movieTitle = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/title_edit"));
        movieTitle.setText("test video title");

        // Upload video
        UiObject uploadButton = new UiObject(new UiSelector().descriptionMatches("Upload"));
        uploadButton.click();

        Thread.sleep(20000);

        // check video is uploaded
        UiObject videoList = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/results"));
        UiObject targetVideo = videoList.getChild(new UiSelector().index(0));
        UiObject videoTitle = targetVideo.getChild(new UiSelector().resourceId("com.google.android.youtube:id/title"));
        assertEquals("test video title", videoTitle.getText());
        assertEquals(true, targetVideo.isClickable());

    }

    @Test
    public  void Step4_PlayVideo() throws UiObjectNotFoundException, InterruptedException {

        Log.d("YoutubeTest", "PlayVideo");
        UiObject videoList = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/results"));
        UiObject targetVideo = videoList.getChild(new UiSelector().index(0));
        targetVideo.click();

        // check video title
        UiObject videoTitle = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/title"));
        assertEquals("test video title", videoTitle.getText());

        Thread.sleep(1000);

        //check video can play
        Thread.sleep(10000);
        UiObject replayIcon =  new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/player_control_play_pause_replay_button"));

        assertEquals(true, replayIcon.isClickable());

        //collapse video
        UiObject collapseButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/player_collapse_button"));
        collapseButton.click();

    }

    @Test
    public void Step5_Logout() throws UiObjectNotFoundException, InterruptedException, RemoteException {

        Log.d("YoutubeTest","Logout");
        // initialize component
        UiObject moreOption = new UiObject(new UiSelector().descriptionMatches("More options"));

        Thread.sleep(1000);
        moreOption.click();

        UiObject listView = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject listViewItem = listView.getChild(new UiSelector().index(4));
        UiObject listViewItemChild = listViewItem.getChild(new UiSelector().index(0));
        UiObject logout = listViewItemChild.getChild(new UiSelector().index(0));
        logout.click();
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
