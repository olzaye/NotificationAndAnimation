package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private val notificationManager: NotificationManager by lazy {
        ContextCompat.getSystemService(this, NotificationManager::class.java)
                as NotificationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(
            getString(R.string.notification_download_channel_id),
            getString(R.string.notification_download_channel_name)
        )
        custom_button.setOnClickListener {
            when (download_radio_group.checkedRadioButtonId) {
                R.id.first_radio_button -> download(GLIDE_ZIP_URL)
                R.id.second_radio_button -> download(UDACITY_URL)
                R.id.third_radio_button -> download(RETROFIT_ZIP_URL)
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.not_selected_error_message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)?.let { id ->
                val query = DownloadManager.Query().setFilterById(id)

                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor = downloadManager.query(query)
                var status = FileStatus.EMPTY

                if (cursor.moveToFirst()) {
                    status =
                        when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                            DownloadManager.STATUS_SUCCESSFUL -> FileStatus.SUCCESS
                            else -> FileStatus.FAIL
                        }
                }
                context?.let {
                    notificationManager.sendNotification(
                        NotificationItem(getCheckedRadioButtonText(), status),
                        it
                    )
                }
            }
        }
    }

    private fun getCheckedRadioButtonText(): String {
        return when (download_radio_group.checkedRadioButtonId) {
            R.id.first_radio_button -> first_radio_button.text.toString()
            R.id.second_radio_button -> second_radio_button.text.toString()
            R.id.third_radio_button -> third_radio_button.text.toString()
            else -> {
                "Unknown file name"
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(false)
                enableLights(true)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val GLIDE_ZIP_URL = "https://github.com/bumptech/glide"
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
        private const val RETROFIT_ZIP_URL = "https://github.com/square/retrofit"
    }
}
