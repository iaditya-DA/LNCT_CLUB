package com.example.lnctclub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentLoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPasswordButton = findViewById<Button>(R.id.forgotPasswordButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        loadingProgressBar = findViewById(R.id.progressBar)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loadingProgressBar.visibility = View.VISIBLE // Shows the loading bar

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    loadingProgressBar.visibility = View.GONE // Hides the loading bar on completion
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            db.collection("students").document(user.uid)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val firstName = document.getString("firstName") ?: ""
                                        val lastName = document.getString("lastName") ?: ""
                                        val studentName = "$firstName $lastName"

                                        Toast.makeText(this, "Login Successful! Welcome, $studentName", Toast.LENGTH_LONG).show()

                                        val intent = Intent(this, StudentDashboardActivity::class.java)
                                        intent.putExtra("studentName", studentName)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "User data not found.", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(this, StudentDashboardActivity::class.java))
                                        finish()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to fetch user data: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        forgotPasswordButton.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        signUpButton.setOnClickListener {
            startActivity(Intent(this, StudentSignupActivity::class.java))
        }
    }
}
