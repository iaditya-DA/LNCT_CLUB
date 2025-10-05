package com.example.lnctclub

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// This Activity handles the Club Admin Panel.
// It sets up click listeners for all the quick action buttons.
class ClubAdminActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_admin) // Setting the layout file 'activity_club_admin'

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Finding UI elements by their IDs
        val postEventBtn = findViewById<LinearLayout>(R.id.postEventBtn)
        val membersBtn = findViewById<LinearLayout>(R.id.membersBtn)
        val clubProfileBtn = findViewById<LinearLayout>(R.id.clubProfileBtn)

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val clubNameText = findViewById<TextView>(R.id.clubNameText)

        // Get the club's name from the intent
        val clubName = intent.getStringExtra("clubName")

        if (clubName != null) {
            welcomeText.text = "Welcome Back!"
            clubNameText.text = clubName
        }

        // Click listener for the 'Post Event' button
        postEventBtn.setOnClickListener {
            // Show a Toast message to confirm the button works
            Toast.makeText(this, "Post Event button clicked", Toast.LENGTH_SHORT).show()
            // Correctly start the PostEventActivity class
            val intent = Intent(this, PostEventActivity::class.java)
            startActivity(intent)
        }

        // Click listener for the 'Members' button
        membersBtn.setOnClickListener {
            Toast.makeText(this, "Members button clicked", Toast.LENGTH_SHORT).show()
            // Correctly start the MembersActivity class
            val intent = Intent(this, MembersActivity::class.java)
            startActivity(intent)
        }

        // Click listener for the 'Club Profile' button
        clubProfileBtn.setOnClickListener {
            Toast.makeText(this, "Club Profile button clicked", Toast.LENGTH_SHORT).show()
            // Correctly start the ClubProfileActivity class
            val intent = Intent(this, ClubProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
