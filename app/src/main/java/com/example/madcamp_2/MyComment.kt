package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityMainBinding
import com.example.madcamp_2.databinding.ActivityMyBoardBinding
import com.example.madcamp_2.databinding.ActivityMyBoardClassBinding
import com.example.madcamp_2.databinding.ActivityMyCommentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyComment : AppCompatActivity() {
    private lateinit var binding: ActivityMyCommentBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user_id = MyApplication.prefs.getString("id", "")
        val nickname = MyApplication.prefs.getString("nickname", "")
        val profile = MyApplication.prefs.getString("profile", "")

        api.getMyComment(getmy(user_id)).enqueue(object: Callback<ArrayList<Comment>> {
            override fun onResponse(
                call: Call<ArrayList<Comment>>,
                response: Response<ArrayList<Comment>>
            ) {
                val boards: ArrayList<Comment> = response.body() ?: return
                for (board in boards) {
                    Log.d("댓글가져오기", "board: $board")
                }
                val layoutManager = LinearLayoutManager(this@MyComment)
                binding.rcvBoard.layoutManager = layoutManager
                val adapter = CommentAdapter(this@MyComment, boards, user_id, "", "", "", "", 0,nickname)
                binding.rcvBoard.adapter = adapter

            }

            override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
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