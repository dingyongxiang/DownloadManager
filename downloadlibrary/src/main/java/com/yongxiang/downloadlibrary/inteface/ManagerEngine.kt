package com.yongxiang.downloadlibrary.inteface

import android.arch.lifecycle.Lifecycle

interface ManagerEngine : Engine {

    // 设置下载引擎
    fun setEngine(engine: Engine): Engine

    // 设置生命周期感知组件
    fun setLifeCycle(lifecycle: Lifecycle): Engine
}