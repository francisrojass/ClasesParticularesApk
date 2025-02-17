package com.example.clasesparticularesapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.MainActivity
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.auth.AuthManager
import com.example.clasesparticularesapp.ui.student.StudentActivity
import com.example.clasesparticularesapp.ui.teacher.TeacherActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var authManager: AuthManager

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            authManager.handleGoogleSignInResult(result.data) { success, message ->
                if (success) {
                    checkUserRole()
                } else {
                    showToast(message ?: "Error desconocido")
                }
            }
        } else {
            showToast("Inicio de sesión con Google cancelado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        authManager = AuthManager(this)

        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<Button>(R.id.register_button)
        val googleSignInButton = findViewById<Button>(R.id.google_sign_in_button)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                showToast("Por favor, completa todos los campos")
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        checkUserRole()
                    } else {
                        showToast("Error: ${task.exception?.localizedMessage}")
                    }
                }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        googleSignInButton.setOnClickListener {
            authManager.signInWithGoogle { intent ->
                googleSignInLauncher.launch(intent)
            }
        }
    }

    private fun checkUserRole() {
        val currentUser = firebaseAuth.currentUser ?: return

        db.collection("users").document(currentUser.uid).get()
            .addOnSuccessListener { document ->
                val role = document.getString("role")

                when (role) {
                    "student" -> navigateTo(StudentActivity::class.java)
                    "teacher" -> navigateTo(TeacherActivity::class.java)
                    else -> navigateTo(MainActivity::class.java) // 🌟 Primera vez
                }
            }
            .addOnFailureListener {
                showToast("Error al verificar el rol del usuario")
                navigateTo(MainActivity::class.java) // En caso de error, que elija
            }
    }

    private fun navigateTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}