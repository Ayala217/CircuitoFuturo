package com.sebastian.circuitofuturo

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Ranking : Menu() {


        private lateinit var spinnerCategoria: Spinner
        private lateinit var tablaRanking: LinearLayout
        private lateinit var mDbRef: DatabaseReference
        private lateinit var categorias: Array<String> // Declaración global para categorías

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_ranking)

            val tvMenu: TextView = findViewById(R.id.tvMenu)
            setupMenu(tvMenu)

            // Inicializa Firebase
            mDbRef = FirebaseDatabase.getInstance().getReference("ranking")

            // Vincula vistas del XML
            spinnerCategoria = findViewById(R.id.spinnerCategoria)
            tablaRanking = findViewById(R.id.tablaRanking)

            // Configura el spinner de categorías
            setupSpinner()

            // Escucha cambios en la selección de categoría
            spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val categoriaSeleccionada = categorias[position]
                    cargarRanking(categoriaSeleccionada)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        private fun setupSpinner() {
            // Obtiene las categorías desde los recursos
            categorias = resources.getStringArray(R.array.spinnerCategoria)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategoria.adapter = adapter
        }

        private fun cargarRanking(categoria: String) {
            // Referencia a la categoría seleccionada en la base de datos
            mDbRef.child(categoria).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val participantes = mutableListOf<Participante>()

                    snapshot.children.forEach { data ->
                        val participante = data.getValue(Participante::class.java)
                        if (participante != null) {
                            participantes.add(participante)
                        }
                    }
                    mostrarRanking(participantes)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Ranking, "Error al cargar el ranking: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        private fun mostrarRanking(participantes: List<Participante>) {
            // Limpia la tabla antes de mostrar nuevos datos
            tablaRanking.removeAllViews()

            // Ordena participantes por los puntos en orden descendente
            participantes.sortedByDescending { it.puntos }.forEachIndexed { index, participante ->
                // Infla la fila del ranking
                val fila = layoutInflater.inflate(R.layout.fila_ranking, null)

                val tvPosicion = fila.findViewById<TextView>(R.id.tvPosicion)
                val tvNombre = fila.findViewById<TextView>(R.id.tvNombre)
                val tvPuntos = fila.findViewById<TextView>(R.id.tvPuntos)

                // Asigna los valores
                tvPosicion.text = "${index + 1}º"
                tvNombre.text = participante.nombre
                tvPuntos.text = "${participante.puntos} pts"

                // Agrega la fila a la tabla
                tablaRanking.addView(fila)
            }
        }

        // Clase de datos para representar a un participante
        data class Participante(
            val nombre: String = "",
            val puntos: Int = 0 // Los puntos vienen directamente de la base de datos
        )
    }






