package com.example.test

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.model.Member

class ListActivity : AppCompatActivity(), MemberAdapter.OnItemClickListener {

    private lateinit var memberAdapter: MemberAdapter
    private lateinit var dbHelper: MyDBHelper
    private var memberList: MutableList<Member> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        dbHelper = MyDBHelper(this)

        // 삭제된 회원의 ID를 전달받음
        val deletedMemberId = intent.getIntExtra("DELETED_MEMBER_ID", -1)
        if (deletedMemberId != -1) {
            // 삭제된 회원의 ID를 기반으로 회원을 리스트에서 제거
            removeDeletedMemberFromList(deletedMemberId)
        }

        // 회원 목록을 로드하여 화면에 표시
        loadMemberList()
    }

    private fun getMemberListFromDatabase(): List<Member> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf("id", "name", "mobile", "age")
        val cursor = db.query(
            "members",
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val memberList = mutableListOf<Member>()
        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndexOrThrow("id")
                val nameIndex = cursor.getColumnIndexOrThrow("name")
                val mobileIndex = cursor.getColumnIndexOrThrow("mobile")
                val ageIndex = cursor.getColumnIndexOrThrow("age")

                val id = cursor.getInt(idIndex)
                val name = cursor.getString(nameIndex)
                val mobile = cursor.getString(mobileIndex)
                val age = cursor.getInt(ageIndex)
                val member = Member(id, name, mobile, age)
                memberList.add(member)
            }
        }
        return memberList
    }

    private fun loadMemberList() {
        memberList = getMemberListFromDatabase().toMutableList()
        memberAdapter = MemberAdapter(memberList, this)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = memberAdapter
    }

    override fun onItemClick(member: Member) {
        // Handle item click, navigate to detail activity
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MEMBER_ID", member.id)
            // Pass additional member details if needed
        }
        startActivity(intent)
    }

    // 삭제된 회원의 ID를 기반으로 회원을 리스트에서 제거하는 함수
    private fun removeDeletedMemberFromList(deletedMemberId: Int) {
        val iterator = memberList.iterator()
        while (iterator.hasNext()) {
            val member = iterator.next()
            if (member.id == deletedMemberId) {
                iterator.remove()
                break
            }
        }
    }
}
