package com.example.bloom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bloom.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var activityWelcomeBinding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWelcomeBinding = ActivityWelcomeBinding.inflate(this.layoutInflater)
        setContentView(activityWelcomeBinding.root)
        activityWelcomeBinding.apply {
            welcomeActivity = this@WelcomeActivity
            lifecycleOwner = lifecycleOwner
        }
    }

    fun launchLoginActivity() {
        val launch = Intent(this, LogIn::class.java)
        startActivity(launch)
    }
}