package ru.point.cars.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.point.cars.model.SearchHistory

const val DATABASE_NAME = "drive_sell.db"

@Database(entities = [SearchHistory::class], version = 1)
abstract class DataBase : RoomDatabase() {

    abstract fun getSearchHistoryDao(): SearchHistoryDao

    companion object {
        fun getDataBase(context: Context) =
            Room.databaseBuilder(context, DataBase::class.java, DATABASE_NAME).build()
    }
}