package com.example.clasesparticularesapp.ui.teacher

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class DetalleClaseActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_clase)

        val claseId = intent.getStringExtra("claseId") ?: return

        listenerRegistration = db.collection("clases").document(claseId)
            .addSnapshotListener { document, error ->
                if (error != null || document == null || !document.exists()) {
                    findViewById<TextView>(R.id.tvNombre).text = "Error cargando la clase"
                    return@addSnapshotListener
                }

                findViewById<TextView>(R.id.tvNombre).text = "Clase: ${document.getString("nombre")}"
                findViewById<TextView>(R.id.tvAsignatura).text = "Asignatura: ${document.getString("asignatura")}"
                findViewById<TextView>(R.id.tvDescripcion).text = "Descripción: ${document.getString("descripcion")}"
                findViewById<TextView>(R.id.tvFecha).text = "Fecha: ${document.getString("fecha")}"
                findViewById<TextView>(R.id.tvHorario).text = "Horario: ${document.getString("horario")}"
                val limite = document.getLong("limiteAlumnos")?.toInt() ?: 0
                val precio = document.getDouble("precioHora")?.toFloat() ?: 0f
                findViewById<TextView>(R.id.tvLimite).text = "Límite de alumnos: $limite"
                findViewById<TextView>(R.id.tvPrecio).text = "Precio por hora: $precio €"

                val alumnos = document.get("alumnos") as? List<Map<String, Any>>
                val alumnosText = alumnos?.joinToString("\n") {
                    "${it["nombre"] ?: ""} ${it["apellidos"] ?: ""}"
                } ?: "No hay alumnos apuntados"
                findViewById<TextView>(R.id.tvAlumnos).text = "Alumnos:\n$alumnosText"
            }

        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
    }
}
