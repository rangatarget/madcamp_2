package com.example.madcamp_2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityBoardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Board : AppCompatActivity() {
    private lateinit var binding: ActivityBoardBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivedIntent = intent
        val boardClassName = receivedIntent.getStringExtra("boardclass").toString()
        api.getBoard(BoardName(boardClassName)).enqueue(object: Callback<ArrayList<BoardModel>> {
            override fun onResponse(
                call: Call<ArrayList<BoardModel>>,
                response: Response<ArrayList<BoardModel>>
            ) {
                val boards: ArrayList<BoardModel> = response.body() ?: return
                for (board in boards) {
                    Log.d("게시물가져오기", "board: $board")
                }
                val layoutManager = LinearLayoutManager(this@Board)
                binding.rcvBoard.layoutManager = layoutManager
                val adapter = BoardAdapter(this@Board, boards, boardClassName)
                binding.rcvBoard.adapter = adapter
                binding.boardtitle.text = boardClassName

            }

            override fun onFailure(call: Call<ArrayList<BoardModel>>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })
        binding.arrowback.setOnClickListener{
            val intent = Intent(this, BoardClass::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonadd.setOnClickListener{
            Log.d("게시물 추가버튼 눌림","아직 구현 안함")
        }
    }
}