package com.sebastian.circuitofuturo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NuevosAdminActivity : Menu() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevos_admin)

        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val etUserEmail: EditText = findViewById(R.id.etEmail)
        val etUserPassword: EditText = findViewById(R.id.etUser)
        val btnSaveAdmin: Button = findViewById(R.id.btnLogin)

        btnSaveAdmin.setOnClickListener {
            val email = etUserEmail.text.toString().trim()
            val password = etUserPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Primero, crear el usuario con los datos ingresados
                registerAdmin(email, password)
            }
        }
    }

    private fun registerAdmin(email: String, password: String) {
        // Crear el usuario con email y password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Registro exitoso. Verifica tu correo para completar el proceso.",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Solo guardamos los datos si el registro es exitoso
                            user?.let {
                                saveUserEmailAndUsername(it.uid, it.email ?: "Correo desconocido")
                            }

                        } else {
                            Toast.makeText(
                                this,
                                "Error al enviar el correo de verificación.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Error al registrar usuario: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun saveUserEmailAndUsername(
        username: String,
        email: String
    ) {
        val userData = mapOf(
            "username" to username,
            "email" to email,
            "role" to "admin"
        )

        val usersRef = FirebaseDatabase.getInstance().getReference("users")

        // Guardar en el nodo 'users'
        usersRef.child(username).setValue(userData).addOnCompleteListener { userTask ->
            if (userTask.isSuccessful) {
                Toast.makeText(
                    this,
                    "Datos guardados correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Error al guardar los datos: ${userTask.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

