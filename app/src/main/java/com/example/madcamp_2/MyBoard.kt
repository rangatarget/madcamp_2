package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityMainBinding
import com.example.madcamp_2.databinding.ActivityMyBoardClassBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBoard : AppCompatActivity() {
    private lateinit var binding: ActivityMyBoardClassBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBoardClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user_id = MyApplication.prefs.getString("id", "")
        val nickname = MyApplication.prefs.getString("nickname", "")
        val profile = MyApplication.prefs.getString("profile", "")


        binding.arrowback.setOnClickListener {
            val intent = Intent(this, ProfileSetting::class.java)
            startActivity(intent)
            finish()
        }
    }
}