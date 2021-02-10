package com.example.appcent.model

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

import androidx.room.Update
import io.reactivex.Flowable
import io.reactivex.Single


interface GameDao {

    @Insert
    fun create(todoList: Game?): Single<Long?>?

    @Update
    fun update(todoList: Game?): Single<Int?>?

    @Delete
    fun delete(todoList: Game?): Single<Int?>?

    @Query("SELECT * FROM game WHERE is_fav = 1")
    fun getFavGames(userId: Long): Flowable<List<Game?>?>?

}