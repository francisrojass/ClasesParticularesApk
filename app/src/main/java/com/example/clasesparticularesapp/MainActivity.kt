package com.example.clasesparticularesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.clasesparticularesapp.ui.auth.LoginActivity
import com.example.clasesparticularesapp.ui.student.StudentActivity
import com.example.clasesparticularesapp.ui.teacher.TeacherActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar el botón de Sign Out
        val signOutButton: Button = findViewById(R.id.sign_out_button)
        signOutButton.setOnClickListener {
            signOut()
        }

        // Configurar la navegación manualmente sin depender de XML
        val studentCard: CardView = findViewById(R.id.student_card)
        val teacherCard: CardView = findViewById(R.id.teacher_card)  // Utiliza CardView

        studentCard.setOnClickListener {
            showToast("Clic en estudiante")
            navigateToActivity(StudentActivity::class.java)
            setUserRole("student")
        }

        teacherCard.setOnClickListener {
            showToast("Clic en profesor")
            navigateToActivity(TeacherActivity::class.java)
            setUserRole("teacher")
        }
    }

    private fun setUserRole(role: String) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            showToast("Usuario no autenticado")
            return
        }

        showToast("Guardando rol: $role")

        db.collection("users").document(currentUser.uid)
            .set(mapOf("role" to role))
            .addOnSuccessListener {
                showToast("Rol guardado: $role")
            }
            .addOnFailureListener {
                showToast("Error al guardar el rol en Firestore")
            }
    }

    private fun navigateToActivity(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }

    private fun signOut() {
        auth.signOut()
        showToast("Sesión cerrada")
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}











