package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.Constant.Companion.NOTIFICATION_ITEM
import kotlinx.android.parcel.Parcelize

private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(
    notificationItem: NotificationItem,
    applicationContext: Context
) {

    val actionIntent = Intent(applicationContext, DetailActivity::class.java)
    actionIntent.flags = FLAG_ACTIVITY_NEW_TASK
    actionIntent.putExtra(NOTIFICATION_ITEM, notificationItem)
    val actionPendingIntent = TaskStackBuilder.create(applicationContext).run {
        addNextIntentWithParentStack(actionIntent)
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_download_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setAutoCancel(true)
        .setContentText(notificationItem.fileName)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            actionPendingIntent
        )


    notify(NOTIFICATION_ID, builder.build())
}

fun cancelNotification(context: Context) {
    (ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager).apply {
        cancelAll()
    }
}

@Parcelize
data class NotificationItem(
    val fileName: String = "",
    val fileStatus: FileStatus = FileStatus.EMPTY
) : Parcelable

enum class FileStatus(private val value: String) {
    SUCCESS("Success"),
    FAIL("Fail"),
    EMPTY("");

    override fun toString(): String {
        return value
    }
}
