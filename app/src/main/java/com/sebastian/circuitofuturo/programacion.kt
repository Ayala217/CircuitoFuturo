package com.sebastian.circuitofuturo

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class programacion : Menu() {

    private lateinit var database: DatabaseReference
    private lateinit var pdfWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programacion) // Establece solo el layout necesario

        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        pdfWebView = findViewById(R.id.pdfWebView)

        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu)

        // Inicializa Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Configura el ArrayAdapter para el Spinner de categorías
        ArrayAdapter.createFromResource(
            this,
            R.array.spinnerCategoria,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategoria.adapter = adapter
        }

        // Configura el listener para el Spinner
        spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val categoriaSeleccionada = parent.getItemAtPosition(position).toString()

                // Llama a Firebase para obtener la URL del PDF de la categoría seleccionada
                obtenerPdfUrl(categoriaSeleccionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun obtenerPdfUrl(categoria: String) {
        // Consulta a Firebase para obtener la URL del PDF para la categoría seleccionada
        database.child("programacion").child(categoria).get().addOnSuccessListener { snapshot ->
            val pdfUrl = snapshot.value as? String
            if (pdfUrl != null) {
                // Cargar el PDF en el WebView
                mostrarPdf(pdfUrl)
            } else {
                Toast.makeText(this, "PDF no disponible para esta categoría", Toast.LENGTH_SHORT).show()
                pdfWebView.visibility = View.GONE
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al obtener la URL del PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarPdf(pdfUrl: String) {
        // Configura el WebView para cargar el PDF desde Google Docs
        pdfWebView.settings.javaScriptEnabled = true
        pdfWebView.webViewClient = WebViewClient()

        // Usamos Google Docs para visualizar el PDF en el WebView
        val googleDocsUrl = "https://docs.google.com/viewer?url=$pdfUrl"
        pdfWebView.loadUrl(googleDocsUrl)
        pdfWebView.visibility = View.VISIBLE
    }
}
