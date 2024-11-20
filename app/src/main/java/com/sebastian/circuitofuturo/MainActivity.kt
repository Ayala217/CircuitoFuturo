package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnLogin: Button
    private lateinit var tvAlreadyHaveAccount: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        editEmail = findViewById(R.id.etCorreo)
        editPassword = findViewById(R.id.etPassword)
        btnSignUp = findViewById(R.id.btRegister)
        btnLogin = findViewById(R.id.btLogin)
        tvAlreadyHaveAccount = findViewById(R.id.tvAlreadyHaveAccount)

        btnSignUp.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            signUp(email, password)
        }

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            loginAndCheckVerification(email, password)
        }

        tvAlreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    if (user != null) {
                        user.sendEmailVerification()
                            .addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Verifica tu correo antes de continuar.",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Agregar el usuario a la base de datos con rol "participante"
                                    saveUserToDatabase(user.uid, email, "participante")
                                } else {
                                    // Manejar el error al enviar el correo de verificación
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Error al enviar el correo de verificación: ${emailTask.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                } else {
                    // Manejar el error de creación de usuario
                    Toast.makeText(
                        this@MainActivity,
                        "Error al crear el usuario: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun saveUserToDatabase(userId: String, email: String, role: String) {
        val user = hashMapOf(
            "email" to email,
            "role" to role
        )

        mDbRef.child("users").child(userId).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this@MainActivity, "Usuario guardado como participante", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@MainActivity, "Error al guardar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loginAndCheckVerification(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                        if (reloadTask.isSuccessful && mAuth.currentUser?.isEmailVerified == true) {
                            val intent = Intent(this@MainActivity, Inicio::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Por favor, verifica tu correo antes de iniciar sesión.",
                                Toast.LENGTH_LONG
                            ).show()
                            mAuth.signOut()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error en el inicio de sesión, verifica tus credenciales.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
