package com.example.lnctclub

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.lnctclub.model.Student

class SplashScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen_loading)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatus()
        }, 1000)
    }

    private fun checkUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in, now determine their role by checking the "students" collection first
            db.collection("students").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val role = document.getString("role")
                        if (role == "student") {
                            val firstName = document.getString("firstName") ?: ""
                            val lastName = document.getString("lastName") ?: ""
                            val studentName = "$firstName $lastName"

                            val intent = Intent(this, StudentDashboardActivity::class.java)
                            intent.putExtra("studentName", studentName)
                            startActivity(intent)
                        } else {
                            // If the document exists but the role is not student, check the clubs
                            checkClubAdminRole(currentUser.uid)
                        }
                    } else {
                        // Document doesn't exist in "students" collection, so check the "clubs" collection
                        checkClubAdminRole(currentUser.uid)
                    }
                    finish()
                }
                .addOnFailureListener {
                    // Handle error, send to login
                    val intent = Intent(this, StudentLoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            // No user is logged in, go to the StartActivity
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkClubAdminRole(userId: String) {
        db.collection("clubs").document(userId)
            .get()
            .addOnSuccessListener { clubDocument ->
                if (clubDocument.exists()) {
                    val role = clubDocument.getString("role")
                    if (role == "club_admin") {
                        val clubName = clubDocument.getString("clubName") ?: "Club Admin"
                        Toast.makeText(this, "Welcome, $clubName", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, ClubAdminActivity::class.java)
                        intent.putExtra("clubName", clubName)
                        startActivity(intent)
                    } else {
                        // User is logged in but has no valid role, send to login
                        val intent = Intent(this, StudentLoginActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    // User data not found in either collection, send to login
                    val intent = Intent(this, StudentLoginActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
            .addOnFailureListener {
                // Handle error, send to login
                val intent = Intent(this, StudentLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
    }
}
