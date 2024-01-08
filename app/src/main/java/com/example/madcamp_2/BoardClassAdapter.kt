package com.example.madcamp_2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardClassAdapter(val context: Context, private val itemList: ArrayList<BoardClassModel>) :
    RecyclerView.Adapter<BoardClassAdapter.BoardClassViewHolder>() {

    // 뷰홀더 클래스 정의
    inner class BoardClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val enterBoard: View = itemView.findViewById(R.id.enterBoard)
        val addStar: View = itemView.findViewById(R.id.addStar)
        fun bind(item: BoardClassModel){
            enterBoard.setOnClickListener{
                val intent = Intent(context, Board::class.java)
                intent.putExtra("boardclass", item.name)
                itemView.context.startActivity(intent)
                (context as Activity).finish()
            }
            addStar.setOnClickListener{
                Log.v("즐겨찾기 버튼 누름", "게시판 이름 : " + item.name)
            }
        }
    }

    // onCreateViewHolder: ViewHolder가 생성될 때 호출되는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.boardclassitem, parent, false)
        Log.d("onCreateViewHolder","")
        return BoardClassViewHolder(view)
    }

    // onBindViewHolder: ViewHolder가 데이터와 연결될 때 호출되는 메서드
    override fun onBindViewHolder(holder: BoardClassViewHolder, position: Int) {
        val item = itemList[position]
        holder.nameTextView.text = item.name
        Log.d("BoardClass bind",item.name)
        holder.bind(item)
    }

    // getItemCount: 전체 아이템 수 반환
    override fun getItemCount(): Int {
        Log.d("getItemCount",itemList.size.toString())
        return itemList.size
    }
}