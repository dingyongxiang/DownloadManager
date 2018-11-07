package com.yongxiang.downloadapk

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.yongxiang.downloadlibrary.download.DownloadManager
import com.yongxiang.downloadlibrary.inteface.ProgressListener
import kotlinx.android.synthetic.main.activity_main.*


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHelloWorld.setOnClickListener {
            start()
        }
        mSecondTv.visibility = View.GONE
    }


    private var downloadManager: DownloadManager? = null

    private fun start() {
        downloadManager = DownloadManager.getInstance().setContext(this)
                .setDownloadUrl("https://dldir1.qq.com/weixin/android/weixin673android1360.apk")
                .setListener(object : ProgressListener {
                    override fun onDownloading(progress: Int, downloadSize: Long, totalSize: Long) {
//                        Toast.makeText(baseContext, "progress:$progress downloadSize:$downloadSize totalSize:$totalSize", Toast.LENGTH_LONG).show()
                        mHelloWorld.text = "progress:$progress downloadSize:$downloadSize totalSize:$totalSize"
                    }

                    override fun onDownLoadSucc(filePath: String) {
                        Log.d("MainActivity", "下载完成：$filePath")
//                        Toast.makeText(baseContext, "下载完成：$filePath", Toast.LENGTH_LONG).show()
                        mHelloWorld.text = "下载完成：$filePath"
                    }

                })
                .setLifeCycle(lifecycle)
                .startDownload()

    }
}
