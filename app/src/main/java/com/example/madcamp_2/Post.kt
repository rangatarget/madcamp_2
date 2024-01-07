package com.example.madcamp_2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityBoardBinding
import com.example.madcamp_2.databinding.ActivityPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Post : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivedIntent = intent
        val classname = receivedIntent.getStringExtra("classname").toString()
        val title = receivedIntent.getStringExtra("title").toString()
        val author = receivedIntent.getStringExtra("author").toString()
        val context = receivedIntent.getStringExtra("context").toString()
        val _idtemp = receivedIntent.getStringExtra("_id")?.toInt()

        if(_idtemp == null){
            Log.d("_id가 null","")
            val intent = Intent(this@Post, Board::class.java)
            startActivity(intent)
            finish()
        }
        val _id : Int = _idtemp!!
        Log.d("title : ",title)
        binding.posttitle.text = title
        binding.author.text = "작성자 : " + author
        binding.context.text = context

        api.getComments(getcomment(_id)).enqueue(object: Callback<ArrayList<Comment>> {
            override fun onResponse(
                call: Call<ArrayList<Comment>>,
                response: Response<ArrayList<Comment>>
            ) {
                val comments: ArrayList<Comment> = response.body() ?: return
                Log.d("댓글 가져오기", "comment size: " + comments.size)
                for (comment in comments) {
                    Log.d("댓글 가져오기", "comment: $comment")
                }
                val layoutManager = LinearLayoutManager(this@Post)
                binding.rcvComments.layoutManager = layoutManager
                val adapter = CommentAdapter(this@Post, comments)
                binding.rcvComments.adapter = adapter

            }

            override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })

        binding.arrowback.setOnClickListener {
            val intent = Intent(this@Post, Board::class.java)
            intent.putExtra("boardclass", classname)
            startActivity(intent)
            finish()
        }
    }
}