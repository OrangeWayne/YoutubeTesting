package com.example.wayne.youtubetesting;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ChihChunLiu on 2017/6/17.
 */

public class AppMonitor {
    Context mainContext;
    Boolean monitorFlag;

    AppMonitor(Context c){
        mainContext = c;
        monitorFlag = false;
    }

    public void StartMonitor(final CallBack cb){
        if(monitorFlag) return;
        monitorFlag = true;

        new Thread(new Runnable(){
            CallBack callBack = cb;

            Handler mHandler = new Handler(Looper.getMainLooper()){
                int i = 0;
                @Override
                public void handleMessage(Message msg) {
                    switch(msg.what){
                        case 1:
                            i++;
                            callBack.notify(getTotalMemory(), getAvailMemory(), getProcessCpuRate());
                            break;
                    }
                    super.handleMessage(msg);
                }
            };

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while(monitorFlag){
                    try{
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                        Thread.sleep(500);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void CloseMonitor(){
        monitorFlag = false;
    }

    float totalCpuTime1 = -1;
    float processCpuTime1 = -1;
    private float getProcessCpuRate()
    {
        if(totalCpuTime1 == -1) totalCpuTime1 = getTotalCpuTime();
        if(processCpuTime1 == -1) processCpuTime1 = getAppCpuTime();
        float totalCpuTime2 = getTotalCpuTime();
        float processCpuTime2 = getAppCpuTime();

        float cpuRate = 100 * (processCpuTime2 - processCpuTime1)
                / (totalCpuTime2 - totalCpuTime1);

        totalCpuTime1 = totalCpuTime2;
        processCpuTime1 = processCpuTime2;
        return cpuRate;
    }

    private long getTotalCpuTime()
    { // 獲取系統總CPU使用時間
        String[] cpuInfos = null;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }

    private long getAppCpuTime()
    { // 獲取應用占用的CPU時間
        String[] cpuInfos = null;
        try
        {
            int pid = android.os.Process.myPid();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long appCpuTime = Long.parseLong(cpuInfos[13])
                + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                + Long.parseLong(cpuInfos[16]);
        return appCpuTime;
    }

    private long getAvailMemory() {// 獲取android當前可用記憶體大小

        ActivityManager am = (ActivityManager) mainContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 當前系統的可用記憶體

        return mi.availMem;
    }

    private long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系統記憶體資訊檔
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 讀取meminfo第一行，系統總記憶體大小

            arrayOfString = str2.split("\\s+");

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 獲得系統總記憶體，單位是KB，乘以1024轉換為Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return initial_memory;
    }
}
