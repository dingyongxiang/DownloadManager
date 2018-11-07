package com.yongxiang.downloadlibrary.inteface

interface ProgressListener {

    fun onDownloading(progress: Int, downloadSize: Long, totalSize: Long)

    fun onDownLoadSucc(filePath: String)

}