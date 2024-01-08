package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityBoardclassBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardClass : AppCompatActivity() {
    private lateinit var binding: ActivityBoardclassBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardclassBinding.inflate(layoutInflater)
        val user_id = MyApplication.prefs.getString("id", "")
        setContentView(binding.root)

        api.getBoardClass(getboardclass(user_id)).enqueue(object: Callback<ArrayList<BoardClassModel>> {
            override fun onResponse(
                call: Call<ArrayList<BoardClassModel>>,
                response: Response<ArrayList<BoardClassModel>>
            ) {
                val boardList: ArrayList<BoardClassModel> = response.body() ?: return
                for (board in boardList) {
                    Log.d("board가져오기", "board: $board")
                }
                val layoutManager = LinearLayoutManager(this@BoardClass)
                binding.rcvBoardClass.layoutManager = layoutManager
                val adapter = BoardClassAdapter(this@BoardClass, boardList, false, user_id, false)
                binding.rcvBoardClass.adapter = adapter
            }

            override fun onFailure(call: Call<ArrayList<BoardClassModel>>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })
        binding.buttonadd.setOnClickListener {
            showDialog()
        }
        binding.arrowback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun showDialog() {
        // LayoutInflater를 사용하여 XML 레이아웃 파일을 View 객체로 변환
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.createboardclass, null)

        // 다이얼로그 생성
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("새로운 게시판 추가")

        val alertDialog = alertDialogBuilder.create()

        // 다이얼로그 내의 버튼과 에디트 텍스트에 대한 처리
        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val buttonAdd = dialogView.findViewById<Button>(R.id.buttonAdd)

        buttonAdd.setOnClickListener {
            val newTitle = editTextTitle.text.toString()
            // TODO: 여기서 새로운 제목을 처리하거나 저장하는 로직을 구현
            val id = MyApplication.prefs.getString("id", "")
            api.createBoardClass(Createboardclass(newTitle, id)).enqueue(object: Callback<CreateboardclassResponse> {
                override fun onResponse(
                    call: Call<CreateboardclassResponse>,
                    response: Response<CreateboardclassResponse>
                ) {
                    val response: CreateboardclassResponse = response.body() ?: return
                    if(response.success == true){
                        Toast.makeText(applicationContext, "추가되었습니다", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@BoardClass, BoardClass::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext, "이미 존재하는 게시판 이름입니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CreateboardclassResponse>, t: Throwable) {
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