package com.example.clasesparticularesapp.ui.teacher

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class MisClasesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MisClasesAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val clasesList = mutableListOf<Clase>()

    private lateinit var nombreClaseInput: EditText
    private lateinit var descripcionClaseInput: EditText
    private lateinit var horarioClaseInput: EditText
    private lateinit var limiteAlumnosInput: EditText
    private lateinit var btnAgregarClase: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_clase)

        // Inicializar los EditText
        nombreClaseInput = findViewById(R.id.etNombreClase)
        descripcionClaseInput = findViewById(R.id.etDescripcion)
        horarioClaseInput = findViewById(R.id.etHorario)


        // RecyclerView
        recyclerView = findViewById(R.id.recyclerViewClases)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MisClasesAdapter(clasesList)
        recyclerView.adapter = adapter


        // Botón para agregar clase
        btnAgregarClase = findViewById(R.id.btnGuardarClase)
        btnAgregarClase.setOnClickListener {
            agregarClase()
        }

        cargarClases()
    }

    private fun agregarClase() {
        val userId = auth.currentUser?.uid ?: return
        val nombre = nombreClaseInput.text.toString().trim()
        val descripcion = descripcionClaseInput.text.toString().trim()
        val fecha = horarioClaseInput.text.toString().trim()
        val horario = horarioClaseInput.text.toString().trim()
        val limiteAlumnos = limiteAlumnosInput.text.toString().trim().toIntOrNull() ?: 0

        if (nombre.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
            return
        }



        val nuevaClase = Clase(
            id = "",
            nombre = nombre,
            horario = horario,
            limiteAlumnos = limiteAlumnos,
            profesorId = userId
        )

        // Guardar en Firestore
        db.collection("clases").add(nuevaClase)
            .addOnSuccessListener { documentReference ->
                nuevaClase.id = documentReference.id
                clasesList.add(nuevaClase)
                adapter.notifyItemInserted(clasesList.size - 1)
                limpiarCampos()
            }
            .addOnFailureListener {
                println("Error al agregar la clase: ${it.message}")
            }
    }

    private fun cargarClases() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("clases")
            .whereEqualTo("profesorId", userId)
            .get()
            .addOnSuccessListener { documents ->
                clasesList.clear()
                for (document in documents) {
                    val clase = document.toObject<Clase>().copy(id = document.id)
                    clasesList.add(clase)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                println("Error al cargar las clases: ${it.message}")
            }
    }

    private fun limpiarCampos() {
        nombreClaseInput.text.clear()
        descripcionClaseInput.text.clear()
        horarioClaseInput.text.clear()
    }
}
