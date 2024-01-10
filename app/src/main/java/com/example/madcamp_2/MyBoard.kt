package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityMainBinding
import com.example.madcamp_2.databinding.ActivityMyBoardBinding
import com.example.madcamp_2.databinding.ActivityMyBoardClassBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBoard : AppCompatActivity() {
    private lateinit var binding: ActivityMyBoardBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user_id = MyApplication.prefs.getString("id", "")
        val nickname = MyApplication.prefs.getString("nickname", "")
        val profile = MyApplication.prefs.getString("profile", "")

        api.getMyBoard(getmy(user_id)).enqueue(object: Callback<ArrayList<BoardModel>> {
            override fun onResponse(
                call: Call<ArrayList<BoardModel>>,
                response: Response<ArrayList<BoardModel>>
            ) {
                val boards: ArrayList<BoardModel> = response.body() ?: return
                Log.d("게시물",  boards.size.toString() + " " + user_id)
                for (board in boards) {
                    Log.d("게시물가져오기", "board: $board")
                }
                val layoutManager = LinearLayoutManager(this@MyBoard)
                binding.rcvBoard.layoutManager = layoutManager
                val adapter = BoardAdapter(this@MyBoard, boards, "")
                binding.rcvBoard.adapter = adapter
            }

            override fun onFailure(call: Call<ArrayList<BoardModel>>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })

        binding.arrowback.setOnClickListener {
            val intent = Intent(this, ProfileSetting::class.java)
            startActivity(intent)
            finish()
        }
    }
}