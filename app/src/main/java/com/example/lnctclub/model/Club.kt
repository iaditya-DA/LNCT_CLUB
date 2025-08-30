package com.example.lnctclub.model

data class Club(
    val clubName: String = "",
    val membersCount: Int = 0,
    val role: String = "",
    val status: String = ""
)

data class Clubs(
    val id: String,
    val name: String,
    val members: Int,
    val imageUrl: String
)