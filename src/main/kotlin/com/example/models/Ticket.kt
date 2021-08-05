package com.example.models


data class Ticket(
    val movieName: String,
    val theaterName: String,
    val time: String,
    val screenNumber: Int,
    val price: Int
)
