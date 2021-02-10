package com.example.appcent.model

import com.google.gson.annotations.SerializedName

data class Game(
    val id: Int,
    val name: String,
    val released: String,
    val rating: Float,
    val metacritic: Int?,
    val description: String?,
    @SerializedName("background_image")
    val imageUrl: String,
    val platforms: List<Platform>,
    var isFav: Boolean = false
) {

}
