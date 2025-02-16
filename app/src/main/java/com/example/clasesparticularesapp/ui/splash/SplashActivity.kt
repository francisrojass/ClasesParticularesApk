package com.example.clasesparticularesapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.ui.auth.LoginActivity
import com.example.clasesparticularesapp.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Asegura que Firebase está inicializado
        setContentView(R.layout.activity_splash)

        checkUserAuthentication()
    }

    private fun checkUserAuthentication() {
        Handler(Looper.getMainLooper()).postDelayed({
            val auth = FirebaseAuth.getInstance()
            val nextActivity = if (auth.currentUser != null) {
                MainActivity::class.java
            } else {
                LoginActivity::class.java
            }

            startActivity(Intent(this, nextActivity))
            finish()
        }, 2000)
    }
}

