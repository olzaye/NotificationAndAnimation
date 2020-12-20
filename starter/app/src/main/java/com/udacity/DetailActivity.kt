package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.Constant.Companion.DOWNLOADED_FILE_NAME
import com.udacity.Constant.Companion.DOWNLOADED_FILE_STATUS
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        intent.let {
            file_name_text_view.text = it.getStringExtra(DOWNLOADED_FILE_NAME)
            status_text_view.text = it.getStringExtra(DOWNLOADED_FILE_STATUS)
        }
    }
}
