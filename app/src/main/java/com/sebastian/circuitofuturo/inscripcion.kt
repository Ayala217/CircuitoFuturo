package com.sebastian.circuitofuturo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class inscripcion : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscripcion)

        // Inicializa Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Obtén las referencias de los elementos de la interfaz
        val etNombreDeportista = findViewById<EditText>(R.id.etNombreDeportista)
        val etNumeroIdentificacion = findViewById<EditText>(R.id.etNumeroIdentificacion)
        val etEquipoClubEntrenador = findViewById<EditText>(R.id.etEquipoClubEntrenador)
        val etCorreoElectronico = findViewById<EditText>(R.id.etCorreoElectronico)
        val etFechaNacimiento = findViewById<EditText>(R.id.etFechaNacimiento)
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val btnInscribirme = findViewById<Button>(R.id.btnInscribirme)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Configura el botón "Inscribirme"
        btnInscribirme.setOnClickListener {
            // Obtén los valores de los campos
            val nombreDeportista = etNombreDeportista.text.toString()
            val numeroIdentificacion = etNumeroIdentificacion.text.toString()
            val equipoClubEntrenador = etEquipoClubEntrenador.text.toString()
            val correoElectronico = etCorreoElectronico.text.toString()
            val fechaNacimiento = etFechaNacimiento.text.toString()
            val categoriaSeleccionada = spinnerCategoria.selectedItem.toString()
            val telefono = etTelefono.text.toString()

            // Validación de campos
            if (nombreDeportista.isEmpty() || numeroIdentificacion.isEmpty() ||
                equipoClubEntrenador.isEmpty() || correoElectronico.isEmpty() ||
                fechaNacimiento.isEmpty() || telefono.isEmpty()) {

                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar datos en Firebase
            guardarEnFirebase(
                nombreDeportista, numeroIdentificacion, equipoClubEntrenador,
                correoElectronico, fechaNacimiento, categoriaSeleccionada, telefono
            )

            // Mensaje de confirmación
            Toast.makeText(this, "Inscripción completada", Toast.LENGTH_SHORT).show()
        }

        // Configura el botón "Cancelar"
        btnCancelar.setOnClickListener {
            // Limpia todos los campos
            etNombreDeportista.text.clear()
            etNumeroIdentificacion.text.clear()
            etEquipoClubEntrenador.text.clear()
            etCorreoElectronico.text.clear()
            etFechaNacimiento.text.clear()
            spinnerCategoria.setSelection(0) // Selecciona la primera opción en el Spinner
            etTelefono.text.clear()

            Toast.makeText(this, "Inscripción cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para guardar datos en Firebase Realtime Database
    private fun guardarEnFirebase(
        nombre: String,
        identificacion: String,
        equipo: String,
        correo: String,
        fechaNacimiento: String,
        categoria: String,
        telefono: String
    ) {
        val usuarioId = database.push().key ?: return  // Crea una clave única para cada inscripción
        val datosUsuario = mapOf(
            "nombre" to nombre,
            "identificacion" to identificacion,
            "equipo" to equipo,
            "correo" to correo,
            "fechaNacimiento" to fechaNacimiento,
            "categoria" to categoria,
            "telefono" to telefono
        )

        // Guarda los datos en la referencia de Firebase
        database.child("inscripciones").child(usuarioId).setValue(datosUsuario)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
