package com.example.test

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "members.db"
        private const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = "CREATE TABLE members (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "mobile TEXT NOT NULL UNIQUE," +
                "age INTEGER)"
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // 이전 버전의 테이블을 삭제하고 새로 생성
            db.execSQL("DROP TABLE IF EXISTS members")
            onCreate(db)
        }
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: MyDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = MyDBHelper(this)

        // 등록 버튼 클릭 이벤트 처리
        val addButton: Button = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 리스트 버튼 클릭 이벤트 처리
        val listButton: Button = findViewById(R.id.show_button)
        listButton.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }

}


