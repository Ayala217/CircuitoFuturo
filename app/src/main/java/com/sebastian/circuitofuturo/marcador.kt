
package com.sebastian.circuitofuturo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class marcador : AppCompatActivity() {

    private lateinit var spinnerJugador: Spinner
    private lateinit var btnSubirMarcador: Button
    private lateinit var mDbRef: DatabaseReference

    // EditTexts for sets
    private lateinit var editSet1: EditText
    private lateinit var editSet2: EditText
    private lateinit var editSet3: EditText
    private lateinit var editSet4: EditText
    private lateinit var editSet5: EditText
    private lateinit var editSet6: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marcador)

        // Initialize Firebase reference
        mDbRef = FirebaseDatabase.getInstance().getReference("torneos")

        // Bind views

        btnSubirMarcador = findViewById(R.id.btnSubirMarcador)
        editSet1 = findViewById(R.id.editSet1)
        editSet2 = findViewById(R.id.editSet2)
        editSet3 = findViewById(R.id.editSet3)
        editSet4 = findViewById(R.id.editSet4)
        editSet5 = findViewById(R.id.editSet5)
        editSet6 = findViewById(R.id.editSet6)

        // Load players into the spinner
        loadPlayers()

        // Handle button click
        btnSubirMarcador.setOnClickListener {
            val jugadorSeleccionado = spinnerJugador.selectedItem?.toString()
            val sets = listOf(
                editSet1.text.toString(),
                editSet2.text.toString(),
                editSet3.text.toString(),
                editSet4.text.toString(),
                editSet5.text.toString(),
                editSet6.text.toString()
            )

            // Validate inputs
            if (jugadorSeleccionado.isNullOrEmpty() || sets.count { it.isNotEmpty() } < 4) {
                Toast.makeText(this, "Completa al menos 4 sets y selecciona un jugador", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save data to Firebase
            saveMatchData(jugadorSeleccionado, sets)
        }
    }

    private fun loadPlayers() {
        val jugadores = listOf("Jugador 1", "Jugador 2", "Jugador 3") // Replace with actual data
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jugadores)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJugador.adapter = adapter
    }

    private fun saveMatchData(jugador: String, sets: List<String>) {
        val matchData = mapOf(
            "jugador" to jugador,
            "sets" to sets.filter { it.isNotEmpty() }
        )

        val torneoId = "torneo_2024" // Replace with dynamic torneo ID
        mDbRef.child(torneoId).child("partidos").push().setValue(matchData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Marcador subido con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al subir el marcador", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
