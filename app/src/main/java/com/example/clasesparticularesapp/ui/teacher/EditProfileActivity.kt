package com.example.clasesparticularesapp.ui.teacher

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Profesor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellidos: EditText
    private lateinit var tvAsignaturas: TextView

    private val asignaturasDisponibles = arrayOf("Matemáticas", "Física", "Inglés", "Química", "Historia")
    private val asignaturasSeleccionadas = mutableListOf<String>()
    private val seleccionEstado = BooleanArray(asignaturasDisponibles.size)

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_teacher)

        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        tvAsignaturas = findViewById(R.id.tvAsignaturas)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        firestore = FirebaseFirestore.getInstance()
        if (userId != null) {
            firestore.collection("profesores").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nombre = document.getString("nombre") ?: ""
                        val apellidos = document.getString("apellidos") ?: ""
                        val asignaturasGuardadas = document.get("asignaturas") as? List<*> ?: emptyList<Any>()

                        etNombre.setText(nombre)
                        etApellidos.setText(apellidos)

                        // ✳️ Aquí actualizamos las asignaturas seleccionadas
                        asignaturasSeleccionadas.clear()
                        asignaturasGuardadas.forEach { asig ->
                            if (asig is String) {
                                asignaturasSeleccionadas.add(asig)
                            }
                        }

                        // ✳️ Actualizamos el estado del diálogo
                        for (i in asignaturasDisponibles.indices) {
                            seleccionEstado[i] = asignaturasSeleccionadas.contains(asignaturasDisponibles[i])
                        }

                        // ✳️ Actualizamos el texto del TextView para que muestre las asignaturas seleccionadas
                        if (asignaturasSeleccionadas.isNotEmpty()) {
                            tvAsignaturas.text = asignaturasSeleccionadas.joinToString(", ")
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
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
            guardarCambios()
        }

        tvAsignaturas.setOnClickListener {
            mostrarDialogoSeleccion()
        }
    }

    private fun guardarCambios() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val nombre = etNombre.text.toString()
        val apellidos = etApellidos.text.toString()

        val profesorActualizado = Profesor(
            id = userId,
            nombre = nombre,
            apellidos = apellidos,
            asignaturas = asignaturasSeleccionadas.toList(),
            valoracion = 0f,
            fotoUrl = "" // Por ahora no gestionas imágenes
        )

        db.collection("profesores").document(userId)
            .set(profesorActualizado)
            .addOnSuccessListener {
                Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
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
