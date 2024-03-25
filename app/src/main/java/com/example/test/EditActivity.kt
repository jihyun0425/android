package com.example.test

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test.model.Member

class EditActivity : AppCompatActivity() {
    private lateinit var dbHelper: MyDBHelper
    private var member: Member? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        dbHelper = MyDBHelper(this)

        val memberId = intent.getIntExtra("MEMBER_ID", -1)
        if (memberId != -1) {
            member = getMemberFromDatabase(memberId)
            if (member != null) {
                displayMemberDetails(member!!)
            } else {
                Toast.makeText(this, "회원을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "잘못된 회원 ID입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        val updateButton: Button = findViewById(R.id.update_button)
        updateButton.setOnClickListener {
            updateMember()
        }
    }

    private fun displayMemberDetails(member: Member) {
        val nameEditText: EditText = findViewById(R.id.name_edit_text)
        val mobileEditText: EditText = findViewById(R.id.mobile_edit_text)
        val ageEditText: EditText = findViewById(R.id.age_edit_text)

        nameEditText.setText(member.name)
        mobileEditText.setText(member.mobile)
        ageEditText.setText(member.age.toString())
    }

    @SuppressLint("Range")
    private fun getMemberFromDatabase(memberId: Int): Member? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "members",
            arrayOf("id", "name", "mobile", "age"),
            "id = ?",
            arrayOf(memberId.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val mobile = cursor.getString(cursor.getColumnIndex("mobile"))
            val age = cursor.getInt(cursor.getColumnIndex("age"))
            Member(id, name, mobile, age)
        } else {
            null
        }
    }

    private fun updateMember() {
        val nameEditText: EditText = findViewById(R.id.name_edit_text)
        val mobileEditText: EditText = findViewById(R.id.mobile_edit_text)
        val ageEditText: EditText = findViewById(R.id.age_edit_text)

        val newName = nameEditText.text.toString()
        val newMobile = mobileEditText.text.toString()
        val newAge = ageEditText.text.toString().toIntOrNull()

        if (newName.isNotEmpty() && newMobile.isNotEmpty() && newAge != null && member != null) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("name", newName)
                put("mobile", newMobile)
                put("age", newAge)
            }
            val result = db.update("members", values, "id = ?", arrayOf(member!!.id.toString()))
            if (result > 0) {
                Toast.makeText(this, "회원 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                // 상세 페이지로 돌아가서 변경된 정보를 다시 불러옴
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("MEMBER_ID", member!!.id)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "회원 정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
            db.close()
        } else {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
