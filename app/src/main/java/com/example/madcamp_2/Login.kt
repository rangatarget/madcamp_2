package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.madcamp_2.databinding.ActivityLoginBinding
import com.example.madcamp_2.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonsignup.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonlogin.setOnClickListener{
            binding.apply {
                val id = inputid.text.toString()
                val pw = inputpassword.text.toString()

                if(id == "" || pw == "") {
                    Toast.makeText(applicationContext, "입력하지 않은 정보가 있습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val loginUser = LoginModel(binding.inputid.text.toString(), binding.inputpassword.text.toString())
            api.login(loginUser).enqueue(object: Callback<LoginResult> {
                override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                    val user_token = response.body()?.token ?: return
                    val user_id = response.body()?.id ?: return
                    if(user_id != "") {
                        Toast.makeText(applicationContext, user_id + "로그인 성공", Toast.LENGTH_SHORT).show()
                        Log.d("로그인 성공", "user_token : " + user_token + "   user_id : " + user_id)
                        MyApplication.prefs.setString("id", user_id)
                        val intent = Intent(this@Login, BoardClass::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext, "로그인 실패, 아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                    Log.d("testt", t.message.toString())
                }
            })
        }
    }
}