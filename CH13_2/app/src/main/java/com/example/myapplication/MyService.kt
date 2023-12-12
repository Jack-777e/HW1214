package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*

class MyService : Service() {
    private var channel = ""
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //解析 Intent 取得字串訊息
        intent?.extras?.let {
            channel = it.getString("channel", "")
        }
        broadcast(
            when(channel) {
                "music" -> "歡迎來到音樂頻道"
                "new" -> "歡迎來到新聞頻道"
                "sport" -> "歡迎來到體育頻道"
                else -> "頻道錯誤"
            }
        )
        //若 job 被初始化過且正在運行，則取消它
        if (job.isActive)
            job.cancel()
        coroutineScope.launch {
            try {
                delay(3000) //延遲三秒
                broadcast(
                    when(channel) {
                        "music" -> "即將播放本月 TOP10 音樂"
                        "new" -> "即將為您提供獨家新聞"
                        "sport" -> "即將播報本週 NBA 賽事"
                        else -> "頻道錯誤"
                    }
                )
            } catch (e: CancellationException) {
                e.printStackTrace()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the job when the service is destroyed
    }

    override fun onBind(intent: Intent): IBinder? = null
    //發送廣播
    private fun broadcast(msg: String) =
        sendBroadcast(Intent(channel).putExtra("msg", msg))
}
