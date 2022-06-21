package com.example.notesapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query

@Dao
interface NoteDao {
    @Insert(onConflict = IGNORE)
   suspend fun insert(note: Note)
    @Delete
   suspend fun delete(note: Note)
    @Query("Select*from notes_table order by id Asc")
    fun getAllNotes():LiveData<List<Note>>
}