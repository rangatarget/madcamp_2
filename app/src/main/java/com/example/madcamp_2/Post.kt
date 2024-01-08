package com.example.madcamp_2

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.author.text = "작성자 : " + author_nickname
        binding.context.text = context
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
                val adapter = CommentAdapter(this@Post, comments, user_id, classname, title, author, context, _id)
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
        binding.addcomment.setOnClickListener {
            showCommentInput(_id, user_id, nickname, classname, title, author, context)
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

    private fun showCommentInput(_id: Int, user_id: String, nickname: String, classname: String, post_title: String, post_author: String, post_context: String) {
        // LayoutInflater를 사용하여 XML 레이아웃 파일을 View 객체로 변환
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.createcomment, null)

        // 다이얼로그 생성
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("댓글 추가")

        val alertDialog = alertDialogBuilder.create()

        // 다이얼로그 내의 버튼과 에디트 텍스트에 대한 처리
        val editTextTitle = dialogView.findViewById<EditText>(R.id.editComment)
        val buttonAdd = dialogView.findViewById<Button>(R.id.buttonAdd)

        buttonAdd.setOnClickListener {
            val context = editTextTitle.text.toString()
            // TODO: 여기서 새로운 제목을 처리하거나 저장하는 로직을 구현
            api.createComment(commentcreate(user_id, nickname, context, _id)).enqueue(object: Callback<RegisterResult> {
                override fun onResponse(
                    call: Call<RegisterResult>,
                    response: Response<RegisterResult>
                ) {
                    val response: RegisterResult = response.body() ?: return
                    if(response.message == true){
                        Toast.makeText(applicationContext, "댓글이 추가되었습니다", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Post, Post::class.java)
                        intent.putExtra("classname", classname)
                        intent.putExtra("title", post_title)
                        intent.putExtra("author", post_author)
                        intent.putExtra("context", post_context)
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

            // 다이얼로그를 닫음
            alertDialog.dismiss()
        }

        // 다이얼로그 표시
        alertDialog.show()
    }
}