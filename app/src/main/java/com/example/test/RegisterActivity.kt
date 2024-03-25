package com.example.test

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: MyDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = MyDBHelper(this)

        // 등록 버튼 클릭 이벤트 처리
        val registerButton: Button = findViewById(R.id.register_button)
        registerButton.setOnClickListener {
            // 사용자로부터 이름, 나이, 번호 입력 받기
            val nameEditText: EditText = findViewById(R.id.name_edit_text)
            val ageEditText: EditText = findViewById(R.id.age_edit_text)
            val mobileEditText: EditText = findViewById(R.id.mobile_edit_text)

            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull()
            val mobile = mobileEditText.text.toString()

            // 이름과 나이는 필수 입력 필드이므로 입력되었는지 확인
            if (name.isEmpty() || age == null) {
                Toast.makeText(this, "이름과 나이는 필수 입력 필드입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 데이터베이스에 회원 추가
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("name", name)
                put("mobile", mobile)
                put("age", age)
            }
            db.insert("members", null, values)
            db.close()

            Toast.makeText(this, "회원이 등록되었습니다.", Toast.LENGTH_SHORT).show()

            // 입력 필드 초기화
            nameEditText.text.clear()
            ageEditText.text.clear()
            mobileEditText.text.clear()

        }
    }

}

