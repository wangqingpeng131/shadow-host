package com.test.shadowmaster

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import android.webkit.WebView
import com.tencent.shadow.core.common.LoggerFactory
import com.tencent.shadow.dynamic.host.DynamicRuntime

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LoggerFactory.setILoggerFactory(AndroidLogLoggerFactory())
        if (isProcess(this, ":plugin")) {
            //在全动态架构中，Activity组件没有打包在宿主而是位于被动态加载的runtime，
            //为了防止插件crash后，系统自动恢复crash前的Activity组件，此时由于没有加载runtime而发生classNotFound异常，导致二次crash
            //因此这里恢复加载上一次的runtime
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WebView.setDataDirectorySuffix("plugin")
            }
            DynamicRuntime.recoveryRuntime(this);
        }
    }
    private fun isProcess(context: Context, processName: String): Boolean {
        var currentProcName = ""
        val manager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                currentProcName = processInfo.processName
                break
            }
        }
        return currentProcName.endsWith(processName)
    }

}