package com.example.clasesparticularesapp.ui.teacher

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.example.clasesparticularesapp.R


class DetalleClaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_clase)

        val nombre = intent.getStringExtra("nombre")
        val asignatura = intent.getStringExtra("asignatura")
        val descripcion = intent.getStringExtra("descripcion")
        val fecha = intent.getStringExtra("fecha")
        val horario = intent.getStringExtra("horario")
        val limite = intent.getIntExtra("limiteAlumnos", 0)
        val precio = intent.getFloatExtra("precioHora", 0f)

        findViewById<TextView>(R.id.tvNombre).text = "Clase: $nombre"
        findViewById<TextView>(R.id.tvAsignatura).text = "Asignatura: $asignatura"
        findViewById<TextView>(R.id.tvDescripcion).text = "Descripción: $descripcion"
        findViewById<TextView>(R.id.tvFecha).text = "Fecha: $fecha"
        findViewById<TextView>(R.id.tvHorario).text = "Horario: $horario"
        findViewById<TextView>(R.id.tvLimite).text = "Límite de alumnos: $limite"
        findViewById<TextView>(R.id.tvPrecio).text = "Precio por hora: $precio €"


        val backButton: ImageButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            // O puedes usar la forma explícita:
            // finish()
        }
    }
}
