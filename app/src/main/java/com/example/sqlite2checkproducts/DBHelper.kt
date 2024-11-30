package com.example.sqlite2checkproducts

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "USER_DATABASE"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "user_table"
        val KEY_ID = "id"
        val KEY_NAME = "name"
        val KEY_EMAIL = "email"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val USER_TABLE = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_EMAIL + " TEXT" + ")")
        db.execSQL(USER_TABLE)
    }
    //CREATE TABLE table_name (column1 datatype, column2 datatype, column3 datatype)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(user: User) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, user.userId)
        contentValues.put(KEY_NAME, user.userName)
        contentValues.put(KEY_EMAIL, user.userEmail)
        db.insert(TABLE_NAME, null,contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun readUser(): MutableList<User> {
        val userList: MutableList<User> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return userList
        }
        var userId: Int
        var userName: String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                val user = User(userId = userId, userName = userName, userEmail = userEmail)
                userList.add(user)
            } while (cursor.moveToNext())
        }
        return userList
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    fun updateUser(user: User) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, user.userId)
        contentValues.put(KEY_NAME, user.userName)
        contentValues.put(KEY_EMAIL, user.userEmail)
        db.update(TABLE_NAME, contentValues, "id=" + user.userId, null)
        db.close()
    }

    fun deleteUser(user: User) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, user.userId)

        db.delete(TABLE_NAME, "id=" + user.userId, null)
        db.close()
    }
}