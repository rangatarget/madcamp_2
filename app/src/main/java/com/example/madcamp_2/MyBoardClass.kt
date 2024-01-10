package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityMainBinding
import com.example.madcamp_2.databinding.ActivityMyBoardClassBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBoardClass : AppCompatActivity() {
    private lateinit var binding: ActivityMyBoardClassBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBoardClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user_id = MyApplication.prefs.getString("id", "")
        val nickname = MyApplication.prefs.getString("nickname", "")
        val profile = MyApplication.prefs.getString("profile", "")

        api.getMyBoardClass(getmy(user_id)).enqueue(object:
            Callback<ArrayList<BoardClassModel>> {
            override fun onResponse(
                call: Call<ArrayList<BoardClassModel>>,
                response: Response<ArrayList<BoardClassModel>>
            ) {
                val boards: ArrayList<BoardClassModel> = response.body() ?: return
                for (board in boards) {
                    Log.d("내가 만든 게시판 가져오기", "board: $board")
                }
                val layoutManager = LinearLayoutManager(this@MyBoardClass)
                binding.rcvMyBoardClass.layoutManager = layoutManager
                val adapter = BoardClassAdapter(this@MyBoardClass, boards, true, user_id, false)
                binding.rcvMyBoardClass.adapter = adapter
            }

            override fun onFailure(call: Call<ArrayList<BoardClassModel>>, t: Throwable) {
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