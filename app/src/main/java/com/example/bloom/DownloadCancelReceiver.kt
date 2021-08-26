package com.example.bloom

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.WorkManager
import java.util.*

class DownloadCancelReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "DownloadCancelReceiver onReceive()..")
        val downloadWordUUid = intent?.getSerializableExtra(Constants.DOWNLOAD_WORK_ID)
        WorkManager.getInstance(context!!)
            .cancelWorkById(downloadWordUUid as UUID)
        val downloadNotificationId = intent.getIntExtra(Constants.DOWNLOAD_NOTIFICATION_ID, 0)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(downloadNotificationId)
    }

    companion object {
        private const val TAG = "DownloadCancelReceiver"
    }
}