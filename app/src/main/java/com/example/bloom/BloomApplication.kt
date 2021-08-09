package com.example.bloom

import android.app.Application
import com.google.firebase.auth.FirebaseUser

class BloomApplication : Application() {
    var currentUser: FirebaseUser? = null
}