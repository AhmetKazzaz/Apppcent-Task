package com.example.appcent.model.response

import com.example.appcent.model.Game

data class GamesResponse(
    val count: Int,
    val next: String?,
    val description: String,
    val results: List<Game>
) {
}