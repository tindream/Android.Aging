package com.riwise.aging.support;

import android.os.Build;
import android.os.Looper;

public class CrashHandlers implements Thread.UncaughtExceptionHandler {
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    // CrashHandler实例
    private static CrashHandlers instance;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandlers() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public synchronized static CrashHandlers getInstance() {
        if (instance == null) {
            instance = new CrashHandlers();
        }
        return instance;
    }

    public void init() {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Method.log("App发生未处理的异常(" + Build.MODEL + "," + Build.VERSION.RELEASE + ")", ex);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Method.hit("很抱歉,程序出现异常,即将退出.");
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        //让系统默认的异常处理器来处理
        mDefaultHandler.uncaughtException(thread, ex);
    }
}
