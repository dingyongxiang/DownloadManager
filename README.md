# DownloadManager
  Android下载工具
  
  默认使用Android原生的DownloadManager下载文件
  
  你也可以使用自定义的下载引擎，只需要继承Engine类
  - 使用方法：
  ```
   DownloadManager.getInstance()
                  //设置上下文
                  .setContext(this)
                  //设置下载地址
                  .setDownloadUrl("https://dldir1.qq.com/weixin/android/weixin673android1360.apk")
                  //设置下载进度监听
                  .setListener(object : ProgressListener {
                      override fun onDownloading(progress: Int, downloadSize: Long, totalSize: Long) {
                          
                      }
  
                      override fun onDownLoadSucc(filePath: String) {
                          
                      }
  
                  })
                  //添加生命周期观测功能 用于在页面销毁时销毁对象
                  .setLifeCycle(lifecycle)
                  .startDownload()
```
 - 自定义下载引擎：
 
   1.让你的下载引擎继承Engine类
 
   2.设置下载引擎
 ```
  DownloadManager.getInstance()
                 .setEngine(engine)
 ```
