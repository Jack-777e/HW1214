package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*

class MyService : Service() {
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()
        coroutineScope.launch {
            delay(5000)
            val intent = Intent(this@MyService, MainActivity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this@MyService.startActivity(intent)
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the job when the service is destroyed
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }
}
