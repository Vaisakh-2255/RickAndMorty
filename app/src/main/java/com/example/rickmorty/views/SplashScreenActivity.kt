package com.example.rickmorty.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.rickmorty.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val SPLASH_TIME_OUT: Long = 3600

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreenActivity, LoginPageActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}
