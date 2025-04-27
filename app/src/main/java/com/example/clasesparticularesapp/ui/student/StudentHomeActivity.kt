package com.example.clasesparticularesapp.ui.student

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.clasesparticularesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class StudentHomeActivity : AppCompatActivity() {

    private lateinit var imagenPerfil: ImageView
    private lateinit var btnBuscarClases: Button
    private lateinit var btnPerfil: Button
    private lateinit var backButton: ImageButton // Declara la variable para el botón de retroceso

    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

        imagenPerfil = findViewById(R.id.imagenPerfil)
        btnBuscarClases = findViewById(R.id.btnBuscarClases)
        btnPerfil = findViewById(R.id.btnPerfil)
        backButton = findViewById(R.id.back_button) // Inicializa el botón de retroceso

        // Cargar imagen de perfil (si está guardada en Firestore o Storage)
        uid?.let {
            db.collection("estudiantes").document(it).get()
                .addOnSuccessListener { doc ->
                    val url = doc.getString("fotoPerfilUrl")
                    if (!url.isNullOrEmpty()) {
                        Glide.with(this).load(url).into(imagenPerfil)
                    }
                }
        }

        btnBuscarClases.setOnClickListener {
            val intent = Intent(this, StudentActivity::class.java)
            startActivity(intent)
            // Opcional: finish() si no quieres que StudentHomeActivity quede en la pila al ir a StudentActivity
        }

        btnPerfil.setOnClickListener {
            val intent = Intent(this, EditProfileActivityStudent::class.java)
            startActivity(intent)
            // Opcional: finish() si no quieres que StudentHomeActivity quede en la pila al ir a EditProfileActivityStudent
        }

        // Configurar el clic para el botón de retroceso
        backButton.setOnClickListener {
            onBackPressed() // Llama a onBackPressed() para volver a la actividad anterior
        }
    }

    // Puedes anular onBackPressed si necesitas lógica adicional antes de volver
    // override fun onBackPressed() {
    //     // Lógica adicional aquí si es necesario
    //     super.onBackPressed() // Llama a la implementación por defecto
    // }
}
