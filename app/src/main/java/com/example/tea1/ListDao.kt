package com.example.tea1

import androidx.room.*
import ObjectClasses.ListeToDo

@Dao
interface ListDao {
    @Query("SELECT * FROM lists")
    fun getAll(): List<ListeToDo>

    @Query("SELECT * FROM lists WHERE id IN (:listIds)")
    fun loadAllByIds(listIds: Array<String>): List<ListeToDo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg lists: ListeToDo)

    @Delete
    fun delete(list: ListeToDo)
}