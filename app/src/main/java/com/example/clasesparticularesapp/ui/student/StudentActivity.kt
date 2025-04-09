package com.example.clasesparticularesapp.ui.student

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase
import com.example.clasesparticularesapp.models.Profesor
import com.google.firebase.firestore.FirebaseFirestore

class StudentActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClassAdapter
    private val db = FirebaseFirestore.getInstance()
    private var clasesList = mutableListOf<Clase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        recyclerView = findViewById(R.id.recycler_view_professors)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ClassAdapter(clasesList)  // Aquí usas el adaptador de clases
        recyclerView.adapter = adapter

        cargarClases()  // Llamamos a cargar las clases desde Firestore

        findViewById<android.widget.EditText>(R.id.search_bar).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filtrarClases(s.toString())  // Filtrar clases en vez de profesores
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun cargarClases() {
        db.collection("clases")
            .get()
            .addOnSuccessListener { documents ->
                clasesList.clear()  // Puedes renombrar esta lista si la utilizas para clases
                for (document in documents) {
                    val clase = document.toObject(Clase::class.java) // Cambiar Profesor a ClassParticular
                    clasesList.add(clase) // También puedes renombrar esta lista a clasesList
                }
                adapter.actualizarLista(clasesList) // Actualizar el adapter con las clases
            }
    }

    private fun filtrarClases(texto: String) {
        val listaFiltrada = clasesList.filter { it.nombre.contains(texto, true) }
        adapter.actualizarLista(listaFiltrada)
    }
}




