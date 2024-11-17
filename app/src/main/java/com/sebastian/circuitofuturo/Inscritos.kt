package com.sebastian.circuitofuturo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class Inscritos : AppCompatActivity() {

    private lateinit var spinnerTorneo: Spinner
    private lateinit var etBuscar: EditText
    private lateinit var tablaDatos: LinearLayout
    private lateinit var mDbRef: DatabaseReference
    private var listaParticipantes: MutableList<Participante> = mutableListOf()
    private var torneoSeleccionado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscritos)

        // Inicializa Firebase
        mDbRef = FirebaseDatabase.getInstance().getReference("torneos")

        // Vincula vistas del XML
        spinnerTorneo = findViewById(R.id.spinnerTorneo)
        etBuscar = findViewById(R.id.etBuscar)
        tablaDatos = findViewById(R.id.tablaDatos)

        // Configura el spinner de torneos
        setupSpinner()

        // Escucha cambios en el buscador
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filtrarDatos(s.toString())
            }
        })
    }

    private fun setupSpinner() {
        mDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val torneos = snapshot.children.map { it.key.toString() } // Obtiene los nombres de los torneos
                if (torneos.isEmpty()) {
                    Toast.makeText(this@Inscritos, "No hay torneos disponibles", Toast.LENGTH_SHORT).show()
                    return
                }

                val adapter = ArrayAdapter(this@Inscritos, android.R.layout.simple_spinner_item, torneos)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTorneo.adapter = adapter

                // Escuchar selección de torneos
                spinnerTorneo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        torneoSeleccionado = torneos[position]
                        cargarParticipantes(torneoSeleccionado) // Carga participantes del torneo seleccionado
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Inscritos, "Error al cargar torneos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarParticipantes(torneo: String) {
        // Accedemos al nodo del torneo seleccionado
        mDbRef.child(torneo).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaParticipantes.clear()

                if (!snapshot.exists()) {
                    Toast.makeText(this@Inscritos, "No hay participantes en este torneo", Toast.LENGTH_SHORT).show()
                    mostrarDatos(listaParticipantes)
                    return
                }

                snapshot.children.forEach { data ->
                    val participante = data.getValue(Participante::class.java)
                    if (participante != null) {
                        listaParticipantes.add(participante)
                        println("Cargado: ${participante.name}, ${participante.equipo}, ${participante.categoria}")
                    } else {
                        println("Error al mapear los datos del nodo: ${data.key}")
                    }
                }
                mostrarDatos(listaParticipantes)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Inscritos, "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun mostrarDatos(participantes: List<Participante>) {
        tablaDatos.removeAllViews()

        if (participantes.isEmpty()) {
            val emptyView = TextView(this)
            emptyView.text = "No hay participantes disponibles"
            emptyView.textSize = 16f
            emptyView.setTextColor(getColor(android.R.color.black))
            emptyView.setPadding(16, 16, 16, 16)
            tablaDatos.addView(emptyView)
            return
        }

        participantes.forEach { participante ->
            val fila = layoutInflater.inflate(R.layout.fila_participante, null)

            val tvDeportista = fila.findViewById<TextView>(R.id.tvNombre)
            val tvEquipo = fila.findViewById<TextView>(R.id.tvEquipo)
            val tvCategoria = fila.findViewById<TextView>(R.id.tvCategoria)

            tvDeportista.text = participante.name
            tvEquipo.text = participante.equipo
            tvCategoria.text = participante.categoria

            tablaDatos.addView(fila)
        }
    }

    private fun filtrarDatos(query: String) {
        val listaFiltrada = if (query.isEmpty()) {
            listaParticipantes
        } else {
            listaParticipantes.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.equipo.contains(query, ignoreCase = true) ||
                        it.categoria.contains(query, ignoreCase = true)
            }
        }
        mostrarDatos(listaFiltrada)
    }

    data class Participante(
        val name: String = "", // En lugar de "nombre"
        val equipo: String = "",
        val categoria: String = "",
        val fechaNacimiento: String = "",
        val identificacion: String = "",
        val telefono: String = ""
    )

}
