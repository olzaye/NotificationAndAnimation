package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.Constant.Companion.DOWNLOADED_FILE_NAME
import com.udacity.Constant.Companion.DOWNLOADED_FILE_STATUS

private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(
    downloadedFile: String,
    status: String,
    applicationContext: Context
) {

    val actionIntent = Intent(applicationContext, DetailActivity::class.java)
    actionIntent.putExtra(DOWNLOADED_FILE_NAME, downloadedFile)
    actionIntent.putExtra(DOWNLOADED_FILE_STATUS, status)

    val actionPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        actionIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_download_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(downloadedFile)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            actionPendingIntent
        )


    notify(NOTIFICATION_ID, builder.build())
}