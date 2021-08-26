package com.example.bloom

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.bloom.network.BloomApi
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class DownloadImageWork(
    val context: Context,
    workerParameters: WorkerParameters
) :
    CoroutineWorker(context, workerParameters) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        Log.e(TAG, "Starting background work")
        //set notification and update its progress, call the below method again to update the progress
        setForeground(createForegroundInfo(0,context.resources.getStringArray(R.array.download_image_channel)[2]))
        val response =
            BloomApi.retrofitService.downloadImage(inputData.getString(Constants.DOWNLOAD_IMAGE_URL)!!)
        setForeground(createForegroundInfo(15,context.resources.getStringArray(R.array.download_image_channel)[2]))
        val bis: InputStream = BufferedInputStream(response.byteStream(), 1024 * 8)
        val bitmap = BitmapFactory.decodeStream(bis)
        setForeground(createForegroundInfo(25,context.resources.getStringArray(R.array.download_image_channel)[2]))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveBitmapQAndAbove(bitmap, applicationContext)
        } else {
            saveBitmap(bitmap, applicationContext)
        }
        //setting progress to 0 to remove the progress bar
        setForeground(createForegroundInfo(0,context.resources.getStringArray(R.array.download_image_channel)[3]))
        //cancel the notification as the download is complete
        notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID)
        return Result.success()
    }

    private fun createForegroundInfo(progress: Int, contentText: String): ForegroundInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        //We need to set max progress and progress to zero to remove the progress bar
        //setting max progress to 0 to remove the progress bar
        val maxProgress = if (progress == 0) 0 else 100
        //good to use broadcast receiver for notification action as other work can also be done
        //val cancelDownload = WorkManager.getInstance(context).createCancelPendingIntent(id)
        val cancelIntent =
            Intent(
                context,
                DownloadCancelReceiver::class.java
            ).putExtra(Constants.DOWNLOAD_NOTIFICATION_ID, DOWNLOAD_NOTIFICATION_ID)
                .putExtra(Constants.DOWNLOAD_WORK_ID, id)
        val cancelDownloadViaBroadcast =
            PendingIntent.getBroadcast(context, 0, cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val notification = NotificationCompat.Builder(context, IMAGE_DOWNLOAD_CHANNEL)
            .setContentTitle(R.string.app_name.toString())
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.logo)
            .setOngoing(true)
            .setOnlyAlertOnce(true)//notification interrupts the user (with sound, vibration, or visual clues) only the first time the notification appears and not for later updates.
            .setProgress(maxProgress, progress, false)
            .addAction(R.drawable.close, context.getString(R.string.cancel), cancelDownloadViaBroadcast)
            .build()
        return ForegroundInfo(DOWNLOAD_NOTIFICATION_ID, notification)
    }

    private fun saveBitmap(bitmap: Bitmap, applicationContext: Context) {
        val filename = "IMG_${System.currentTimeMillis()}.png"
        val sd: File =
            applicationContext.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)!!
        val dest = File(sd, filename)
        try {
            val out = FileOutputStream(dest)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = context.resources.getStringArray(R.array.download_image_channel)[0]
        val descriptionText =
            context.resources.getStringArray(R.array.download_image_channel)[1]
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(IMAGE_DOWNLOAD_CHANNEL, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun saveBitmapQAndAbove(bitmap: Bitmap, applicationContext: Context) {
        Log.e(TAG, "saveBitmapQAndAbove()..")
        val filename = "IMG_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        val resolver = applicationContext.contentResolver
        val imageUri = resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        setForeground(createForegroundInfo(45,context.resources.getStringArray(R.array.download_image_channel)[2]))
        val outputStream = resolver.openOutputStream(imageUri!!)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        setForeground(createForegroundInfo(65,context.resources.getStringArray(R.array.download_image_channel)[2]))
        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        resolver.update(imageUri, contentValues, null, null)
        setForeground(createForegroundInfo(85,context.resources.getStringArray(R.array.download_image_channel)[2]))
        Log.e(TAG, "saveBitmapQAndAbove()..End")
    }

    companion object {
        private const val TAG = "DownloadImageWork"
        private const val IMAGE_DOWNLOAD_CHANNEL = "1"
        private const val DOWNLOAD_NOTIFICATION_ID = 99
    }
}