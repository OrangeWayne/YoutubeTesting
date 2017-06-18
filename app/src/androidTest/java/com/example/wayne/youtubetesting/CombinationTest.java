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
        if (mDevice.hasObject(By.text("新增帳戶")) || mDevice.hasObject(By.text("Add account"))){
            UiObject addAccount = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/add_account"));
            addAccount.click();
            sleep(2000);

            UiObject email = new UiObject(new UiSelector().resourceId("identifierId"));
            email.setText("sttest12332123");
            UiObject nextButton = new UiObject(new UiSelector().resourceId("identifierNext"));
            nextButton.click();
            sleep(1000);

            UiObject frameLayout = new UiObject(new UiSelector().resourceId("com.google.android.gms:id/minute_maid"));
            UiObject webView = frameLayout.getChild(new UiSelector().index(0));
            UiObject webViewChild = webView.getChild(new UiSelector().index(0));
            UiObject view = webViewChild.getChild(new UiSelector().index(2));
            UiObject password = view.getChild(new UiSelector().index(0));
            password.setText("qazxswpl3159");
            nextButton = new UiObject(new UiSelector().resourceId("passwordNext"));
            nextButton.click();

            UiObject signinconsentNextButton = new UiObject(new UiSelector().resourceId("signinconsentNext"));
            signinconsentNextButton.click();
            sleep(2000);
            UiObject continueBotton = new UiObject(new UiSelector().resourceId("com.google.android.gms:id/google_services_next_button_item"));
            continueBotton.click();
        }
        else {
            loginAccount.click();
        }

    }

    @Test //切換至發燒影片 5
    public void SwitchTab_hotVideo() throws UiObjectNotFoundException {
        UiObject tabBar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/tabs_bar"));
        UiObject tabBarLayout = tabBar.getChild(new UiSelector().index(0));
        UiObject hotVideo = tabBarLayout.getChild(new UiSelector().index(1));
        hotVideo.click();
    }

    @Test //切換至訂閱內容 1
    public void SwitchTab_subscription() throws UiObjectNotFoundException {
        UiObject tabBar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/tabs_bar"));
        UiObject tabBarLayout = tabBar.getChild(new UiSelector().index(0));
        UiObject subscription = tabBarLayout.getChild(new UiSelector().index(2));
        subscription.click();
    }

    @Test //切換至帳戶 4
    public void SwitchTab_account() throws UiObjectNotFoundException {
        UiObject tabBar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/tabs_bar"));
        UiObject tabBarLayout = tabBar.getChild(new UiSelector().index(0));
        UiObject account = tabBarLayout.getChild(new UiSelector().index(3));
        account.click();
    }

    @Test //訂閱 2
    public void SubscriptAndUnSubscript() throws UiObjectNotFoundException {
        //取得主頁第二個影片的上傳者 ((第一個影片通常都是廣告
        UiObject mainPage = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/results"));
        mainPage.swipeUp(10); //往下滑 直到第二個影片上傳者出現
        UiObject frameLayout = mainPage.getChild(new UiSelector().index(1));//取得訂閱目標位置
        UiObject linearLayout = frameLayout.getChild(new UiSelector().index(0));
        UiObject relativeLayout = linearLayout.getChild(new UiSelector().index(1));
        //取得訂閱目標
        UiObject subscriptTarget = relativeLayout.getChild(new UiSelector().index(0));
        subscriptTarget.click();
        //取得訂閱按鈕
        UiObject subscriptButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/subscribe_button"));
        subscriptButton.click(); //點選訂閱
        mDevice.pressBack();

        //取消訂閱
        UiObject tabBar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/tabs_bar"));
        UiObject tabBarLayout = tabBar.getChild(new UiSelector().index(0));
        UiObject subscription = tabBarLayout.getChild(new UiSelector().index(2));
        subscription.click();
        //選擇訂閱戶
        UiObject subscriptAccount = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/channel_avatar"));
        subscriptAccount.click(); //進入用戶頁面'
        //切換至用戶主頁
        linearLayout = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/appbar_layout"));
        UiObject horizontalScrollView = linearLayout.getChild(new UiSelector().index(1));
        UiObject kidLinearLayout = horizontalScrollView.getChild(new UiSelector().index(0));
        UiObject indexTab = kidLinearLayout.getChild(new UiSelector().index(0));
        indexTab.click(); //點擊至主頁
        //取得訂閱按鈕位置
        subscriptButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/subscribe_button"));
        subscriptButton.click(); //點選取消訂閱
        UiObject cancelSubscriptButton = new UiObject(new UiSelector().resourceId("android:id/button1"));
        cancelSubscriptButton.click();
        mDevice.pressBack();
    }

    @Test //選取影片加入稍後觀看
    public void AddInWatchLater() throws UiObjectNotFoundException {
        //取得主頁第二個影片 ((第一個影片通常都是廣告
        UiObject mainPage = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/results"));
        mainPage.swipeUp(10); //往下滑 直到第二個影片出現
        UiObject frameLayout = mainPage.getChild(new UiSelector().index(1));//取得目標位置
        UiObject linearLayout = frameLayout.getChild(new UiSelector().index(0));
        UiObject relativeLayout = linearLayout.getChild(new UiSelector().index(0));
        //取得目標影片選項
        UiObject moiveTarget = relativeLayout.getChild(new UiSelector().index(0));
        moiveTarget.click(); //點選影片
        UiObject viewGroup = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/buttons_container"));
        UiObject viewGroupItem = viewGroup.getChild(new UiSelector().index(3));
        UiObject addIntoButton = viewGroupItem.getChild(new UiSelector().index(0));
        addIntoButton.click(); //點選新增至

        UiObject playList = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/lists"));
        UiObject watchLater = playList.getChild(new UiSelector().index(1));
        UiObject addWatchLaterButton = watchLater.getChild(new UiSelector().index(1));
        addWatchLaterButton.click();
        mDevice.pressBack();
        mainPage.swipeDown(10); //往上滑 直到TabBar出現

        //切換至帳戶
        UiObject tabBar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/tabs_bar"));
        UiObject tabBarLayout = tabBar.getChild(new UiSelector().index(0));
        UiObject account = tabBarLayout.getChild(new UiSelector().index(3));
        account.click();
        mainPage = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/results"));
        linearLayout = mainPage.getChild(new UiSelector().index(4));
        UiObject watchLaterList = linearLayout.getChild(new UiSelector().index(1));
        watchLaterList.click();
        UiObject option = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/contextual_menu_anchor"));
        option.click();
        UiObject listView = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject listViewItem = listView.getChild(new UiSelector().index(1));
        UiObject removeFromWatchLater = listViewItem.getChild(new UiSelector().index(0));
        removeFromWatchLater.click();
        mDevice.pressBack();
    }

    @After
    public void Logout() throws UiObjectNotFoundException {
        UiObject toolbar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/toolbar"));
        UiObject toolbarItem = toolbar.getChild(new UiSelector().index(1));
        UiObject moreOption = toolbarItem.getChild((new UiSelector().index(1)));
        sleep(1000);
        moreOption.click();

        UiObject listView = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject listViewItem = listView.getChild(new UiSelector().index(4));
        UiObject listViewItemChild = listViewItem.getChild(new UiSelector().index(0));
        UiObject logout = listViewItemChild.getChild(new UiSelector().index(0));
        logout.click();
    }

}
