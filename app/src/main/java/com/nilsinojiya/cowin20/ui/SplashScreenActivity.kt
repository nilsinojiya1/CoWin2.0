package com.nilsinojiya.cowin20.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.nilsinojiya.cowin20.MainActivity
import com.nilsinojiya.cowin20.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        var aniFade = AnimationUtils.loadAnimation(applicationContext,R.anim.fade_in)
        tvTitle.startAnimation(aniFade)
        tvCaption.startAnimation(aniFade)


        Handler().postDelayed({
         startActivity(Intent(this, MainActivity::class.java))
         finish()
        }, 6000)

    }
}