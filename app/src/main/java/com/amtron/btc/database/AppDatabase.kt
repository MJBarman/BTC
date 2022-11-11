package com.amtron.btc.database

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amtron.btc.dao.MasterDataDao
import com.amtron.btc.model.MasterData

@Database(entities = [MasterData::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
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