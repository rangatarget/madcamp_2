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
        Log.d("지금 MainActivity고 profile 디코딩한거", profile)
        if(profile != ""){
            val profile_bitmap = decodeBase64ToImage(profile)
            Glide.with(this@MainActivity).load(profile_bitmap).circleCrop().into(binding.userprofile)
            binding.userprofile.setImageBitmap(profile_bitmap)
        }
        //즐겨찾기 게시판 불러오기
        api.getCheckedBoardClass(Checkedboardclass(user_id)).enqueue(object: Callback<ArrayList<BoardClassModel>> {
            override fun onResponse(
                call: Call<ArrayList<BoardClassModel>>,
                response: Response<ArrayList<BoardClassModel>>
            ) {
                val boards: ArrayList<BoardClassModel> = response.body() ?: return
                Log.d("즐겨찾기게시판가져오기", boards.size.toString())
                boards.sortBy { it.name }
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
        //추천수 top5 게시물 불러오기
        api.getTop5(getmy(user_id)).enqueue(object: Callback<ArrayList<BoardModel>> {
            override fun onResponse(
                call: Call<ArrayList<BoardModel>>,
                response: Response<ArrayList<BoardModel>>
            ) {
                val boards: ArrayList<BoardModel> = response.body() ?: return
                Log.d("인기게시글가져오기", boards.size.toString())
                for (board in boards) {
                    Log.d("인기게시글가져오기", "board: $board")
                }
                val layoutManager = LinearLayoutManager(this@MainActivity)
                binding.top5List.layoutManager = layoutManager
                val adapter = BoardAdapter(this@MainActivity, boards, " ")
                binding.top5List.adapter = adapter
            }
            override fun onFailure(call: Call<ArrayList<BoardModel>>, t: Throwable) {
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
        binding.userprofile.setOnClickListener{
            val intent = Intent(this, ProfileSetting::class.java)
            startActivity(intent)
            finish()
        }

        binding.searchbutton.setOnClickListener{
            val searchkeyword = binding.searchtext.text.toString()
            val intent = Intent(this, SearchBlog::class.java)
            intent.putExtra("searchkeyword", searchkeyword)
            startActivity(intent)
            finish()
        }
    }

}