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

class BlogAdapter(val context: Context, private val itemList: ArrayList<BlogModel>) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
    // 뷰홀더 클래스 정의
    inner class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val contents: TextView = itemView.findViewById(R.id.context)

        fun bind(item: BlogModel) {
            itemView.setOnClickListener {
                Log.d("블로그 눌림", item.title)
            }
        }
    }

    // onCreateViewHolder: ViewHolder가 생성될 때 호출되는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.blogitem, parent, false)
        Log.d("onCreateViewHolder","")
        return BlogViewHolder(view)
    }

    // onBindViewHolder: ViewHolder가 데이터와 연결될 때 호출되는 메서드
    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val item = itemList[position]
        holder.title.text = removeTags(item.title)
        val originalText = removeTags(item.description)
        val maxLength = 30
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
        Log.d("onBindViewHolder",item.title)
        holder.bind(item)
    }

    // getItemCount: 전체 아이템 수 반환
    override fun getItemCount(): Int {
        Log.d("getItemCount",itemList.size.toString())
        return itemList.size
    }
}