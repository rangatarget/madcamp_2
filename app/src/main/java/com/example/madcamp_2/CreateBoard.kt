package com.example.madcamp_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityBoardBinding
import com.example.madcamp_2.databinding.ActivityCreateBoardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBoard : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBoardBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivedIntent = intent
        val boardClassName = receivedIntent.getStringExtra("boardclass").toString()
        val nickname = MyApplication.prefs.getString("nickname", "")
        val title = binding.posttitle.text.toString()
        var context = binding.postcontext.text.toString()
        api.createBoard(Createboard(nickname, title, context, boardClassName)).enqueue(object: Callback<RegisterResult> {
            override fun onResponse(
                call: Call<RegisterResult>,
                response: Response<RegisterResult>
            ) {
                val response: RegisterResult = response.body() ?: return
            }

            override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })
    }
}