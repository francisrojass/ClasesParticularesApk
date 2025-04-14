package com.example.clasesparticularesapp.ui.student

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivityStudent : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var nombreEdit: EditText
    private lateinit var apellidosEdit: EditText
    private lateinit var asignaturasView: TextView

    private val asignaturasDisponibles = listOf("Matemáticas", "Física", "Inglés", "Química", "Historia")
    private val seleccionadas = mutableListOf<String>()
    private val seleccionadasBool = BooleanArray(asignaturasDisponibles.size)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_student)

        nombreEdit = findViewById(R.id.edit_nombre)
        apellidosEdit = findViewById(R.id.edit_apellidos)
        asignaturasView = findViewById(R.id.edit_asignaturas)

        val btnGuardar = findViewById<Button>(R.id.btn_guardar)
        val btnCancelar = findViewById<Button>(R.id.btn_cancelar)

        // Abrir el diálogo al pulsar el campo
        asignaturasView.setOnClickListener {
            mostrarDialogoAsignaturas()
        }

        btnGuardar.setOnClickListener {
            guardarPerfil()
        }

        btnCancelar.setOnClickListener {
            finish()
        }

        cargarPerfil()
    }

    private fun mostrarDialogoAsignaturas() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona asignaturas")
        builder.setMultiChoiceItems(asignaturasDisponibles.toTypedArray(), seleccionadasBool) { _, which, isChecked ->
            seleccionadasBool[which] = isChecked
        }
        builder.setPositiveButton("Aceptar") { _, _ ->
            seleccionadas.clear()
            for (i in seleccionadasBool.indices) {
                if (seleccionadasBool[i]) seleccionadas.add(asignaturasDisponibles[i])
            }
            asignaturasView.text = seleccionadas.joinToString(", ")
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun cargarPerfil() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("estudiantes").document(userId).get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    nombreEdit.setText(doc.getString("nombre") ?: "")
                    apellidosEdit.setText(doc.getString("apellidos") ?: "")
                    val asignaturas = doc.get("asignaturas") as? List<*>
                    if (asignaturas != null) {
                        seleccionadas.clear()
                        seleccionadas.addAll(asignaturas.filterIsInstance<String>())
                        asignaturasView.text = seleccionadas.joinToString(", ")
                        for (i in asignaturasDisponibles.indices) {
                            seleccionadasBool[i] = asignaturasDisponibles[i] in seleccionadas
                        }
                    }
                }
            }
    }

    private fun guardarPerfil() {
        val userId = auth.currentUser?.uid ?: return
        val nombre = nombreEdit.text.toString().trim()
        val apellidos = apellidosEdit.text.toString().trim()

        val perfil = hashMapOf(
            "nombre" to nombre,
            "apellidos" to apellidos,
            "asignaturas" to seleccionadas
        )

        db.collection("estudiantes").document(userId).set(perfil)
            .addOnSuccessListener {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
    }
}
