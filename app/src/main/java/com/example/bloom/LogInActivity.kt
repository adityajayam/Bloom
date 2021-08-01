package com.example.bloom

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.bloom.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {
    private lateinit var activityLogInBinding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLogInBinding = ActivityLogInBinding.inflate(this.layoutInflater)
        setContentView(activityLogInBinding.root)
        activityLogInBinding.apply {
            logInActivity = this@LogInActivity
            lifecycleOwner = lifecycleOwner
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorOnSecondary)
        }
    }

    fun launchHomeActivity() {
        val launch = Intent(this, HomeActivity::class.java)
        startActivity(launch)
    }
}