package com.yongxiang.downloadapk

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yongxiang.downloadlibrary.download.DownloadManager
import com.yongxiang.downloadlibrary.inteface.ProgressListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHelloWorld.setOnClickListener {
            start()
        }
        mSecondTv.setOnClickListener {
            startActivity(Intent(this,SecondActivity::class.java))
        }
    }


    private var downloadManager: DownloadManager? = null

    private fun start() {
        DownloadManager.getInstance().setContext(this)
                .setDownloadUrl("https://dldir1.qq.com/weixin/android/weixin673android1360.apk")
                .setListener(object : ProgressListener {
                    override fun onDownloading(progress: Int, downloadSize: Long, totalSize: Long) {
                        mHelloWorld.text = "progress:$progress downloadSize:$downloadSize totalSize:$totalSize"
                    }
                    override fun onDownLoadSucc(filePath: String) {
                        Log.d("MainActivity", "下载完成：$filePath")
                        mHelloWorld.text = "下载完成：$filePath"
                    }

                })
                .setLifeCycle(lifecycle)
                .startDownload()

    }
}
