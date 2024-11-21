package com.sebastian.circuitofuturo

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class marcador : AppCompatActivity() {

    private lateinit var spinnerTorneo: Spinner
    private lateinit var spinnerCategoria: Spinner
    private lateinit var spinnerJugador: Spinner
    private lateinit var etSet1: EditText
    private lateinit var etSet2: EditText
    private lateinit var etSet3: EditText
    private lateinit var etSet11: EditText
    private lateinit var etSet22: EditText
    private lateinit var etSet33: EditText
    private lateinit var btnSubirMarcador: Button
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marcador)

        // Inicializa Firebase
        mDbRef = FirebaseDatabase.getInstance().getReference("torneos")

        // Vincula vistas del XML
        spinnerTorneo = findViewById(R.id.spinnerTorneo)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        spinnerJugador = findViewById(R.id.spinnerJugador)
        etSet1 = findViewById(R.id.etSet1)
        etSet2 = findViewById(R.id.etSet2)
        etSet3 = findViewById(R.id.etSet3)
        etSet11 = findViewById(R.id.etSet11)
        etSet22 = findViewById(R.id.etSet22)
        etSet33 = findViewById(R.id.etSet33)
        btnSubirMarcador = findViewById(R.id.btnSubirMarcador)

        // Configurar spinners
        setupSpinnerTorneo()
        setupSpinnerCategoria()

        // Subir marcador
        btnSubirMarcador.setOnClickListener {
            subirMarcador()
        }
    }

    private fun setupSpinnerTorneo() {
        // Carga torneos desde el archivo strings.xml
        val torneos = resources.getStringArray(R.array.spinnerTorneo)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, torneos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTorneo.adapter = adapter

        // Configurar listener de selección
        spinnerTorneo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Cuando se selecciona un torneo, el siguiente spinner será cargado
                val torneoSeleccionado = torneos[position]
                cargarJugadores(torneoSeleccionado, spinnerCategoria.selectedItem?.toString() ?: "")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSpinnerCategoria() {
        // Carga categorías desde el archivo strings.xml
        val categorias = resources.getStringArray(R.array.spinnerCategoria)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter

        // Configurar listener de selección
        spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Cuando se selecciona una categoría, carga los jugadores
                val categoriaSeleccionada = categorias[position]
                cargarJugadores(spinnerTorneo.selectedItem?.toString() ?: "", categoriaSeleccionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun cargarJugadores(torneo: String, categoria: String) {
        // Carga jugadores desde Firebase con el torneo y categoría seleccionados
        mDbRef.child(torneo).child(categoria).child("participantes").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jugadores = snapshot.children.map { it.child("nombre").value.toString() }
                val adapter = ArrayAdapter(this@marcador, android.R.layout.simple_spinner_item, jugadores)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerJugador.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@marcador, "Error al cargar jugadores: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun subirMarcador() {
        val torneoSeleccionado = spinnerTorneo.selectedItem?.toString()
        val categoriaSeleccionada = spinnerCategoria.selectedItem?.toString()
        val jugadorSeleccionado = spinnerJugador.selectedItem?.toString()
        val set1 = etSet1.text.toString()
        val set2 = etSet2.text.toString()
        val set3 = etSet3.text.toString()
        val set11 = etSet11.text.toString()
        val set22 = etSet22.text.toString()
        val set33 = etSet33.text.toString()

        if (torneoSeleccionado.isNullOrEmpty() || categoriaSeleccionada.isNullOrEmpty() || jugadorSeleccionado.isNullOrEmpty()) {
            Toast.makeText(this, "Por favor, selecciona todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val marcador = mapOf(
            "jugador" to jugadorSeleccionado,
            "set1" to set1,
            "set2" to set2,
            "set3" to set3,
            "set11" to set11,
            "set22" to set22,
            "set33" to set33
        )

        mDbRef.child(torneoSeleccionado).child(categoriaSeleccionada).child("marcadores").push()
            .setValue(marcador)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Marcador registrado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al subir el marcador", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
