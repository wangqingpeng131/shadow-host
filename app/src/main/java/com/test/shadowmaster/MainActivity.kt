package com.test.shadowmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.tencent.shadow.dynamic.host.DynamicPluginManager
import com.tencent.shadow.dynamic.host.EnterCallback

class MainActivity : AppCompatActivity() {
    var pluginManager: DynamicPluginManager? = null
    val pmUpdater: PmUpdater by lazy {
        PmUpdater(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enter(PmUpdater.getPluginFile(this).absolutePath)

    }
    private fun enter(path: String) {
        Log.i("topwise","enter")
        if (pluginManager == null) {
            pmUpdater.preparePlugin()
            pmUpdater.testAssetZip()
            val latest = pmUpdater.latest
            if (latest != null) {
                pluginManager = DynamicPluginManager(pmUpdater)
            }
        }
        checkAndLoadPlugin(path)
    }
    fun checkAndLoadPlugin(pluginZipPath: String) {

        if (pluginManager != null) {
            val bundle = Bundle()
            bundle.putString("p_p", pluginZipPath)
            bundle.putString("part_key", "pp")
            bundle.putString("process_service_name", "${packageName}.PProcessService")
            bundle.putString("app_country_code", "US")
            bundle.putString("app_version", "1.0")
            pluginManager!!.enter(this, 1011, bundle, object : EnterCallback {
                override fun onEnterComplete() {

                    Handler().postDelayed({

                    }, 1000)
                    // progress.visibility = View.GONE

                }

                override fun onShowLoadingView(view: View?) {
                    // progress.visibility = View.VISIBLE
                }

                override fun onCloseLoadingView() {
                    Handler().postDelayed(Runnable {

                    }, 1000)
                }
            })
        }

    }
}