package com.example.test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test.model.Member

class DetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: MyDBHelper
    private var member: Member? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = MyDBHelper(this)

        val memberId = intent.getIntExtra("MEMBER_ID", -1)
        if (memberId != -1) {
            val retrievedMember = getMemberFromDatabase(memberId)
            if (retrievedMember != null) {
                member = retrievedMember
                displayMemberDetails(member!!)
            } else {
                Toast.makeText(this, "회원을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "잘못된 회원 ID입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 수정 버튼 클릭 이벤트 처리
        val editButton: Button = findViewById(R.id.edit_button)
        editButton.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("MEMBER_ID", member?.id)
            startActivity(intent)
        }

        // 삭제 버튼 클릭 이벤트 처리
        val deleteButton: Button = findViewById(R.id.delete_button)
        deleteButton.setOnClickListener {
            member?.id?.let { memberId ->
                deleteMember(memberId)
            }
        }

        // 목록 버튼 클릭 이벤트 처리
        val listButton: Button = findViewById(R.id.list_button)
        listButton.setOnClickListener {
            // 리스트 페이지로 이동하여 변경된 정보를 반영
            loadMemberList()
            finish()
        }
    }

    private fun displayMemberDetails(member: Member) {
        val nameTextView: TextView = findViewById(R.id.name_text_view)
        val mobileTextView: TextView = findViewById(R.id.mobile_text_view)
        val ageTextView: TextView = findViewById(R.id.age_text_view)

        nameTextView.text = "이름: ${member.name}"
        mobileTextView.text = "전화번호: ${member.mobile}"
        ageTextView.text = "나이: ${member.age}"
    }

    private fun deleteMember(memberId: Int) {
        val db = dbHelper.writableDatabase
        val result = db.delete("members", "id = ?", arrayOf(memberId.toString()))
        if (result > 0) {
            Toast.makeText(this, "회원이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            // 리스트 페이지로 이동하여 변경된 정보를 반영
            loadMemberList()
            finish()
        } else {
            Toast.makeText(this, "회원 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
        db.close()
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

        private fun loadMemberList() {
        // 리스트 페이지로 이동하여 변경된 정보를 반영
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
