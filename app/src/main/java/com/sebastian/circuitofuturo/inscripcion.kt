package com.sebastian.circuitofuturo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class inscripcion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscripcion)

        val etNombreDeportista = findViewById<EditText>(R.id.etNombreDeportista)
        val etNumeroIdentificacion = findViewById<EditText>(R.id.etNumeroIdentificacion)
        val etEquipoClubEntrenador = findViewById<EditText>(R.id.etEquipoClubEntrenador)
        val etCorreoElectronico = findViewById<EditText>(R.id.etCorreoElectronico)
        val etFechaNacimiento = findViewById<EditText>(R.id.etFechaNacimiento)
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val btnInscribirme = findViewById<Button>(R.id.btnInscribirme)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        btnInscribirme.setOnClickListener {
            // Obtén los valores de los campos
            val nombreDeportista = etNombreDeportista.text.toString()
            val numeroIdentificacion = etNumeroIdentificacion.text.toString()
            val equipoClubEntrenador = etEquipoClubEntrenador.text.toString()
            val correoElectronico = etCorreoElectronico.text.toString()
            val fechaNacimiento = etFechaNacimiento.text.toString()
            val categoriaSeleccionada = spinnerCategoria.selectedItem.toString()
            val telefono = etTelefono.text.toString()

            // Aquí puedes realizar la validación de los campos si es necesario
            if (nombreDeportista.isEmpty() || numeroIdentificacion.isEmpty() ||
                equipoClubEntrenador.isEmpty() || correoElectronico.isEmpty() ||
                fechaNacimiento.isEmpty() || telefono.isEmpty()) {

                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Aquí se enviaría la información a la base de datos
            // Por ejemplo:
            // guardarEnBaseDeDatos(nombreDeportista, numeroIdentificacion, equipoClubEntrenador, correoElectronico, fechaNacimiento, categoriaSeleccionada, telefono)

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

    // Método ejemplo para guardar datos en la base de datos (implementa según tu lógica)
    private fun guardarEnBaseDeDatos(
        nombre: String,
        identificacion: String,
        equipo: String,
        correo: String,
        fechaNacimiento: String,
        categoria: String,
        telefono: String
    ) {
        // Implementa la lógica para guardar en la base de datos
        // Esta función es un ejemplo y deberás implementarla según tu estructura de base de datos
    }
}
