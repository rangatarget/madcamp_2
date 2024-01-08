package com.example.madcamp_2

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

        binding.userpassword.setOnClickListener{
            Log.d("패스워드 변경 버튼 눌림", "")
        }
        binding.profile.setOnClickListener{
            Log.d("프로필 사진 변경 버튼 눌림", "")
        }


    }
}