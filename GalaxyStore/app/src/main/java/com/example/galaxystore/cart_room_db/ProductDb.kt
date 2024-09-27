package com.example.galaxystore.cart_room_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 2)
abstract class ProductDb : RoomDatabase() {

    abstract fun productDao():ProductDao

    companion object{

       @Volatile
        private var database:ProductDb? = null

        fun getDatabase(context: Context):ProductDb
        {
            if (database==null)
            {
                synchronized(this) {
                    database = Room.databaseBuilder(context.applicationContext,ProductDb::class.java,"galaxyDB").allowMainThreadQueries().build()
                }
            }
            return database!!
        }
    }

}