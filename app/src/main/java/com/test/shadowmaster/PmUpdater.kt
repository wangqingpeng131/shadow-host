package com.test.shadowmaster

import android.content.Context
import com.tencent.shadow.dynamic.host.PluginManagerUpdater
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.InputStream
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

/**
 * @author Afra55
 * @date 2020/5/25
 * A smile is the best business card.
 */
class PmUpdater(val context:Context) : PluginManagerUpdater {

    companion object{
        val pmApkName = "mp_1.apk"
        val pluginZipName = "plugin-release.zip"

        // PluginManager 文件目录，可自行修改
        fun getPmDirFile(context: Context):File {
            val file = File(context.filesDir, "m")
            if (!file.exists()) {
                file.mkdirs()
            } else {
                if (!file.isDirectory) {
                    file.delete()
                    file.mkdirs()
                }
            }
            return file
        }
        // 插件目录，可自行修改
        fun getPluginDirFile(context: Context):File {
            val file = File(context.filesDir, "p")
            if (!file.exists()) {
                file.mkdirs()
            } else {
                if (!file.isDirectory) {
                    file.delete()
                    file.mkdirs()
                }
            }
            return file
        }
        fun getPluginFile(context: Context):File {
           return File(getPluginDirFile(context), pluginZipName)
        }
    }

    /**
     * 用于拷贝asset 目录下的 PluginManger 文件
     */
    fun preparePlugin() {
        val inputStream = context.assets.open("mp_1.ttf")
        FileUtils.copyInputStreamToFile(inputStream, File(getPmDirFile(context), pmApkName));
    }

    // 测试时使用的 asset 插件包，记得发布时不要在本地放插件包
    fun testAssetZip() {
        val zip: InputStream =
            context.assets.open(pluginZipName)
        FileUtils.copyInputStreamToFile(zip, getPluginFile(context));
    }


    override fun update(): Future<File>? {
        return null
    }

    override fun getLatest(): File? {

        val file = File(getPmDirFile(context), pmApkName)
        return if (file.exists()) {
            file
        } else {
            null
        }

    }

    override fun isAvailable(file: File?): Future<Boolean> {
        return FutureTask {
            try {
                File(getPmDirFile(context), pmApkName).exists()
            } catch (e: Exception) {
                false
            }
        }
    }

    override fun wasUpdating(): Boolean {
        return false
    }
}