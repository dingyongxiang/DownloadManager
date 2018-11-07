package com.yongxiang.downloadlibrary.download

import android.arch.lifecycle.*
import android.content.Context
import android.util.Log
import com.yongxiang.downloadlibrary.inteface.Engine
import com.yongxiang.downloadlibrary.inteface.ManagerEngine
import com.yongxiang.downloadlibrary.inteface.ProgressListener
import java.lang.ref.WeakReference

class DownloadManager : ManagerEngine, LifecycleObserver {

    private var lifecycle: Lifecycle? = null

    private var mDownLoadEngine: Engine? = null

    private var url: String? = null

    private var context: WeakReference<Context>? = null

    private var listener: ProgressListener? = null

    override fun setLifeCycle(lifecycle: Lifecycle): DownloadManager {
        this.lifecycle = lifecycle
        lifecycle.addObserver(this)
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        Log.d("DownloadManager", "onDestroy")
        mDownLoadEngine?.onDestroy()
        lifecycle?.removeObserver(this)
        context = null
    }


    override fun setEngine(engine: Engine): DownloadManager {
        mDownLoadEngine = engine
        return this
    }

    override fun setDownloadUrl(url: String): DownloadManager {
        this.url = url
        return this
    }

    override fun setContext(context: Context): DownloadManager {
        this.context = WeakReference(context)
        return this
    }

    override fun setListener(listener: ProgressListener): DownloadManager {
        this.listener = listener
        return this
    }

    override fun startDownload(): DownloadManager {
        if (mDownLoadEngine == null) {
            mDownLoadEngine = DefaultDownloadEngine()
        }
        context?.let {
            it.get()?.let {
                mDownLoadEngine?.setContext(it)
            }
        }
        listener?.let {
            mDownLoadEngine?.setListener(it)
        }
        url?.let {
            mDownLoadEngine?.setDownloadUrl(it)
        }
        mDownLoadEngine?.startDownload()
        return this
    }

    companion object {
        fun getInstance() = DownloadManager()
    }


}