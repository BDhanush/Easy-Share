package com.example.easyshare.db
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.easyshare.model.Link

@Dao
interface LinkDao {
    @Query("SELECT * FROM link")
    fun getAll(): LiveData<List<Link>>

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(streak: Link):Long

    @Delete
    fun delete(streak: Link)
}