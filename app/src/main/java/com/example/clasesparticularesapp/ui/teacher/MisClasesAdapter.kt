package com.example.clasesparticularesapp.ui.teacher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase

class MisClasesAdapter(private val clases: List<Clase>) : RecyclerView.Adapter<MisClasesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreClase: TextView = view.findViewById(R.id.nombreClase)
        val horarioClase: TextView = view.findViewById(R.id.horarioClase)
        val alumnosInscritos: TextView = view.findViewById(R.id.alumnosInscritos)
        val btnEditar: Button = view.findViewById(R.id.btnEditarClase)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminarClase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clase, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clase = clases[position]
        holder.nombreClase.text = clase.nombre
        holder.horarioClase.text = "Horario: ${clase.horario}"
        holder.alumnosInscritos.text = "Inscritos: ${clase.alumnosInscritos.size}/${clase.limiteAlumnos}"

        // Acciones de editar y eliminar (Por ahora solo mostramos mensajes)
        holder.btnEditar.setOnClickListener {
            println("Editar clase: ${clase.nombre}")
        }

        holder.btnEliminar.setOnClickListener {
            println("Eliminar clase: ${clase.nombre}")
        }
    }

    override fun getItemCount() = clases.size
}
