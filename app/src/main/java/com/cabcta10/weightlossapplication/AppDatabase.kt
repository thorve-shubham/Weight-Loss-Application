package com.cabcta10.weightlossapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cabcta10.weightlossapplication.dao.SettingsDao
import com.cabcta10.weightlossapplication.entity.Settings

@Database(version = 1, entities = [Settings::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDAO(): SettingsDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, AppDatabase::class.java, "weight_loss_application"
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}