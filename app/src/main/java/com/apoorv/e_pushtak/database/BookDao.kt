package com.apoorv.e_pushtak.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks(): List<BookEntity>
    @Query("SELECT * FROM books WHERE book_Id = :bookId")
    fun getBookById(bookId:String): BookEntity
}