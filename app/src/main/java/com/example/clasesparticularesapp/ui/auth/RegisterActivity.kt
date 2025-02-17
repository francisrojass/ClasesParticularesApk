package com.example.clasesparticularesapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.MainActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val registerButton = findViewById<Button>(R.id.register_button)
        val backToLoginButton = findViewById<Button>(R.id.back_to_login_button) // 🔹 Nuevo botón

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.length < 6) {
                Toast.makeText(this, "Correo inválido o contraseña muy corta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        Log.d("RegisterActivity", "Usuario registrado: ${auth.currentUser?.email}")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        val errorMessage = task.exception?.message ?: "Error desconocido"
                        Log.e("RegisterActivity", "Error en el registro: $errorMessage")
                        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
        }

        backToLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java)) // 🔹 Vuelve al login
            finish()
        }
    }
}


