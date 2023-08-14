package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class splashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
       //binding = splashActivityBinding.inflate(layoutInflater)
       // setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        actionBar?.hide()
        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent= Intent(this,homeActivity::class.java)
            startActivity(intent)
            finish()
        },3000)

    }
}