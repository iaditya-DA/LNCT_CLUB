package com.example.lnctclub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lnctclub.R
import com.example.lnctclub.model.Club

class ClubsAdapter(private var clubsList: MutableList<Club>) :
    RecyclerView.Adapter<ClubsAdapter.ClubViewHolder>() {

    class ClubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clubIcon: ImageView = itemView.findViewById(R.id.clubIconImageView)
        val clubName: TextView = itemView.findViewById(R.id.clubNameTextView)
        val membersCount: TextView = itemView.findViewById(R.id.membersCountTextView)
        val viewButton: Button = itemView.findViewById(R.id.viewButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_club, parent, false) // ✅ Correct layout
        return ClubViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        val currentClub = clubsList[position]
        holder.clubName.text = currentClub.clubName
        holder.membersCount.text = "${currentClub.membersCount} Members"

        holder.viewButton.setOnClickListener {
            // TODO: Navigate to detailed view of this club
        }
    }

    override fun getItemCount() = clubsList.size

    // ✅ Helper function to update list
    fun updateList(newList: MutableList<Club>) {
        clubsList.clear()
        clubsList.addAll(newList)
        notifyDataSetChanged()
    }


}
