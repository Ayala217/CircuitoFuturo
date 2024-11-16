package com.sebastian.circuitofuturo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.regex.Pattern

class inscripcion : Menu() {

    private lateinit var mDbRef: DatabaseReference
    private lateinit var editName: EditText
    private lateinit var editIdentificacion: EditText
    private lateinit var editEquipo: EditText
    private lateinit var spinnerYear: Spinner
    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerDay: Spinner
    private lateinit var editCategoria: Spinner
    private lateinit var editTelefono: EditText
    private lateinit var btnInscribirme: Button
    private lateinit var editTorneo: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscripcion)

        supportActionBar?.hide()

        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu)

        mDbRef = FirebaseDatabase.getInstance().getReference("torneos")

        editName = findViewById(R.id.etNombreDeportista)
        editIdentificacion = findViewById(R.id.etNumeroIdentificacion)
        editEquipo = findViewById(R.id.etEquipoClubEntrenador)
        spinnerYear = findViewById(R.id.spinnerYear)
        spinnerMonth = findViewById(R.id.spinnerMonth)
        spinnerDay = findViewById(R.id.spinnerDay)
        editCategoria = findViewById(R.id.spinnerCategoria)
        editTelefono = findViewById(R.id.etTelefono)
        btnInscribirme = findViewById(R.id.btnInscribirme)
        editTorneo = findViewById(R.id.spinnerTorneo)

        setupDateSpinners()
        setupCategoriaSpinner()
        setupTorneoSpinner() // Configura el spinner para los torneos

        btnInscribirme.setOnClickListener {
            val name = editName.text.toString()
            val identificacion = editIdentificacion.text.toString()
            val equipo = editEquipo.text.toString()
            val year = spinnerYear.selectedItem.toString()
            val month = spinnerMonth.selectedItem.toString()
            val day = spinnerDay.selectedItem.toString()
            val fechaNacimiento = "$day/$month/$year"
            val categoria = editCategoria.selectedItem?.toString() ?: "Sin categoría"
            val telefono = editTelefono.text.toString()
            val torneo = editTorneo.selectedItem?.toString() ?: "Torneo desconocido"

            if (validateFields(name, identificacion, equipo, fechaNacimiento, telefono)) {
                saveUserData(name, identificacion, equipo, fechaNacimiento, categoria, telefono, torneo)
            }
        }
    }

    private fun setupDateSpinners() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (1950..currentYear).toList()
        val months = (1..12).toList()
        val days = (1..30).toList()

        spinnerYear.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        spinnerMonth.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        spinnerDay.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
    }

    private fun setupCategoriaSpinner() {
        val categorias = resources.getStringArray(R.array.spinnerCategoria)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editCategoria.adapter = adapter
    }

    private fun setupTorneoSpinner() {
        val torneos = resources.getStringArray(R.array.spinnerTorneo)// Lista de torneos disponibles
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, torneos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editTorneo.adapter = adapter
    }

    private fun validateFields(name: String, identificacion: String, equipo: String, fechaNacimiento: String, telefono: String): Boolean {
        val namePattern = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"
        val idPattern = "^\\d{10}$"
        val phonePattern = "^\\d{10}$"

        if (!Pattern.matches(namePattern, name)) {
            Toast.makeText(this, "El nombre solo debe contener letras y no puede estar vacío.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Pattern.matches(idPattern, identificacion)) {
            Toast.makeText(this, "La identificación debe tener 10 dígitos numéricos.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (equipo.isBlank()) {
            Toast.makeText(this, "El campo de equipo no puede estar vacío.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (fechaNacimiento.isBlank()) {
            Toast.makeText(this, "Por favor, seleccione una fecha válida.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Pattern.matches(phonePattern, telefono)) {
            Toast.makeText(this, "El teléfono debe tener 10 dígitos numéricos.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveUserData(
        name: String,
        identificacion: String,
        equipo: String,
        fechaNacimiento: String,
        categoria: String,
        telefono: String,
        torneo: String
    ) {
        val user = User(name, identificacion, equipo, fechaNacimiento, categoria, telefono)

        // Guardar bajo el nodo del torneo seleccionado
        mDbRef.child(torneo).child(identificacion).setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Inscripción exitosa en $torneo", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al inscribir en $torneo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    data class User(
        val name: String,
        val identificacion: String,
        val equipo: String,
        val fechaNacimiento: String,
        val categoria: String,
        val telefono: String
    )
}
