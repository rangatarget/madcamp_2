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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardAdapter(val context: Context, private val itemList: ArrayList<BoardModel>, val classname: String) :
    RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {
    // 뷰홀더 클래스 정의
    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val contents: TextView = itemView.findViewById(R.id.contents)
        val author: TextView = itemView.findViewById(R.id.author)
        val recomnum: TextView = itemView.findViewById(R.id.recommendnum)
        val bottomLine: View = itemView.findViewById(R.id.bottomLine)

        fun bind(item: BoardModel) {
            itemView.setOnClickListener {
                Log.d("게시물 눌림", item.title)
                if(classname ==""){
                    return@setOnClickListener
                }
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
        holder.author.setText("글쓴이 : " + item.author_nickname)
        val api = RetroInterface.create()
        api.getRecommend(giveidnum(item._id)).enqueue(object: Callback<onlynumber> {
            override fun onResponse(
                call: Call<onlynumber>,
                response: Response<onlynumber>
            ) {
                val response: onlynumber = response.body() ?: return
                Log.d("추천 수", "recommends: " + response.toString())
                val recommendnum = response.recommendcount
                holder.recomnum.setText("추천 수 : " + recommendnum)
            }

            override fun onFailure(call: Call<onlynumber>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })

        if (position == itemList.size - 1) {
            holder.bottomLine.visibility = View.GONE
        } else {
            holder.bottomLine.visibility = View.VISIBLE
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