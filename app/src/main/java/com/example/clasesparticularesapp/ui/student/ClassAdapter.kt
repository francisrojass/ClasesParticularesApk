package com.example.clasesparticularesapp.ui.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase

class ClassAdapter(private var clases: List<Clase>) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profesor, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val clase = clases[position]
        holder.nombre.text = clase.nombre
        holder.asignatura.text = clase.asignatura
        holder.horario.text = clase.horario
        holder.precio.text = "Precio: ${clase.precioHora} €/h"
        holder.limiteAlumnos.text = "Límite alumnos: ${clase.limiteAlumnos}"
        holder.valoracion.text = "Valoración: pendiente" // Opcional si no se carga aún el profesor
    }

    override fun getItemCount(): Int = clases.size

    fun actualizarLista(nuevaLista: List<Clase>) {
        clases = nuevaLista
        notifyDataSetChanged()
    }

    class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.profesor_nombre)
        val asignatura: TextView = view.findViewById(R.id.profesor_asignatura)
        val horario: TextView = view.findViewById(R.id.profesor_horario)
        val precio: TextView = view.findViewById(R.id.profesor_precio)
        val limiteAlumnos: TextView = view.findViewById(R.id.profesor_limite_alumnos)
        val valoracion: TextView = view.findViewById(R.id.profesor_valoracion)
    }
}
