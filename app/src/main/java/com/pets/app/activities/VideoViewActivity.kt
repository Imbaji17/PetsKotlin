package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.VideoView
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants

class VideoViewActivity : AppCompatActivity() {

    private var videoView: VideoView? = null
    private var url: String? = ""

    companion object {
        private val TAG = VideoViewActivity::class.java.simpleName
        fun startActivity(activity: Activity, url: String) {
            val intent = Intent(activity, VideoViewActivity::class.java)
            intent.putExtra(ApplicationsConstants.DATA, url)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        init()
        initView()
        setValues()
    }

    fun init() {
        url = intent.getStringExtra(ApplicationsConstants.DATA)
    }

    fun initView() {
        videoView = findViewById(R.id.videoView)
    }

    private fun setValues() {
        videoView!!.setVideoPath(url)
        videoView!!.start()
    }
}
