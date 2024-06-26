package com.example.easyshare.db
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.easyshare.model.Link

@Database(entities = [Link::class], version = 1)
abstract class LinkDatabase : RoomDatabase() {
    companion object{
        val NAME = "LinkDatabase"
    }
    abstract fun linkDao(): LinkDao

}