package com.example.bloom

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.bloom.databinding.ActivityLogInBinding
import com.example.bloom.repository.LogInRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
    private lateinit var activityLogInBinding: ActivityLogInBinding
    private var createAccountFlow: Boolean = false
    private lateinit var auth: FirebaseAuth
    private lateinit var logInRepository: LogInRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLogInBinding = ActivityLogInBinding.inflate(this.layoutInflater)
        createAccountFlow = intent.getBooleanExtra(WelcomeActivity.CREATE_ACCOUNT, false)
        setContentView(activityLogInBinding.root)
        if (createAccountFlow) {
            activityLogInBinding.logInSubtitle.text = getString(R.string.create_account)
            activityLogInBinding.loginSignUpButton.text = getString(R.string.sign_up)
        }
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
        logInRepository = LogInRepository(this)
        Firebase.initialize(applicationContext)
        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
    }

    private fun launchHomeActivity() {
        val launch = Intent(this, HomeActivity::class.java)
        startActivity(launch)
        finish()
    }

    fun launchNextActivity() {
        if (createAccountFlow) {
            lifecycleScope.launch {
                logInRepository.signUpAccount(
                    activityLogInBinding.emailAddress.text.toString(),
                    activityLogInBinding.password.text.toString()
                ) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        BloomApplication().currentUser = auth.currentUser
                        launchHomeActivity()
                    } else {
                        // If sign up fails, display a message to the user.
                        Log.e(TAG, "createUserWithEmail:failure", task.exception)
                        if (task.exception is FirebaseAuthWeakPasswordException) {
                            Toast.makeText(
                                this@LogInActivity, "Password should be at least 6 characters.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            if (BloomApplication().currentUser != null) {
                launchHomeActivity()
            } else {
                lifecycleScope.launch {
                    logInRepository.signInAccount(
                        activityLogInBinding.emailAddress.text.toString(),
                        activityLogInBinding.password.text.toString()
                    ) { task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "SignIn:success")
                            BloomApplication().currentUser = auth.currentUser!!
                            launchHomeActivity()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signIn:failure", task.exception)
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    baseContext, "Invalid credentials, Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "LogInActivity"
    }
}