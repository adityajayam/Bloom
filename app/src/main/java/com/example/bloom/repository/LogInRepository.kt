package com.example.bloom.repository

import android.app.Activity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogInRepository(private val activity: Activity) {
    private var auth: FirebaseAuth

    init {
        Firebase.initialize(activity.applicationContext)
        auth = FirebaseAuth.getInstance()
    }

    suspend fun signUpAccount(
        email: String,
        password: String,
        listener: (Task<AuthResult>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(
                email,
                password
            ).addOnCompleteListener(activity, OnCompleteListener { task ->
                listener.invoke(task)
            })
        }
    }

    suspend fun signInAccount(
        email: String,
        password: String,
        listener: (Task<AuthResult>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(
                email,
                password
            ).addOnCompleteListener(activity, OnCompleteListener { task ->
                listener.invoke(task)
            })
        }
    }

    companion object {
        private const val TAG = "LogInRepository"
    }
}