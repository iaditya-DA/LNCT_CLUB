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
    private lateinit var clubsListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clubs)

        db = FirebaseFirestore.getInstance()

        clubsRecyclerView = findViewById(R.id.allClubsRecyclerView)
        clubsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list
        clubsAdapter = ClubsAdapter(mutableListOf())
        clubsRecyclerView.adapter = clubsAdapter

        // Start fetching data from Firestore
        setupAllClubsListener()
    }

    private fun setupAllClubsListener() {
        // Here, we fetch ALL clubs from the 'clubs' collection.
        // We don't use a 'whereEqualTo' filter, so it gets everything.
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
                    // Update the adapter with the new list
                    clubsAdapter.updateList(allClubsList)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        // It's important to remove the listener when the activity is destroyed
        clubsListener.remove()
    }
}