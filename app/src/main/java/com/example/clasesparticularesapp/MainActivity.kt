package com.example.clasesparticularesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    }

    fun onStudentClick(view: View) {
        setUserRole("student", StudentActivity::class.java)
    }

    fun onTeacherClick(view: View) {
        setUserRole("teacher", TeacherActivity::class.java)
    }

    private fun setUserRole(role: String, destination: Class<*>) {
        val currentUser = auth.currentUser ?: return

        db.collection("users").document(currentUser.uid)
            .set(mapOf("role" to role))
            .addOnSuccessListener {
                startActivity(Intent(this, destination))
                finish()
            }
            .addOnFailureListener {
                showToast("Error al guardar el rol")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}







