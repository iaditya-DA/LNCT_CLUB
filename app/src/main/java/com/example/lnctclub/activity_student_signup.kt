package com.example.lnctclub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.example.lnctclub.model.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentSignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_signup)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // ProgressBar init
        progressBar = findViewById(R.id.progressBar)

        // Finding UI elements by their IDs
        val firstNameEditText = findViewById<TextInputEditText>(R.id.firstNameEditText)
        val lastNameEditText = findViewById<TextInputEditText>(R.id.lastNameEditText)
        val enrollmentEditText = findViewById<TextInputEditText>(R.id.enrollmentEditText)
        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<TextInputEditText>(R.id.confirmPasswordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Setting a click listener for the register button
        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val enrollmentNo = enrollmentEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || enrollmentNo.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show progress bar
            progressBar.visibility = View.VISIBLE

            // Create user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            // Save additional user data to Firestore
                            val student = Student(firstName, lastName, enrollmentNo)
                            db.collection("students").document(user.uid)
                                .set(student)
                                .addOnSuccessListener {
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_LONG).show()
                                    // Navigate to student login after a successful registration
                                    val intent = Intent(this, StudentLoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(this, "Failed to save student data: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
