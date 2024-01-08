package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        binding.apply.setOnClickListener {
            val title = binding.posttitle.text.toString()
            val context = binding.postcontext.text.toString()
            Log.d("title", title)
            Log.d("context", context)
            if(title != "" && context != ""){
                api.createBoard(boardcreate(nickname, title, context, boardClassName))
                    .enqueue(object : Callback<RegisterResult> {
                        override fun onResponse(
                            call: Call<RegisterResult>,
                            response: Response<RegisterResult>
                        ) {
                            val response: RegisterResult = response.body() ?: return
                            if (response.message == true) {
                                Log.d("게시글 생성 성공", title)
                                Toast.makeText(applicationContext, "게시글 생성 성공", Toast.LENGTH_SHORT)
                                val intent = Intent(this@CreateBoard, Board::class.java)
                                intent.putExtra("boardclass", boardClassName)
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                            Log.d("testt", t.message.toString())
                        }
                    })
            }
        }
        binding.arrowback.setOnClickListener{
            val intent = Intent(this@CreateBoard, Board::class.java)
            intent.putExtra("boardclass", boardClassName)
            startActivity(intent)
            finish()
        }
    }
}