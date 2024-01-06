package com.example.madcamp_2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BoardClassAdapter(private val itemList: ArrayList<BoardClassModel>) :
    RecyclerView.Adapter<BoardClassAdapter.BoardClassViewHolder>() {

    // 뷰홀더 클래스 정의
    class BoardClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val createrTextView: TextView = itemView.findViewById(R.id.createrTextView)
    }

    // onCreateViewHolder: ViewHolder가 생성될 때 호출되는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.boardclassitem, parent, false)
        return BoardClassViewHolder(view)
    }

    // onBindViewHolder: ViewHolder가 데이터와 연결될 때 호출되는 메서드
    override fun onBindViewHolder(holder: BoardClassViewHolder, position: Int) {
        val item = itemList[position]
        holder.nameTextView.text = "${item.name}"
        holder.createrTextView.text = "${item.creater}"
    }

    // getItemCount: 전체 아이템 수 반환
    override fun getItemCount(): Int {
        return itemList.size
    }
}