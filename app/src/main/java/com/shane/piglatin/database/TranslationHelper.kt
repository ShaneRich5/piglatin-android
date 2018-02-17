package com.shane.piglatin.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object TranslationContract {
    object TranslationEntry : BaseColumns {
        const val TABLE_NAME = "translations"
        const val COLUMN_TEXT = "text"
        const val COLUMN_TRANSLATION = "translation"
        const val COLUMN_FAVORITEED = "favorited"
    }
}

private const val SQLITE_CREATE_TRANSLATIONS =
        "CREATE TABLE ${TranslationContract.TranslationEntry.TABLE_NAME} (" +
                "${BaseColumns._ID}) INTEGER PRIMARY KEY," +
                "${TranslationContract.TranslationEntry.COLUMN_TEXT} TEXT," +
                "${TranslationContract.TranslationEntry.COLUMN_TRANSLATION} TEXT," +
                "${TranslationContract.TranslationEntry.COLUMN_FAVORITEED} TEXT)"

private const val SQL_DELETE_TRANSLATIONS = "DROP TABLE IF EXISTS ${TranslationContract.TranslationEntry.TABLE_NAME}"

class TranslationDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        val DATABASE_NAME = "piglatin"
        val DATABASE_VERSION = 1
    }
    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(SQLITE_CREATE_TRANSLATIONS)
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        database?.execSQL(SQL_DELETE_TRANSLATIONS)
        onCreate(database)
    }
}

