package com.example.clasesparticularesapp.ui.student

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView // Importa TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.clasesparticularesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log // Importa Log

class StudentHomeActivity : AppCompatActivity() {

    private lateinit var tvBienvenida: TextView // Declara el TextView para la bienvenida
    private lateinit var imagenPerfil: ImageView
    private lateinit var btnBuscarClases: Button
    private lateinit var btnPerfil: Button
    private lateinit var backButton: ImageButton

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance() // Usa auth para obtener el UID
    private val uid = auth.currentUser?.uid // Obtén el UID aquí

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)
        Log.d("StudentHomeActivity", "onCreate() llamado") // Log para depuración

        tvBienvenida = findViewById(R.id.tvBienvenidaEstudiante) // Inicializa el TextView
        imagenPerfil = findViewById(R.id.imagenPerfil)
        btnBuscarClases = findViewById(R.id.btnBuscarClases)
        btnPerfil = findViewById(R.id.btnPerfil)
        backButton = findViewById(R.id.back_button)

        // Cargar datos del estudiante (nombre y foto)
        cargarDatosEstudiante(uid)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnBuscarClases.setOnClickListener {
            val intent = Intent(this, StudentActivity::class.java)
            startActivity(intent)
            // Opcional: finish() si no quieres que StudentHomeActivity quede en la pila
        }

        btnPerfil.setOnClickListener {
            val intent = Intent(this, EditProfileActivityStudent::class.java)
            startActivity(intent)
            // Opcional: finish() si no quieres que StudentHomeActivity quede en la pila
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("StudentHomeActivity", "onResume() llamado") // Log para depuración
        // Vuelve a cargar los datos por si se editaron en EditProfileActivityStudent
        cargarDatosEstudiante(uid)
    }

    private fun cargarDatosEstudiante(userId: String?) {
        if (userId != null) {
            db.collection("estudiantes").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nombre = document.getString("nombre") ?: ""
                        val fotoUrl = document.getString("fotoPerfilUrl") ?: ""

                        // Actualizar el TextView de bienvenida
                        tvBienvenida.text = if (nombre.isNotEmpty()) "Bienvenido, $nombre" else "Bienvenido"
                        Log.d("StudentHomeActivity", "Nombre estudiante cargado: $nombre")

                        // Cargar foto de perfil con Glide
                        if (fotoUrl.isNotEmpty()) {
                            Glide.with(this)
                                .load(fotoUrl)
                                .placeholder(R.drawable.ic_profile_placeholder) // Placeholder mientras carga
                                .error(R.drawable.ic_profile_placeholder) // Imagen si falla la carga
                                .into(imagenPerfil)
                            Log.d("StudentHomeActivity", "Foto estudiante cargada: $fotoUrl")
                        } else {
                            imagenPerfil.setImageResource(R.drawable.ic_profile_placeholder)
                            Log.d("StudentHomeActivity", "No se encontró URL de foto para estudiante")
                        }
                    } else {
                        // Documento no existe, mostrar mensaje y foto por defecto
                        tvBienvenida.text = "Bienvenido"
                        imagenPerfil.setImageResource(R.drawable.ic_profile_placeholder)
                        Log.d("StudentHomeActivity", "Documento de estudiante no encontrado en Firestore")
                    }
                }
                .addOnFailureListener { exception ->
                    // Error al cargar los datos, mostrar mensaje y foto por defecto
                    tvBienvenida.text = "Bienvenido"
                    imagenPerfil.setImageResource(R.drawable.ic_profile_placeholder)
                    Log.e("StudentHomeActivity", "Error al cargar datos del estudiante", exception)
                }
        } else {
            // UID es nulo, mostrar mensaje y foto por defecto
            tvBienvenida.text = "Bienvenido"
            imagenPerfil.setImageResource(R.drawable.ic_profile_placeholder)
            Log.d("StudentHomeActivity", "UID de usuario nulo")
        }
    }
}
