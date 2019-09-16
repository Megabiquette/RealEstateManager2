package com.albanfontaine.realestatemanager2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.albanfontaine.realestatemanager2.models.Media
import com.albanfontaine.realestatemanager2.models.Property

@Database(entities = [Media::class, Property::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase{

    abstract fun propertyDAO(): PropertyDAO
    abstract fun mediaDAO(): MediaDAO

    constructor()

    companion object{
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase?{
            if(INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "myDB").build()
                }
            }
            return INSTANCE
        }
    }

}