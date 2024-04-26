package com.cabcta10.weightlossapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cabcta10.weightlossapplication.dao.GeofenceCoordinatesDao
import com.cabcta10.weightlossapplication.dao.SettingsDao
import com.cabcta10.weightlossapplication.dao.StepDao
import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import com.cabcta10.weightlossapplication.entity.Settings
import com.cabcta10.weightlossapplication.entity.StepData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(version = 1, entities = [Settings::class, GeofenceCoordinates::class, StepData::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDAO(): SettingsDao
    abstract fun geofenceCoordinatesDao(): GeofenceCoordinatesDao
    abstract fun stepDao(): StepDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, AppDatabase::class.java, "weight_loss_application"
                )
                    .addCallback(DatabaseCallback(context))
                    .build()
                    .also { Instance = it }
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {

            super.onCreate(db)
            // Insert initial data here
            CoroutineScope(Dispatchers.IO).launch {
                getDatabase(context).geofenceCoordinatesDao().insert(
                    GeofenceCoordinates(
                        1,
                        "55.85933606036693",
                        "-4.240754911032492",
                        "grocery",
                        "ALDI"
                    )
                )
                getDatabase(context).geofenceCoordinatesDao().insert(
                    GeofenceCoordinates(
                        2,
                        "55.85725636774398",
                        "-4.248761626504728",
                        "grocery",
                        "Tesco"
                    )
                )
                getDatabase(context).geofenceCoordinatesDao().insert(
                    GeofenceCoordinates(
                        3,
                        "55.863322916698465",
                        "-4.242119714996397",
                        "fitness",
                        "Strath Sport"
                    )
                )
                getDatabase(context).geofenceCoordinatesDao().insert(
                    GeofenceCoordinates(
                        4,
                        "55.858517014502134",
                        "-4.2484349577196365",
                        "fitness",
                        "Club Gym Wellness"
                    )
                )

            }
        }
    }
}