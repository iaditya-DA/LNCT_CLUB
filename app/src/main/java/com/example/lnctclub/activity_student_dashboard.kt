package com.example.lnctclub

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lnctclub.adapters.ClubsAdapter
import com.example.lnctclub.model.Club
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class StudentDashboardActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var clubsRecyclerView: RecyclerView
    private lateinit var clubsAdapter: ClubsAdapter
    private lateinit var clubsListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        db = FirebaseFirestore.getInstance()

        val clubsBtn = findViewById<LinearLayout>(R.id.clubsBtn)
        val eventsBtn = findViewById<LinearLayout>(R.id.eventsBtn)
        val profileBtn = findViewById<LinearLayout>(R.id.profileBtn)
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val studentNameText = findViewById<TextView>(R.id.studentNameText)

        val studentName = intent.getStringExtra("studentName")
        if (studentName != null) {
            welcomeText.text = "Welcome Back!"
            studentNameText.text = studentName
        }

        clubsBtn.setOnClickListener {
            startActivity(Intent(this, ClubsActivity::class.java))
        }
        eventsBtn.setOnClickListener {
            startActivity(Intent(this, EventsActivity::class.java))
        }
        profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        clubsRecyclerView = findViewById(R.id.clubsRecyclerView)
        clubsRecyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ Initialize adapter once
        clubsAdapter = ClubsAdapter(mutableListOf())
        clubsRecyclerView.adapter = clubsAdapter

        setupRealtimeClubsListener()
    }

    private fun setupRealtimeClubsListener() {
        clubsListener = db.collection("clubs")
            .whereEqualTo("status", "active")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error getting clubs: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val clubsList = mutableListOf<Club>()
                    for (document in snapshots) {
                        val club = document.toObject(Club::class.java)
                        clubsList.add(club)
                    }
                    clubsAdapter.updateList(clubsList) // ✅ Update instead of recreating
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        clubsListener.remove()
    }
}
