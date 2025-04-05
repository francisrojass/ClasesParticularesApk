package com.example.clasesparticularesapp.ui.teacher

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MisClasesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MisClasesAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val clasesList = mutableListOf<Clase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_clases)

        recyclerView = findViewById(R.id.recyclerViewClases)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MisClasesAdapter(clasesList)
        recyclerView.adapter = adapter

        val fabAgregarClase: FloatingActionButton? = findViewById(R.id.fabAgregarClase)
        fabAgregarClase?.setOnClickListener {
            val intent = Intent(this, NuevaClaseActivity::class.java)
            startActivity(intent)
        }

        cargarClases()
    }

    override fun onResume() {
        super.onResume()
        cargarClases() // Recargar la lista cuando se vuelve a esta actividad
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
            .addOnFailureListener { e ->
                println("Error al cargar las clases: ${e.message}")
                Toast.makeText(this, "Error al cargar las clases: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}