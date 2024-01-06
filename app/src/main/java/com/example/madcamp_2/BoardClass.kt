package com.example.madcamp_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityBoardclassBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardClass : AppCompatActivity() {
    private lateinit var binding: ActivityBoardclassBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardclassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api.getBoardClass().enqueue(object: Callback<ArrayList<BoardClassModel>> {
            override fun onResponse(
                call: Call<ArrayList<BoardClassModel>>,
                response: Response<ArrayList<BoardClassModel>>
            ) {
                val boardList: ArrayList<BoardClassModel> = response.body() ?: return
                for (board in boardList) {
                    Log.d("board가져오기", "board: $board")
                }
                val layoutManager = LinearLayoutManager(this@BoardClass)
                binding.rcvBoardClass.layoutManager = layoutManager
                val adapter = BoardClassAdapter(boardList)
                binding.rcvBoardClass.adapter = adapter
            }

            override fun onFailure(call: Call<ArrayList<BoardClassModel>>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })
    }


}