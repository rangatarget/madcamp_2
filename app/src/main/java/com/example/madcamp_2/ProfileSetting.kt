package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.madcamp_2.databinding.ActivityMainBinding
import com.example.madcamp_2.databinding.ActivityProfileSettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileSetting : AppCompatActivity() {
    private lateinit var binding: ActivityProfileSettingBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user_id = MyApplication.prefs.getString("id", "")
        val nickname = MyApplication.prefs.getString("nickname", "")
        val profile = MyApplication.prefs.getString("profile", "")

        binding.nickname.setText("닉네임 : " + nickname)
        binding.userid.setText("아이디 : " + user_id)
        if(profile != "") Glide.with(this).load(profile).circleCrop().into(binding.profile)
        else Glide.with(this).load(R.drawable.defaultprofile).circleCrop().into(binding.profile)

        binding.userpassword.setOnClickListener{
            Log.d("패스워드 변경 버튼 눌림", "")
        }
        binding.profile.setOnClickListener{
            Log.d("프로필 사진 변경 버튼 눌림", "")
        }
        api.getMyBoardClass(getmyboardclass(user_id)).enqueue(object:
            Callback<ArrayList<BoardClassModel>> {
            override fun onResponse(
                call: Call<ArrayList<BoardClassModel>>,
                response: Response<ArrayList<BoardClassModel>>
            ) {
                val boards: ArrayList<BoardClassModel> = response.body() ?: return
                for (board in boards) {
                    Log.d("내가 만든 게시판 가져오기", "board: $board")
                }
                val layoutManager = LinearLayoutManager(this@ProfileSetting)
                binding.rcvMyBoardClass.layoutManager = layoutManager
                val adapter = BoardClassAdapter(this@ProfileSetting, boards, true, user_id, false)
                binding.rcvMyBoardClass.adapter = adapter
            }

            override fun onFailure(call: Call<ArrayList<BoardClassModel>>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })

        binding.arrowback.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}