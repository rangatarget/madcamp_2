package com.example.madcamp_2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(val context: Context, private val itemList: ArrayList<BoardModel>, val classname: String) :
    RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {
    // 뷰홀더 클래스 정의
    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val contents: TextView = itemView.findViewById(R.id.contents)
        val author: TextView = itemView.findViewById(R.id.author)

        fun bind(item: BoardModel) {
            itemView.setOnClickListener {
                Log.d("게시물 눌림", item.title)
                val intent = Intent(context, Post::class.java)
                intent.putExtra("classname", classname)
                intent.putExtra("title", item.title)
                intent.putExtra("author", item.author)
                intent.putExtra("author_nickname", item.author_nickname)
                intent.putExtra("context", item.context)
                intent.putExtra("_id", item._id.toString())
                itemView.context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    // onCreateViewHolder: ViewHolder가 생성될 때 호출되는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.boarditem, parent, false)
        Log.d("onCreateViewHolder","")
        return BoardViewHolder(view)
    }

    // onBindViewHolder: ViewHolder가 데이터와 연결될 때 호출되는 메서드
    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val item = itemList[position]
        holder.title.text = item.title
        val originalText = item.context
        val maxLength = 25
        if(originalText != null && originalText.length > 0) {
            if (originalText.length > maxLength) {
                val truncatedText = originalText.substring(0, maxLength) + "..."
                holder.contents.text = truncatedText
            } else {
                holder.contents.text = originalText
            }
        }
        else{
            holder.contents.text = originalText
        }
        holder.author.text = "글쓴이 : " + item.author_nickname
        Log.d("onBindViewHolder",item.title)
        holder.bind(item)
    }

    // getItemCount: 전체 아이템 수 반환
    override fun getItemCount(): Int {
        Log.d("getItemCount",itemList.size.toString())
        return itemList.size
    }
}