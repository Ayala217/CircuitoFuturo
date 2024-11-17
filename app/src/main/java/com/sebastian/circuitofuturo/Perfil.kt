package com.sebastian.circuitofuturo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

class Perfil : Menu() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var fotoDeportista: ImageView
    private lateinit var ranking: TextView
    private lateinit var nombreTextView: TextView
    private lateinit var identificacionTextView: TextView
    private lateinit var equipoTextView: TextView
    private lateinit var correoTextView: TextView
    private lateinit var fechaNacimientoTextView: TextView
    private lateinit var categoriaTextView: TextView
    private lateinit var telefonoTextView: TextView
    private val PICK_IMAGE_REQUEST = 71  // Código para abrir la galería

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializa Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Referencias a los elementos del XML
        fotoDeportista = findViewById(R.id.foto_deportista)
        ranking = findViewById(R.id.ranking)
        nombreTextView = findViewById(R.id.nombre)
        identificacionTextView = findViewById(R.id.identificacion)
        equipoTextView = findViewById(R.id.equipo)
        correoTextView = findViewById(R.id.correo) // Correo
        fechaNacimientoTextView = findViewById(R.id.fecha_nacimiento)
        categoriaTextView = findViewById(R.id.categoria)
        telefonoTextView = findViewById(R.id.telefono)

        // Cargar datos del usuario y foto
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val identificacionUsuario = "1123432380" // Sustituye por el valor dinámico si es necesario

            val tvMenu: TextView = findViewById(R.id.tvMenu)
            setupMenu(tvMenu)

            // Consulta al nodo users buscando por el subnodo identificacion
            database.child("users").orderByChild("identificacion").equalTo(identificacionUsuario)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                val name = userSnapshot.child("name").getValue(String::class.java) ?: "N/A"
                                val identificacion =
                                    userSnapshot.child("identificacion").getValue(String::class.java)
                                        ?: "N/A"
                                val equipo =
                                    userSnapshot.child("equipo").getValue(String::class.java) ?: "N/A"
                                val fechaNacimiento =
                                    userSnapshot.child("fechaNacimiento").getValue(String::class.java)
                                        ?: "N/A"
                                val categoria =
                                    userSnapshot.child("categoria").getValue(String::class.java) ?: "N/A"
                                val telefono =
                                    userSnapshot.child("telefono").getValue(String::class.java) ?: "N/A"
                                val photoBase64 = userSnapshot.child("photo").getValue(String::class.java)

                                // Muestra los datos
                                nombreTextView.text = "Nombre: $name"
                                identificacionTextView.text = "Identificación: $identificacion"
                                equipoTextView.text = "Equipo: $equipo"
                                fechaNacimientoTextView.text = "Fecha de Nacimiento: $fechaNacimiento"
                                categoriaTextView.text = "Categoría: $categoria"
                                telefonoTextView.text = "Teléfono: $telefono"

                                // Obtener el correo del usuario desde Firebase Authentication
                                val correo = currentUser.email ?: "Correo no disponible"
                                correoTextView.text = "Correo: $correo"

                                // Si hay una foto almacenada, la decodificamos y mostramos
                                if (photoBase64 != null) {
                                    val decodedBytes = Base64.decode(photoBase64, Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(
                                        decodedBytes,
                                        0,
                                        decodedBytes.size
                                    )
                                    fotoDeportista.setImageBitmap(bitmap)
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@Perfil,
                                "No se encontraron datos del usuario.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@Perfil,
                            "Error al cargar los datos: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "No hay usuario autenticado.", Toast.LENGTH_SHORT).show()
        }

        // Abre la galería cuando el ImageView es clickeado
        fotoDeportista.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }
}
