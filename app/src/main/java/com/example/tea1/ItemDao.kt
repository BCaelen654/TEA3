package com.example.tea1

import androidx.room.*
import ObjectClasses.ItemToDo

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getAll(): List<ItemToDo>

    @Query("SELECT * FROM items WHERE listId = :listId")
    fun findByList(listId: String): List<ItemToDo>

    @Query("SELECT * FROM items WHERE toSync = 1")
    fun getItemsToSync(): List<ItemToDo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: ItemToDo)

    @Update
    fun update(item: ItemToDo)

    @Delete
    fun delete(item: ItemToDo)
}