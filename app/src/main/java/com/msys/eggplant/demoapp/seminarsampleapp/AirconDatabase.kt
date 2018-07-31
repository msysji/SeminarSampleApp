package com.msys.eggplant.demoapp.seminarsampleapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DB_NAME = "AirconInfos"
private const val DB_VERSION = 1

data class AirconInfo(val position: Int, val status: Int, val location: String, val mode: String, val temp: Int)

class AirconDatabase(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE AirconInfos (
            _id INTEGER PRIMARY KEY AUTOINCREMENT,
            position INTEGER NOT NULL,
            status INTEGER NOT NULL,
            location TEXT NOT NULL,
            mode TEXT NOT NULL,
            temperature INTEGER NOT NULL);
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun queryAirconInfos(context: Context) : List<AirconInfo>{
        val airconInfos = mutableListOf<AirconInfo>()
        val database = AirconDatabase(context).readableDatabase
        val cursor = database.query(
                "AirconInfos",
                null,
                null,
                null,
                null,
                null,
                "position ASC")
        cursor.use {
            while(cursor.moveToNext()) {
                airconInfos.add(AirconInfo(
                        cursor.getInt(cursor.getColumnIndex("position")),
                        cursor.getInt(cursor.getColumnIndex("status")),
                        cursor.getString(cursor.getColumnIndex("location")),
                        cursor.getString(cursor.getColumnIndex("mode")),
                        cursor.getInt(cursor.getColumnIndex("temperature"))
                ))
            }
        }

        database.close()
        return airconInfos
    }

    fun insertAirconInfos(context: Context, airconInfos: List<AirconInfo>) {
        val database = AirconDatabase(context).writableDatabase

        database.use { db ->
            airconInfos.forEach { airconInfo ->
                val record = ContentValues().apply {
                    put("position", airconInfo.position)
                    put("status", airconInfo.status)
                    put("location", airconInfo.location)
                    put("mode", airconInfo.mode)
                    put("temperature", airconInfo.temp)
                }
                db.insert("AirconInfos", null, record)
            }
        }
    }

    fun updateAirconInfo(context: Context, airconInfo: AirconInfo) {
        val database = AirconDatabase(context).writableDatabase

        database.use { db ->
            val update = ContentValues().apply {
                put("status", airconInfo.status)
                put("mode", airconInfo.mode)
                put("temperature", airconInfo.temp)
            }
            db.update("AirconInfos",
                    update,
                    "position = ?",
                    arrayOf(airconInfo.position.toString()))
        }
    }
}