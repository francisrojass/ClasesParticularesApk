package com.example.clasesparticularesapp.ui.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Profesor

class ProfesorAdapter(private var profesores: List<Profesor>) :
    RecyclerView.Adapter<ProfesorAdapter.ProfesorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profesor, parent, false)
        return ProfesorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfesorViewHolder, position: Int) {
        val profesor = profesores[position]
        holder.bind(profesor)
    }

    override fun getItemCount(): Int = profesores.size

    fun actualizarLista(nuevaLista: List<Profesor>) {
        profesores = nuevaLista
        notifyDataSetChanged()
    }

    class ProfesorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre: TextView = itemView.findViewById(R.id.profesor_nombre)
        private val asignatura: TextView = itemView.findViewById(R.id.profesor_asignatura)
        private val precio: TextView = itemView.findViewById(R.id.profesor_precio)
        private val valoracion: TextView = itemView.findViewById(R.id.profesor_valoracion)
        private val foto: ImageView = itemView.findViewById(R.id.profesor_foto)

        fun bind(profesor: Profesor) {
            nombre.text = profesor.nombre
            asignatura.text = "Asignatura: ${profesor.asignatura}"
            precio.text = "Precio: ${profesor.precio} €/h"
            valoracion.text = "Valoración: ${profesor.valoracion} ★"

            Glide.with(itemView.context)
                .load(profesor.fotoUrl)
                .placeholder(R.drawable.ic_profile)
                .into(foto)
        }
    }
}
