package com.zx.mvvmdemo.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zx.mvvmdemo.R
import com.zx.mvvmdemo.module.OPENGLTestActivity
import kotlinx.android.synthetic.main.activity_sample.*

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        vr_button.setOnClickListener {
            startActivity(Intent(this@SampleActivity, OPENGLTestActivity::class.java))
        }
    }
}
