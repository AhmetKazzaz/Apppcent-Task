package com.example.appcent.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.appcent.util.DateConverter


@Database(
    entities = [Game::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class
)
abstract class AppDB : RoomDatabase() {
    abstract fun gameDao(): GameDao?

    companion object {
        private const val DATABASE_NAME = "database"
        private var instance: AppDB? = null

        @Synchronized
        fun getInstance(context: Context): AppDB? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java, DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build()
            }
            return instance
        }

        private val callback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }
}