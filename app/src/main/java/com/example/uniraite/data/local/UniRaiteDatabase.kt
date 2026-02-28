package com.example.uniraite.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uniraite.data.local.entities.*

@Database(
    entities = [
        Usuario::class,
        Vehiculo::class,
        Viaje::class,
        Reserva::class,
        Calificacion::class,
        Reporte::class
    ],
    version = 1,
    exportSchema = false
)
abstract class UniRaiteDatabase : RoomDatabase() {

    abstract fun uniRaiteDao(): UniRaiteDao

    companion object {
        @Volatile
        private var INSTANCE: UniRaiteDatabase? = null

        fun getDatabase(context: Context): UniRaiteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UniRaiteDatabase::class.java,
                    "uniraite_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}