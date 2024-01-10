package com.example.madcamp_2

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
        val author_nickname = receivedIntent.getStringExtra("author_nickname").toString()
        val context = receivedIntent.getStringExtra("context").toString()
        val _idtemp = receivedIntent.getStringExtra("_id")?.toInt()
        val user_id = MyApplication.prefs.getString("id", "")
        val nickname = MyApplication.prefs.getString("nickname", "")
        var isrecommended = false
        var recommendnum = 0

        if(_idtemp == null){
            Log.d("_id가 null","")
            val intent = Intent(this@Post, Board::class.java)
            intent.putExtra("boardclass", classname)
            startActivity(intent)
            finish()
        }
        val _id : Int = _idtemp!!
        Log.d("title : ",title)
        binding.posttitle.text = title
        binding.author.text = author_nickname
        binding.context.text = context

        api.getAuthorImage(giveuserandpost(user_id, _id, author)).enqueue(object: Callback<imageandisrecommend> {
            override fun onResponse(
                call: Call<imageandisrecommend>,
                response: Response<imageandisrecommend>
            ) {
                val response: imageandisrecommend = response.body() ?: return
                if(response.image != null && response.image.length !=0){
                    val profile_bitmap = decodeBase64ToImage(response.image)
                    Glide.with(this@Post).load(profile_bitmap).circleCrop().into(binding.authorprofile)
                }
                if(response.isRecommended == true){
                    isrecommended = true
                    binding.thumb.setImageResource(R.drawable.thumb_blue)
                }
            }

            override fun onFailure(call: Call<imageandisrecommend>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })

        if(user_id != author){
            binding.deletepost.visibility = View.GONE
            binding.updatepost.visibility = View.GONE
        }

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
                binding.commentnumber.text = "댓글 " + comments.size + "개"
                val layoutManager = LinearLayoutManager(this@Post)
                binding.rcvComments.layoutManager = layoutManager
                val adapter = CommentAdapter(this@Post, comments, user_id, classname, title, author, context, _id,author_nickname)
                binding.rcvComments.adapter = adapter
            }

            override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })

        api.getRecommend(giveidnum(_id)).enqueue(object: Callback<onlynumber> {
            override fun onResponse(
                call: Call<onlynumber>,
                response: Response<onlynumber>
            ) {
                val response: onlynumber = response.body() ?: return
                Log.d("추천 수", "recommends: " + response.toString())
                recommendnum = response.recommendcount
                binding.recommendnumber.setText("추천 수 : " + recommendnum)
            }

            override fun onFailure(call: Call<onlynumber>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })

        binding.thumb.setOnClickListener{
            api.isRecommended(giveforrecommend(!isrecommended, _id, user_id)).enqueue(object: Callback<RegisterResult> {
                override fun onResponse(
                    call: Call<RegisterResult>,
                    response: Response<RegisterResult>
                ) {
                    val response: RegisterResult = response.body() ?: return
                    if(response.message == true){
                        if(isrecommended == true){
                            isrecommended = false
                            binding.thumb.setImageResource(R.drawable.thumb_gray)
                            recommendnum = recommendnum - 1
                            binding.recommendnumber.setText("추천 수 : " + recommendnum)
                        }
                        else{
                            isrecommended = true
                            binding.thumb.setImageResource(R.drawable.thumb_blue)
                            recommendnum = recommendnum + 1
                            binding.recommendnumber.setText("추천 수 : " + recommendnum)
                        }
                    }
                    else{
                        Log.d("ERROR","")
                    }
                }

                override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                    Log.d("testt",t.message.toString())
                }
            })
        }

        binding.arrowback.setOnClickListener {
            val intent = Intent(this@Post, Board::class.java)
            intent.putExtra("boardclass", classname)
            startActivity(intent)
            finish()
        }
        binding.makecomment.setOnClickListener {
            val comment = binding.writecomment.text.toString()
            api.createComment(commentcreate(user_id, nickname, comment, _id)).enqueue(object: Callback<RegisterResult> {
                override fun onResponse(
                    call: Call<RegisterResult>,
                    response: Response<RegisterResult>
                ) {
                    val response: RegisterResult = response.body() ?: return
                    if(response.message == true){
                        Toast.makeText(applicationContext, "댓글이 추가되었습니다", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Post, Post::class.java)
                        intent.putExtra("classname", classname)
                        intent.putExtra("title", title)
                        intent.putExtra("author", author)
                        intent.putExtra("author", author_nickname)
                        intent.putExtra("author_nickname", author_nickname)
                        intent.putExtra("context", context)
                        intent.putExtra("_id", _id.toString())
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext, "서버 오류", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                    Log.d("testt",t.message.toString())
                }
            })
        }
        binding.deletepost.setOnClickListener{
            Log.d("게시글 삭제 눌림","게시글 id : " + _id.toString())
            api.deletePost(deletepost(_id)).enqueue(object: Callback<RegisterResult> {
                override fun onResponse(
                    call: Call<RegisterResult>,
                    response: Response<RegisterResult>
                ) {
                    val response: RegisterResult = response.body() ?: return
                    if(response.message == true){
                        val intent = Intent(this@Post, Board::class.java)
                        intent.putExtra("boardclass", classname)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                    Log.d("testt",t.message.toString())
                }
            })
        }
        binding.updatepost.setOnClickListener{
            val intent = Intent(this@Post, UpdatePost::class.java)
            intent.putExtra("boardclass", classname)
            intent.putExtra("post_id", _id.toString())
            intent.putExtra("posttitle", title)
            intent.putExtra("postcontext", context)
            startActivity(intent)
            finish()
        }
    }
}