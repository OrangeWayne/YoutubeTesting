package com.example.wayne.youtubetesting;

import android.content.Context;
import android.text.format.Formatter;
import android.util.Log;

/**
 * Created by ChihChunLiu on 2017/6/17.
 */

interface CallBack{
    void notify(long totalMemory, long currentMemory, double cpuUsage);
}

/**
 * 要取得記憶體和cpu使用率的話請改這邊的程式
 * AppMonitor是功能，除非要加新的東西來監控，不然不需要改
 * 做成call back的方式，會不斷地呼叫notify ，直到呼叫close
 * 預設輸出在logcat可以看到
 *
 * 使用方法如下 :
 * CallBack cb = new MonitorCallBack(this);
 * AppMonitor am = new AppMonitor(this);
 * am.StartMonitor(cb);
 * 其中this是在表示activity的context
 *
 * 關閉為
 * am.CloseMonitor
 */

public class MonitorCallBack {
}
