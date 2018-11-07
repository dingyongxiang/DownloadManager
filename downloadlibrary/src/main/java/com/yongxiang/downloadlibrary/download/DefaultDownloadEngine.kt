package com.yongxiang.downloadlibrary.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import com.yongxiang.downloadlibrary.FileAttribute
import com.yongxiang.downloadlibrary.inteface.Engine
import com.yongxiang.downloadlibrary.inteface.ProgressListener


class DefaultDownloadEngine : Engine {

    private var mContext: Context? = null
    private var refernece: Long = 0
    private var receiver: BroadcastReceiver? = null
    private var mListener: ProgressListener? = null
    private var mDownLoadChangeObserver: DownloadChangeObserver? = null
    private lateinit var mDownloadManager: DownloadManager
    private lateinit var mDownloadPath: String
    private lateinit var mDownloadUrl: String

    override fun setDownloadUrl(url: String): Engine {
        mDownloadUrl = url
        return this
    }

    override fun setContext(context: Context): Engine {
        mContext = context
        return this
    }

    override fun setListener(listener: ProgressListener): Engine {
        mListener = listener
        return this
    }

    override fun startDownload(): Engine {
        if (mContext == null) {
            throw NullPointerException("context is  null , please set context !")
        }
        initListener()
        initDownManager()
        return this
    }

    override fun onDestroy() {
        receiver.let {
            mContext?.unregisterReceiver(receiver)
        }

        mDownLoadChangeObserver?.let {
            mContext?.contentResolver?.unregisterContentObserver(mDownLoadChangeObserver)
        }
        mContext = null
        mListener = null
        receiver = null
        mDownLoadChangeObserver = null
    }

    private fun initDownManager() {

        mDownloadManager = mContext?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(mDownloadUrl)
        val request = DownloadManager.Request(uri)
        // 设置下载路径和文件名
        val fileName = System.currentTimeMillis().toString() + ".apk"
        val dirName = mContext?.cacheDir?.absolutePath
        mDownloadPath = "$dirName/$fileName"
        //设置文件存放目录
        request.setDestinationInExternalPublicDir(dirName, fileName)
        request.setDescription("正在下载")
        request.setTitle(mContext?.packageName)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setMimeType("application/vnd.android.package-archive")
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner()
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true)
        // 获取此次下载的ID
        refernece = mDownloadManager.enqueue(request)
        // 注册广播接收器，当下载完成时自动安装
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (refernece == myDwonloadID) {
                    mListener?.onDownLoadSucc(mDownloadPath)
                    mContext?.unregisterReceiver(receiver)
                }
            }
        }
        mContext?.registerReceiver(receiver, filter)
    }


    private fun initListener() {
        mDownLoadChangeObserver = DownloadChangeObserver(Handler())
        mContext?.contentResolver?.registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, mDownLoadChangeObserver)
    }


    // 查询下载进度，文件总大小多少，已经下载多少？
    private fun query(): FileAttribute {
        val bytesAndStatus = longArrayOf(0, 0, 0)
        val query = DownloadManager.Query().setFilterById(refernece)
        var c: Cursor? = null
        var progress = 0
        try {
            c = mDownloadManager.query(query)
            if (c != null && c.moveToFirst()) {
                //已经下载的字节数
                bytesAndStatus[0] = c.getLong(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                //总需下载的字节数
                bytesAndStatus[1] = c.getLong(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                //状态所在的列索引
                bytesAndStatus[2] = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
                progress = (bytesAndStatus[0] / bytesAndStatus[1].toFloat() * 100).toInt()
            }
        } finally {
            c?.close()
        }
        return FileAttribute(progress, bytesAndStatus[0], bytesAndStatus[1])
    }


    // 下载监听回调
    internal inner class DownloadChangeObserver(handler: Handler) : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            val value = query()
            mListener?.onDownloading(value.progress, value.downloadSize, value.totalSize)
        }
    }

}