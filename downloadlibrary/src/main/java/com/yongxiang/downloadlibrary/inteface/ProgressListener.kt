package com.yongxiang.downloadlibrary.inteface

interface ProgressListener {

    /**
     * 文件正在下载
     * @param progress 下载进度 0-100
     * @param downloadSize 已下载文件大小
     * @param totalSize 文件总大小
     */
    fun onDownloading(progress: Int, downloadSize: Long, totalSize: Long)

    /**
     * 下载成功
     * @param filePath 文件存储位置
     */
    fun onDownLoadSucc(filePath: String)

}