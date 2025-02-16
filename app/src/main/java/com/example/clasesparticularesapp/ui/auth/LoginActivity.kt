package com.example.clasesparticularesapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.auth.AuthManager
import com.example.clasesparticularesapp.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var firebaseAuth: FirebaseAuth

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            authManager.signInWithGoogle(data!!) { success, message ->
                if (success) {
                    goToMainActivity() // 🔹 Ir a MainActivity si el login es exitoso
                } else {
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authManager = AuthManager(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // 🔹 Verifica si el usuario ya ha iniciado sesión
        if (firebaseAuth.currentUser != null) {
            goToMainActivity()
        }

        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)
        val googleButton = findViewById<Button>(R.id.google_sign_in_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authManager.loginUser(email, password) { success, message ->
                if (success) {
                    goToMainActivity()
                } else {
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }

        googleButton.setOnClickListener {
            val signInIntent = authManager.getGoogleSignInClient().signInIntent
            googleSignInLauncher.launch(signInIntent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // 🔹 Cierra `LoginActivity` para que el usuario no pueda volver atrás
    }
}



