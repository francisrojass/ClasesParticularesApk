package com.example.clasesparticularesapp.ui.teacher

import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
//import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
//import com.example.clasesparticularesapp.models.Clase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore

class MisClasesAdapter : AppCompatActivity() {

    //private lateinit var recyclerView: RecyclerView
    //private lateinit var adapter: MisClasesAdapter
    //private val db = FirebaseFirestore.getInstance()
    //private val auth = FirebaseAuth.getInstance()
    //private val clasesList = mutableListOf<Clase>()

    //private lateinit var nombreClase: TextView
    private lateinit var descripcionClaseInput: EditText
    private lateinit var horarioClaseInput: EditText
    //private lateinit var limiteAlumnosInput: EditText
    private lateinit var precioXHoraInput: EditText
    //private lateinit var guardarClase: Button
    //private lateinit var descartarClase: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_clase)

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Inicializar los EditText
        //nombreClase = findViewById(R.id.tvNombreClase)
        descripcionClaseInput = findViewById(R.id.etDescripcion)
        horarioClaseInput = findViewById(R.id.etHorario)
        precioXHoraInput = findViewById(R.id.etPrecioHora)

        //guardarClase = findViewById(R.id.btnGuardarClase)
        //descartarClase = findViewById(R.id.btnGuardarClase)



    }


    /*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clase, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clase = clases[position]
        holder.nombreClase.text = clase.nombre
        holder.horarioClase.text = "Horario: ${clase.horario}"
        //holder.alumnosInscritos.text = "Inscritos: ${clase.alumnosInscritos.size}/${clase.limiteAlumnos}"

        // Acciones de editar y eliminar (Por ahora solo mostramos mensajes)
        holder.btnGuardar.setOnClickListener {
            println("Guardar clase: ${clase.nombre}")
        }

        holder.btnDescartar.setOnClickListener {
            println("Descartar clase: ${clase.nombre}")
        }
    }

    override fun getItemCount() = clases.size
    */
}

