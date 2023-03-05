package com.amk.market.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amk.market.model.data.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}