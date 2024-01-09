package com.example.madcamp_2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.madcamp_2.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user_id = MyApplication.prefs.getString("id", "")
        val nickname = MyApplication.prefs.getString("nickname", "")
        val profile = MyApplication.prefs.getString("profile", "")
        if(user_id == "") {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        //프로필 사진과 닉네임 불러오기
        binding.usernickname.setText(nickname)
        Log.d("profile", profile)
        if(profile != "") Glide.with(this).load(profile).circleCrop().into(binding.userprofile)
        //즐겨찾기 게시판 불러오기
        api.getCheckedBoardClass(Checkedboardclass(nickname)).enqueue(object: Callback<ArrayList<BoardClassModel>> {
            override fun onResponse(
                call: Call<ArrayList<BoardClassModel>>,
                response: Response<ArrayList<BoardClassModel>>
            ) {
                val boards: ArrayList<BoardClassModel> = response.body() ?: return
                for (board in boards) {
                    Log.d("즐겨찾기게시판가져오기", "board: $board")
                }
                val layoutManager = LinearLayoutManager(this@MainActivity)
                binding.rcvCheckedBoardClass.layoutManager = layoutManager
                val adapter = BoardClassAdapter(this@MainActivity, boards, false, user_id, true)
                binding.rcvCheckedBoardClass.adapter = adapter
            }

            override fun onFailure(call: Call<ArrayList<BoardClassModel>>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })
        //더보기 버튼
        binding.moreBoardClass.setOnClickListener{
            val intent = Intent(this, BoardClass::class.java)
            startActivity(intent)
            finish()
        }
        //프로필 버튼
        binding.enterProfile.setOnClickListener{
            val intent = Intent(this, ProfileSetting::class.java)
            startActivity(intent)
            finish()
        }
    }

}