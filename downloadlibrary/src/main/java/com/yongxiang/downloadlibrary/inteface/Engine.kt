package com.yongxiang.downloadlibrary.inteface

import android.content.Context

interface Engine {

    // 设置下载地址
    fun setDownloadUrl(url: String): Engine

    // 设置上下文
    fun setContext(context: Context): Engine

    // 设置下载监听
    fun setListener(listener: ProgressListener): Engine

    // 开始下载
    fun startDownload(): Engine

    // 页面销毁
    fun onDestroy()

}