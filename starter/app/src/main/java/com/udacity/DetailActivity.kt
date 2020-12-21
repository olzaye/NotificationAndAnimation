package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.Constant.Companion.NOTIFICATION_ITEM
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)

        setSupportActionBar(toolbar)
        cancelNotification(this)

        binding.contentDetailLayout.motionLayout.transitionToStart()

        intent?.getParcelableExtra<NotificationItem>(NOTIFICATION_ITEM)?.let {
            binding.contentDetailLayout.notificationItem = it
        }

        binding.contentDetailLayout.button.setOnClickListener {
            onBackPressed()
        }
    }
}
