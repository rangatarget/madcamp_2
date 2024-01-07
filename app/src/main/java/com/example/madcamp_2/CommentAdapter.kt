package com.example.madcamp_2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(val context: Context, private val itemList: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    // 뷰홀더 클래스 정의
    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val writer: TextView = itemView.findViewById(R.id.writer)
        val comment: TextView = itemView.findViewById(R.id.context)
    }

    // onCreateViewHolder: ViewHolder가 생성될 때 호출되는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.commentitem, parent, false)
        return CommentViewHolder(view)
    }

    // onBindViewHolder: ViewHolder가 데이터와 연결될 때 호출되는 메서드
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = itemList[position]
        holder.writer.text = item.writer
        holder.comment.text = item.context
    }

    // getItemCount: 전체 아이템 수 반환
    override fun getItemCount(): Int {
        return itemList.size
    }
}