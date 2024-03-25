package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.model.Member
import com.example.test.R

class MemberAdapter(private val memberList: List<Member>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.member_item, parent, false)
        return MemberViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val currentItem = memberList[position]
        holder.nameTextView.text = currentItem.name
        holder.mobileTextView.text = currentItem.mobile
        holder.ageTextView.text = currentItem.age.toString()

        holder.itemView.setOnClickListener {
            listener.onItemClick(currentItem)
        }
    }

    override fun getItemCount() = memberList.size

    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
        val mobileTextView: TextView = itemView.findViewById(R.id.mobile_text_view)
        val ageTextView: TextView = itemView.findViewById(R.id.age_text_view)
    }

    interface OnItemClickListener {
        fun onItemClick(member: Member)
    }
}

