package com.example.clasesparticularesapp.ui.student

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth



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
        adapter = ClassAdapter(clasesList)
        recyclerView.adapter = adapter

        cargarClases()

        findViewById<android.widget.EditText>(R.id.search_bar).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filtrarClases(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        findViewById<ImageView>(R.id.profile_image).setOnClickListener {
            val intent = Intent(this, EditProfileActivityStudent::class.java)
            startActivity(intent)
        }

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()

        }
    }

    private fun cargarClases() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Primero obtenemos las asignaturas seleccionadas por el estudiante
        db.collection("estudiantes").document(uid).get()
            .addOnSuccessListener { documentoEstudiante ->
                val asignaturasSeleccionadas = documentoEstudiante.get("asignaturas") as? List<String>

                if (asignaturasSeleccionadas != null) {
                    // Luego cargamos las clases y las filtramos
                    db.collection("clases").get()
                        .addOnSuccessListener { documents ->
                            clasesList.clear()
                            for (document in documents) {
                                val clase = document.toObject(Clase::class.java)
                                if (clase.asignatura in asignaturasSeleccionadas) {
                                    clasesList.add(clase)
                                }
                            }
                            adapter.actualizarLista(clasesList)
                        }
                }
            }
    }


    private fun filtrarClases(texto: String) {
        val listaFiltrada = clasesList.filter { it.nombre.contains(texto, true) }
        adapter.actualizarLista(listaFiltrada)
    }
}



