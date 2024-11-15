package com.sebastian.circuitofuturo

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.regex.Pattern

class inscripcion : Menu() {

    private lateinit var mDbRef: DatabaseReference
    private lateinit var editName: EditText
    private lateinit var editIdentificacion: EditText
    private lateinit var editEquipo: EditText
    private lateinit var editFechaNacimiento: EditText
    private lateinit var editCategoria: Spinner
    private lateinit var editTelefono: EditText
    private lateinit var btnInscribirme: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscripcion)

        supportActionBar?.hide()

        setContentView(R.layout.activity_inicio)

        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu)


        mDbRef = FirebaseDatabase.getInstance().getReference("users")


        editName = findViewById(R.id.etNombreDeportista)
        editIdentificacion = findViewById(R.id.etNumeroIdentificacion)
        editEquipo = findViewById(R.id.etEquipoClubEntrenador)
        editFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        editCategoria = findViewById(R.id.spinnerCategoria)
        editTelefono = findViewById(R.id.etTelefono)
        btnInscribirme = findViewById(R.id.btnInscribirme)


        val categorias = arrayOf("Categoria 1", "Categoria 2", "Categoria 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editCategoria.adapter = adapter


        editFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }

        btnInscribirme.setOnClickListener {
            val name = editName.text.toString()
            val identificacion = editIdentificacion.text.toString()
            val equipo = editEquipo.text.toString()
            val fechaNacimiento = editFechaNacimiento.text.toString()
            val categoria = editCategoria.selectedItem?.toString() ?: "Sin categoría"
            val telefono = editTelefono.text.toString()

            if (validateFields(name, identificacion, equipo, fechaNacimiento, telefono)) {
                saveUserData(name, identificacion, equipo, fechaNacimiento, categoria, telefono)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val age = year - selectedYear
            if (age in 10..100) {
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editFechaNacimiento.setText(formattedDate)
            } else {
                Toast.makeText(this, "La edad debe estar entre 10 y 100 años.", Toast.LENGTH_SHORT).show()
            }
        }, year, month, day)


        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
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
            Toast.makeText(this, "Por favor, seleccione una fecha de nacimiento válida.", Toast.LENGTH_SHORT).show()
            return false
        }


        if (!Pattern.matches(phonePattern, telefono)) {
            Toast.makeText(this, "El teléfono debe tener 10 dígitos numéricos.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveUserData(name: String, identificacion: String, equipo: String, fechaNacimiento: String, categoria: String, telefono: String) {
        val userId = mDbRef.push().key ?: return
        val user = User(name, identificacion, equipo, fechaNacimiento, categoria, telefono)
        mDbRef.child(userId).setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Inscripción exitosa", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error en la inscripción", Toast.LENGTH_SHORT).show()
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
