package com.example.clasesparticularesapp.ui.teacher

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.MainActivity
import com.example.clasesparticularesapp.R
import android.widget.ImageButton
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherActivity : AppCompatActivity() {

    private lateinit var tvBienvenida: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        tvBienvenida = findViewById(R.id.teacher_title)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = auth.currentUser?.uid

        if (userId != null) {
            firestore.collection("profesores").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nombre = document.getString("nombre") ?: ""
                        tvBienvenida.text = "Bienvenido, $nombre"
                    } else {
                        tvBienvenida.text = "Bienvenido"
                    }
                }
                .addOnFailureListener {
                    tvBienvenida.text = "Bienvenido"
                }
        } else {
            tvBienvenida.text = "Bienvenido"
        }

        // Configurar el botón de retroceso
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Manejar el evento de retroceso usando OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@TeacherActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        // ✅ Configurar el botón "Mis Clases"
        val misClasesButton: Button = findViewById(R.id.manage_classes_button) // Asegúrate de que el ID coincide
        misClasesButton.setOnClickListener {
            val intent = Intent(this, MisClasesActivity::class.java)
            startActivity(intent)
        }

        val editarPerfilButton: Button = findViewById(R.id.editarPerfil) // Asegúrate de que el ID coincide
        editarPerfilButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
