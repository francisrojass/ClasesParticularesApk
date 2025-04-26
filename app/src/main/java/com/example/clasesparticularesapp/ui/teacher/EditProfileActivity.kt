package com.example.clasesparticularesapp.ui.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Profesor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellidos: EditText
    private lateinit var tvAsignaturas: TextView
    private lateinit var imageViewFotoPerfil: ImageView

    private val asignaturasDisponibles = arrayOf("Matemáticas", "Física", "Inglés", "Química", "Historia")
    private val asignaturasSeleccionadas = mutableListOf<String>()
    private val seleccionEstado = BooleanArray(asignaturasDisponibles.size)

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var fotoUrl: String = "" // Para guardar la URL de la foto

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_teacher)

        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        tvAsignaturas = findViewById(R.id.tvAsignaturas)
        imageViewFotoPerfil = findViewById(R.id.imgPerfil)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            cargarDatosPerfil()
        }

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val discardButton: Button = findViewById(R.id.btnDescartarCambiosPerfil)
        discardButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val saveButton: Button = findViewById(R.id.btnGuardarCambiosPerfil)
        saveButton.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        tvAsignaturas.setOnClickListener {
            mostrarDialogoSeleccion()
        }

        // Si quieres permitir cambiar la foto, puedes poner un listener aquí
        // imageViewFotoPerfil.setOnClickListener { ... }
    }

    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar cambios")
            .setMessage("¿Estás seguro de que deseas guardar los cambios?")
            .setPositiveButton("Sí") { _, _ ->
                guardarCambios()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun guardarCambios() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val nombre = etNombre.text.toString()
        val apellidos = etApellidos.text.toString()
        val asignaturas = asignaturasSeleccionadas.toList()

        val profesorActualizado = Profesor(
            id = userId,
            nombre = nombre,
            apellidos = apellidos,
            asignaturas = asignaturas,
            valoracion = 0f,
            fotoUrl = fotoUrl // Guardamos la URL de la foto
        )

        db.collection("profesores").document(userId)
            .set(profesorActualizado)
            .addOnSuccessListener {
                Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TeacherActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarDatosPerfil() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("profesores").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nombre = document.getString("nombre") ?: ""
                    val apellidos = document.getString("apellidos") ?: ""
                    val asignaturasGuardadas = document.get("asignaturas") as? List<*> ?: emptyList<Any>()
                    fotoUrl = document.getString("fotoUrl") ?: "" // Obtenemos la URL de la foto

                    etNombre.setText(nombre)
                    etApellidos.setText(apellidos)

                    asignaturasSeleccionadas.clear()
                    asignaturasGuardadas.forEach { asig ->
                        if (asig is String) {
                            asignaturasSeleccionadas.add(asig)
                        }
                    }

                    for (i in asignaturasDisponibles.indices) {
                        seleccionEstado[i] = asignaturasSeleccionadas.contains(asignaturasDisponibles[i])
                    }

                    if (asignaturasSeleccionadas.isNotEmpty()) {
                        tvAsignaturas.text = asignaturasSeleccionadas.joinToString(", ")
                    }

                    // Cargar la imagen con Glide
                    if (fotoUrl.isNotEmpty()) {
                        Glide.with(this)
                            .load(fotoUrl)
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .error(R.drawable.ic_profile_placeholder)
                            .into(imageViewFotoPerfil)
                    } else {
                        imageViewFotoPerfil.setImageResource(R.drawable.ic_profile_placeholder)
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarDialogoSeleccion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona tus asignaturas")
        builder.setMultiChoiceItems(asignaturasDisponibles, seleccionEstado) { _, index, isChecked ->
            if (isChecked) {
                if (!asignaturasSeleccionadas.contains(asignaturasDisponibles[index])) {
                    asignaturasSeleccionadas.add(asignaturasDisponibles[index])
                }
            } else {
                asignaturasSeleccionadas.remove(asignaturasDisponibles[index])
            }
        }

        builder.setPositiveButton("Aceptar") { _, _ ->
            tvAsignaturas.text = asignaturasSeleccionadas.joinToString(", ")
        }
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }
}
