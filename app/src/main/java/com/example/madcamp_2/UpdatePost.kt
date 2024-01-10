package com.example.madcamp_2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.madcamp_2.databinding.ActivityCreateBoardBinding
import com.example.madcamp_2.databinding.ActivityUpdatePostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePost : AppCompatActivity() {
    private lateinit var binding: ActivityUpdatePostBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivedIntent = intent
        val post_id = receivedIntent.getStringExtra("post_id")?.toInt()!!
        val classname = receivedIntent.getStringExtra("boardclass").toString()
        val old_posttitle = receivedIntent.getStringExtra("posttitle").toString()
        val old_postcontext = receivedIntent.getStringExtra("postcontext").toString()
        binding.posttitle.setText(old_posttitle)
        binding.postcontext.setText(old_postcontext)
        binding.apply.setOnClickListener {
            val title = binding.posttitle.text.toString()
            val context = binding.postcontext.text.toString()
            Log.d("title", title)
            Log.d("context", context)
            if (title != "" && context != "") {
                api.updatePost(update_post(post_id, title, context))
                    .enqueue(object : Callback<RegisterResult> {
                        override fun onResponse(
                            call: Call<RegisterResult>,
                            response: Response<RegisterResult>
                        ) {
                            val response: RegisterResult = response.body() ?: return
                            if (response.message == true) {
                                Log.d("게시글 수정 성공", title)
                                Toast.makeText(applicationContext, "게시글 수정 성공", Toast.LENGTH_SHORT)
                                val intent = Intent(this@UpdatePost, Board::class.java)
                                intent.putExtra("boardclass", classname)
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                            Log.d("testt", t.message.toString())
                        }
                    })
            } else Toast.makeText(applicationContext, "빈 칸이 있습니다.", Toast.LENGTH_SHORT)
        }
        binding.arrowback.setOnClickListener{
            val intent = Intent(this@UpdatePost, Board::class.java)
            intent.putExtra("boardclass", classname)
            startActivity(intent)
            finish()
        }
    }
}