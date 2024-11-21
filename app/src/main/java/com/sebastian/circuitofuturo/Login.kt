package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editLogIn: Button
    private lateinit var editSignUp: TextView
    private lateinit var userTypeGroup: RadioGroup
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnForgot: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        editEmail = findViewById(R.id.etEmail)
        editPassword = findViewById(R.id.etPassword)
        editLogIn = findViewById(R.id.btnLogin)
        editSignUp = findViewById(R.id.tvRegister)
        userTypeGroup = findViewById(R.id.rgUserType)
        btnForgot= findViewById(R.id.tvForgot)

        // Establecer el listener del RadioGroup para manejar el cambio de los RadioButton
        userTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbAdmin) {
                // Si seleccionas Admin:
                findViewById<TextView>(R.id.tvEmailOrUser).text = "Correo"
                editEmail.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                editEmail.hint = "Correo electrónico" // Campo para el correo
                findViewById<TextView>(R.id.tvPassword).text = "Usuario" // Cambia de "Contraseña" a "Usuario"
                editPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT
                editPassword.hint = "Usuario" // Campo para el usuario
            } else {
                // Si seleccionas Participante:
                findViewById<TextView>(R.id.tvEmailOrUser).text = "Correo"
                editEmail.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                editEmail.hint = "Correo electrónico" // Campo para el correo
                findViewById<TextView>(R.id.tvPassword).text = "Contraseña" // Vuelve a "Contraseña"
                editPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                editPassword.hint = "Contraseña" // Campo para la contraseña
            }
        }



        // Lógica para abrir la pantalla de registro
        editSignUp.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Lógica para abrir la pantalla de registro
        btnForgot.setOnClickListener {
            val intent = Intent(this, RecoverPass::class.java)
            startActivity(intent)
        }

        // Lógica para el login
        editLogIn.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val userId = user?.uid ?: return@addOnCompleteListener
                    val db = FirebaseDatabase.getInstance().getReference("users").child(userId)

                    db.get().addOnSuccessListener { snapshot ->
                        val role = snapshot.child("role").value.toString()
                        if (role == "admin") {
                            val adminIntent = Intent(this@Login, AdminActivity::class.java)
                            finish()
                            startActivity(adminIntent)
                        } else {
                            val userIntent = Intent(this@Login, Inicio::class.java)
                            finish()
                            startActivity(userIntent)
                        }
                    }
                } else {
                    Toast.makeText(this, "Error: el usuario no existe", Toast.LENGTH_SHORT).show()
                }
            }
    }
}



