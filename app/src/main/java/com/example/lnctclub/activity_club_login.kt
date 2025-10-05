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

class ClubLoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Correctly find the email and password fields from the layout
        val emailEditText = findViewById<TextInputEditText>(R.id.clubIdEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPasswordButton = findViewById<Button>(R.id.forgotPasswordButton)

        loadingProgressBar = findViewById(R.id.progressBar)


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loadingProgressBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    loadingProgressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            db.collection("clubs").document(user.uid)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val clubName = document.getString("clubName") ?: "Club Admin"
                                        Toast.makeText(this, "Welcome, $clubName", Toast.LENGTH_LONG).show()

                                        val intent = Intent(this, ClubAdminActivity::class.java)
                                        intent.putExtra("clubName", clubName)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Club data not found.", Toast.LENGTH_LONG).show()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to fetch club data: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Correctly handle clicks for Forgot Password and Sign Up
        forgotPasswordButton.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }


        }
    }

