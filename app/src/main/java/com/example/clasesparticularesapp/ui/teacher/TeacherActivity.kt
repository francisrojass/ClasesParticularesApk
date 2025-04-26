package com.example.clasesparticularesapp.ui.teacher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.MainActivity
import com.example.clasesparticularesapp.R
import android.widget.ImageButton
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.Glide
import android.util.Log

class TeacherActivity : AppCompatActivity() {

    private lateinit var tvBienvenida: TextView
    private lateinit var imageViewFotoPerfil: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        Log.d("TeacherActivity", "onCreate() llamado")

        tvBienvenida = findViewById(R.id.teacher_title)
        imageViewFotoPerfil = findViewById(R.id.imgPerfil)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = auth.currentUser?.uid
        cargarDatosPerfil(userId)

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val misClasesButton: Button = findViewById(R.id.manage_classes_button)
        misClasesButton.setOnClickListener {
            val intent = Intent(this, MisClasesActivity::class.java)
            startActivity(intent)
        }

        val editarPerfilButton: Button = findViewById(R.id.editarPerfil)
        editarPerfilButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TeacherActivity", "onResume() llamado")
        val userId = auth.currentUser?.uid
        cargarDatosPerfil(userId)
    }

    private fun cargarDatosPerfil(userId: String?) {
        if (userId != null) {
            firestore.collection("profesores").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nombre = document.getString("nombre") ?: ""
                        val fotoUrl = document.getString("fotoUrl") ?: ""
                        tvBienvenida.text = "Bienvenido, $nombre"

                        // Cargar foto de perfil con Glide
                        if (fotoUrl.isNotEmpty()) {
                            Glide.with(this)
                                .load(fotoUrl)
                                .placeholder(R.drawable.ic_profile_placeholder)
                                .error(R.drawable.ic_profile_placeholder)
                                .into(imageViewFotoPerfil)
                        } else {
                            imageViewFotoPerfil.setImageResource(R.drawable.ic_profile_placeholder)
                        }
                    } else {
                        tvBienvenida.text = "Bienvenido"
                        imageViewFotoPerfil.setImageResource(R.drawable.ic_profile_placeholder)
                    }
                }
                .addOnFailureListener {
                    tvBienvenida.text = "Bienvenido"
                    imageViewFotoPerfil.setImageResource(R.drawable.ic_profile_placeholder)
                }
        } else {
            tvBienvenida.text = "Bienvenido"
            imageViewFotoPerfil.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }
}
