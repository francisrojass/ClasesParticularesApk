package com.example.clasesparticularesapp.ui.teacher

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.MainActivity
import com.example.clasesparticularesapp.R
import android.widget.ImageButton

class TeacherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        // Configurar el botón de retroceso
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Manejar el evento de retroceso usando OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Inicia MainActivity y finaliza la actividad actual
                val intent = Intent(this@TeacherActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}



