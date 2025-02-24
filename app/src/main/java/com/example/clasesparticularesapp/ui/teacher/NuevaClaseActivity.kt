package com.example.clasesparticularesapp.ui.teacher

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NuevaClaseActivity : AppCompatActivity() {

    private lateinit var etNombreClase: EditText
    private lateinit var etHorario: EditText
    private lateinit var etLimiteAlumnos: EditText
    private lateinit var btnGuardarClase: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_clase)

        etNombreClase = findViewById(R.id.etNombreClase)
        etHorario = findViewById(R.id.etHorario)
        etLimiteAlumnos = findViewById(R.id.etLimiteAlumnos)
        btnGuardarClase = findViewById(R.id.btnGuardarClase)

        btnGuardarClase.setOnClickListener {
            guardarClase()
        }
    }

    private fun guardarClase() {
        val nombre = etNombreClase.text.toString().trim()
        val horario = etHorario.text.toString().trim()
        val limiteAlumnos = etLimiteAlumnos.text.toString().trim().toIntOrNull()

        if (nombre.isEmpty() || horario.isEmpty() || limiteAlumnos == null) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevaClase = hashMapOf(
            "nombre" to nombre,
            "horario" to horario,
            "limiteAlumnos" to limiteAlumnos,
            "profesorId" to userId
        )

        db.collection("clases").add(nuevaClase)
            .addOnSuccessListener {
                Toast.makeText(this, "Clase guardada correctamente", Toast.LENGTH_SHORT).show()
                finish() // Cierra la actividad tras guardar
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar la clase", Toast.LENGTH_SHORT).show()
            }
    }
}
