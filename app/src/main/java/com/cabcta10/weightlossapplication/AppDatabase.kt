package com.cabcta10.weightlossapplication

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cabcta10.weightlossapplication.dao.SettingsDao

abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDAO(): SettingsDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, AppDatabase::class.java, "meal_database"
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}