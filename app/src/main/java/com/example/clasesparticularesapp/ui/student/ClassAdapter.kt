package com.example.clasesparticularesapp.ui.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase

class ClassAdapter(private val clases: List<Clase>) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profesor, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val clase = clases[position]
        holder.nombre.text = clase.nombre
        holder.horario.text = clase.horario
        holder.limiteAlumnos.text = "Limite de alumnos: ${clase.limiteAlumnos}"
    }

    override fun getItemCount(): Int = clases.size

    class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.profesor_nombre)
        val horario: TextView = view.findViewById(R.id.profesor_horario)
        val limiteAlumnos: TextView = view.findViewById(R.id.profesor_limite_alumnos)
    }
}
