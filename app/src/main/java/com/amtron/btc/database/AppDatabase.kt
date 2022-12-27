package com.amtron.btc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amtron.btc.dao.MasterDataDao
import com.amtron.btc.helper.Converters
import com.amtron.btc.model.*

@Database(
    entities = [MasterData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MasterDataDao(): MasterDataDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "btc_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}