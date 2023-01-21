package com.apoorv.e_pushtak.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [BookEntity::class], version = 1)
abstract class BookDatabase:RoomDatabase() {
    abstract fun bookDao(): BookDao
}


/*
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.media.Rating

class BookDatabase(context: Context):SQLiteOpenHelper(context,"Books",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create Table Books(book_id int primary key,bookName TEXT,bookAuthor TEXT,bookPrice TEXT," +
                "bookRating TEXT,bookDesc TEXT,bookImage char)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("Drop table if exists Books")
    }
    fun insertBook(book_id:Int,bookName:String,bookAuthor:String,bookPrice:String,bookRating: String,bookDesc:String,
    bookImage:String){
        val db:SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("book_id",book_id)
        contentValues.put("bookName",bookName)
        contentValues.put("bookAuthor",bookAuthor)
        contentValues.put("bookPrice",bookPrice)
        contentValues.put("bookRating",bookRating)
        contentValues.put("bookDesc",bookDesc)
        contentValues.put("bookImage",bookImage)
    }
}*/
