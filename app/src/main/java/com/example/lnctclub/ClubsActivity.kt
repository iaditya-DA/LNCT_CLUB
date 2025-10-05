package com.example.lnctclub

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lnctclub.R
import com.example.lnctclub.adapters.ClubsAdapter
import com.example.lnctclub.model.Club
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ClubsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var clubsRecyclerView: RecyclerView
    private lateinit var clubsAdapter: ClubsAdapter
    private var clubsListener: ListenerRegistration? = null // Made nullable for safety

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clubs)

        db = FirebaseFirestore.getInstance()

        clubsRecyclerView = findViewById(R.id.allClubsRecyclerView)
        clubsRecyclerView.layoutManager = LinearLayoutManager(this)

        clubsAdapter = ClubsAdapter(mutableListOf())
        clubsRecyclerView.adapter = clubsAdapter

        setupAllClubsListener()
    }

    private fun setupAllClubsListener() {
        clubsListener = db.collection("clubs")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error fetching clubs: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val allClubsList = mutableListOf<Club>()
                    for (document in snapshots) {
                        val club = document.toObject(Club::class.java)
                        allClubsList.add(club)
                    }
                    clubsAdapter.updateList(allClubsList)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        clubsListener?.remove()
    }
}