package com.example.madcamp_2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

<<<<<<< HEAD:app/src/main/java/com/example/madcamp_2/CommentAdapter.kt
class CommentAdapter(val context: Context, private val itemList: ArrayList<Comment>, val user_id : String, val classname: String, val post_title: String, val post_author: String, val post_context: String, val _id: Int,val author_nickname: String) :
=======
class CommentAdapter(val context: Context, private val itemList: ArrayList<Comment>, val user_id : String, val classname: String, val post_title: String, val post_author: String, val post_context: String, val _id: Int, val author_nickname: String) :
>>>>>>> ehj:frontend/app/src/main/java/com/example/madcamp_2/CommentAdapter.kt
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    // 뷰홀더 클래스 정의
    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val writer: TextView = itemView.findViewById(R.id.writer)
        val comment: TextView = itemView.findViewById(R.id.context)
        val delete: TextView = itemView.findViewById(R.id.delete)
        val update: TextView = itemView.findViewById(R.id.update)
        val profile: ImageView = itemView.findViewById(R.id.userprofile)
        val bottomLine: View = itemView.findViewById(R.id.bottomLine)

        fun bind(item: Comment){
            delete.setOnClickListener {
                val api = RetroInterface.create()
                Log.d("댓글삭제 눌림", item._id.toString() + "의 댓글")
                api.deleteComment(deletecomment(item._id)).enqueue(object:
                    Callback<RegisterResult> {
                    override fun onResponse(
                        call: Call<RegisterResult>,
                        response: Response<RegisterResult>
                    ) {
                        val response: RegisterResult = response.body() ?: return
                        if(response.message == true){
                            Toast.makeText(context, "댓글이 삭제되었습니다", Toast.LENGTH_SHORT).show()
                            if(classname == ""){
                                val intent = Intent(context as Activity, MyComment::class.java)
                                (context as Activity).startActivity(intent)
                                context.finish()
                            }
                            else{
                                val intent = Intent(context as Activity, Post::class.java)
                                intent.putExtra("classname", classname)
                                intent.putExtra("title", post_title)
                                intent.putExtra("author", post_author)
                                intent.putExtra("context", post_context)
                                intent.putExtra("_id", _id.toString())
                                (context as Activity).startActivity(intent)
                                context.finish()
                            }
                        }
                        else{
                            Toast.makeText(context, "서버 오류", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                        Log.d("testt",t.message.toString())
                    }
                })
            }
            update.setOnClickListener {
                showCommentUpdate(item._id, _id, item.context)
            }
        }
    }

    // onCreateViewHolder: ViewHolder가 생성될 때 호출되는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.commentitem, parent, false)
        return CommentViewHolder(view)
    }

    // onBindViewHolder: ViewHolder가 데이터와 연결될 때 호출되는 메서드
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = itemList[position]
        holder.writer.text = item.writer_nickname
        holder.comment.text = item.context
        val profile = item.image
        if(profile != null){
            val profile_bitmap = decodeBase64ToImage(profile)
            Glide.with(context).load(profile_bitmap).circleCrop().into(holder.profile)
        }
        if(user_id != item.writer || classname == "") holder.delete.visibility=View.GONE
        if(user_id != item.writer || classname == "") holder.update.visibility=View.GONE
        if(post_author == item.writer || classname == "") holder.writer.setTextColor(Color.parseColor("#2051B5"))
        if(user_id == item.writer || classname == "") holder.writer.setTextColor(Color.parseColor("#439800"))

        if (position == itemList.size - 1) {
            holder.bottomLine.visibility = View.GONE
        } else {
            holder.bottomLine.visibility = View.VISIBLE
        }

        holder.bind(item)
    }

    // getItemCount: 전체 아이템 수 반환
    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun showCommentUpdate(_id: Int, board_id: Int, old_context: String) {
        // LayoutInflater를 사용하여 XML 레이아웃 파일을 View 객체로 변환
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.updatecomment, null)

        // 다이얼로그 생성
        val alertDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("댓글 추가")

        val alertDialog = alertDialogBuilder.create()

        // 다이얼로그 내의 버튼과 에디트 텍스트에 대한 처리
        val editComment = dialogView.findViewById<EditText>(R.id.editComment)
        val buttonAdd = dialogView.findViewById<Button>(R.id.buttonAdd)
        editComment.setText(old_context)

        buttonAdd.setOnClickListener {
            val new_context = editComment.text.toString()
            // TODO: 여기서 새로운 제목을 처리하거나 저장하는 로직을 구현
            val api = RetroInterface.create()
            Log.d("댓글수정 눌림", "_id : " + _id.toString())
            api.updateComment(updatecomment(_id, new_context)).enqueue(object:
                Callback<RegisterResult> {
                override fun onResponse(
                    call: Call<RegisterResult>,
                    response: Response<RegisterResult>
                ) {
                    val response: RegisterResult = response.body() ?: return
                    if(response.message == true){
                        Toast.makeText(context, "댓글이 수정되었습니다", Toast.LENGTH_SHORT).show()
                        if(classname == ""){
                            val intent = Intent(context as Activity, MyComment::class.java)
                            (context as Activity).startActivity(intent)
                            context.finish()
                        }
                        else{
                            val intent = Intent(context as Activity, Post::class.java)
                            intent.putExtra("classname", classname)
                            intent.putExtra("title", post_title)
                            intent.putExtra("author", post_author)
                            intent.putExtra("author_nickname", author_nickname)
<<<<<<< HEAD:app/src/main/java/com/example/madcamp_2/CommentAdapter.kt
                            Log.d("Adapter",author_nickname)
=======
>>>>>>> ehj:frontend/app/src/main/java/com/example/madcamp_2/CommentAdapter.kt
                            intent.putExtra("context", post_context)
                            intent.putExtra("_id", _id.toString())
                            (context as Activity).startActivity(intent)
                            context.finish()
                        }
                    }
                    else{
                        Toast.makeText(context, "서버 오류", Toast.LENGTH_SHORT).show()
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