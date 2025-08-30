package com.example.lnctclub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lnctclub.model.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var fullNameValue: TextView
    private lateinit var enrollmentNoValue: TextView
    private lateinit var emailValue: TextView
    private lateinit var avatarInitials: TextView
    private lateinit var courseNameTextView: TextView
    private lateinit var sectionTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var updateProfileButton: Button
    private lateinit var editCourseIcon: View
    private lateinit var editSectionIcon: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Views
        fullNameValue = findViewById(R.id.fullNameValue)
        enrollmentNoValue = findViewById(R.id.enrollmentNoValue)
        emailValue = findViewById(R.id.emailValue)
        avatarInitials = findViewById(R.id.avatarInitials)
        courseNameTextView = findViewById(R.id.courseNameEditText)
        sectionTextView = findViewById(R.id.sectionEditText)
        logoutButton = findViewById(R.id.logoutButton)
        updateProfileButton = findViewById(R.id.updateProfileButton)
        editCourseIcon = findViewById(R.id.editCourseIcon)
        editSectionIcon = findViewById(R.id.editSectionIcon)

        // By default disable editing
        disableEditing(courseNameTextView)
        disableEditing(sectionTextView)

        // Fetch user data
        fetchUserData()

        // Logout
        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, StudentLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Enable editing when pencil clicked
        editCourseIcon.setOnClickListener {
            enableEditing(courseNameTextView)
        }

        editSectionIcon.setOnClickListener {
            enableEditing(sectionTextView)
        }

        // Update profile button
        updateProfileButton.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun fetchUserData() {
        val user = auth.currentUser
        if (user != null) {
            db.collection("students").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        val enrollmentNo = document.getString("enrollmentNo") ?: ""
                        val email = user.email ?: ""
                        val courseName = document.getString("courseName") ?: ""
                        val section = document.getString("section") ?: ""

                        // Set the values on the TextViews
                        fullNameValue.text = "$firstName $lastName"
                        enrollmentNoValue.text = enrollmentNo
                        emailValue.text = email
                        courseNameTextView.text = courseName
                        sectionTextView.text = section

                        // Avatar initials
                        val initials =
                            "${firstName.firstOrNull() ?: ""}${lastName.firstOrNull() ?: ""}".uppercase()
                        avatarInitials.text = initials
                    } else {
                        Toast.makeText(this, "User data not found.", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to fetch user data: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                }
        }
    }

    private fun updateUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            val updatedCourse = courseNameTextView.text.toString().trim()
            val updatedSection = sectionTextView.text.toString().trim()

            val updates = hashMapOf(
                "courseName" to updatedCourse,
                "section" to updatedSection
            )

            db.collection("students").document(user.uid)
                .update(updates as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()

                    // Disable again after update
                    disableEditing(courseNameTextView)
                    disableEditing(sectionTextView)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    // Helper function to enable editing
    private fun enableEditing(textView: TextView) {
        textView.isFocusableInTouchMode = true
        textView.requestFocus()
        textView.setSelectAllOnFocus(true)
        textView.isCursorVisible = true

        // Show keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT)
    }

    // Helper function to disable editing
    private fun disableEditing(textView: TextView) {
        textView.isFocusable = true
        textView.isCursorVisible = true
    }
}
