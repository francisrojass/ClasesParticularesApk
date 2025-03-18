package com.example.clasesparticularesapp.ui.student

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Profesor
import com.google.firebase.firestore.FirebaseFirestore

class StudentActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfesorAdapter
    private val db = FirebaseFirestore.getInstance()
    private var profesoresList = mutableListOf<Profesor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        recyclerView = findViewById(R.id.recycler_view_professors)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProfesorAdapter(profesoresList)
        recyclerView.adapter = adapter

        cargarProfesores()

        findViewById<android.widget.EditText>(R.id.search_bar).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filtrarProfesores(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun cargarProfesores() {
        db.collection("profesores")
            .get()
            .addOnSuccessListener { documents ->
                profesoresList.clear()
                for (document in documents) {
                    val profesor = document.toObject(Profesor::class.java)
                    profesoresList.add(profesor)
                }
                adapter.actualizarLista(profesoresList)
            }
    }

    private fun filtrarProfesores(texto: String) {
        val listaFiltrada = profesoresList.filter { it.nombre.contains(texto, true) }
        adapter.actualizarLista(listaFiltrada)
    }
}




