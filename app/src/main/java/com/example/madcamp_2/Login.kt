package com.example.madcamp_2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.madcamp_2.databinding.ActivityLoginBinding
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("tag", "keyhash : ${Utility.getKeyHash(this)}")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonsignup.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        binding.kakaologin.setOnClickListener{
            kakaologin()
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
                    val user_id = response.body()?.id ?: return
                    val user_nickname = response.body()?.nickname ?: return
                    val user_profile = response.body()?.image ?: return
                    if(user_id != "") {
                        Toast.makeText(applicationContext, user_nickname + "로그인 성공", Toast.LENGTH_SHORT).show()
                        Log.d("로그인 성공", "user_nickname : " + user_nickname + "  profile_url : ")
                        MyApplication.prefs.setString("id", user_id)
                        MyApplication.prefs.setString("nickname", user_nickname)
                        MyApplication.prefs.setString("profile", user_profile)
                        val intent = Intent(this@Login, MainActivity::class.java)
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

    private fun kakaologin(){
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e("카카오", "로그인 실패", error)
            }
            else if (token != null) {
                Log.i("카카오", "로그인 성공 ${token.accessToken}")
                UserApiClient.instance.me { user, getError ->
                    if (getError != null) {
                        Log.d("loginkakao", "fail error")
                    }
                    else if (user != null) {
                        val userId = user.id.toString()
                        val nickname = user.kakaoAccount?.profile?.nickname.toString()
                        val profile = user.kakaoAccount?.profile?.profileImageUrl.toString()
                        Log.d("loginkakao", nickname + ", " + userId + ", " + profile)
                        api.kakaoLogin(kakaoExist(userId)).enqueue(object: Callback<RegisterResult> {
                            override fun onResponse(
                                call: Call<RegisterResult>,
                                response: Response<RegisterResult>
                            ) {
                                val isExist = response.body()?.message ?: return
                                if(isExist == true) {
                                    Toast.makeText(applicationContext, nickname + "님 환영합니다", Toast.LENGTH_SHORT).show()
                                    Log.d("카카오 로그인 성공", nickname)
                                    MyApplication.prefs.setString("nickname", nickname)
                                    MyApplication.prefs.setString("id", userId)
                                    MyApplication.prefs.setString("profile", profile)
                                    val intent = Intent(this@Login, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Toast.makeText(applicationContext, "회원가입 필요함", Toast.LENGTH_SHORT).show()
                                    Log.d("카카오 회원가입 필요", nickname)
                                    showDialog(userId, profile, nickname)
                                }
                            }
                            override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                                Log.d("testt", t.message.toString())
                            }
                        })
                    }
                }
            }
        }
    }

    private fun showDialog(id: String, profile: String, nickname: String) {
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.kakaoregister, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("카카오톡으로 계정 만들기")

        val alertDialog = alertDialogBuilder.create()

        // Spinner와 Button 찾기
        val spinnerClass = dialogView.findViewById<Spinner>(R.id.inputclass)
        val buttonAdd = dialogView.findViewById<Button>(R.id.buttonAdd)

        // Spinner에 사용할 ArrayAdapter 설정
        val classAdapter = ArrayAdapter.createFromResource(
            this, R.array.class_array, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerClass.adapter = classAdapter

        buttonAdd.setOnClickListener {
            val classes = spinnerClass.selectedItem.toString()

            // TODO: 여기서 선택된 분반을 처리하거나 저장하는 로직을 구현
            api.kakaoRegister(kakaoregister(id, profile, classes, nickname)).enqueue(object: Callback<RegisterResult> {
                override fun onResponse(
                    call: Call<RegisterResult>,
                    response: Response<RegisterResult>
                ) {
                    val response: RegisterResult = response.body() ?: return
                    if(response.message == true){
                        Toast.makeText(applicationContext, "계정 생성 완료", Toast.LENGTH_SHORT).show()
                        MyApplication.prefs.setString("nickname", nickname)
                        MyApplication.prefs.setString("id", id)
                        MyApplication.prefs.setString("profile", profile)
                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                    Log.d("testt",t.message.toString())
                }
            })

            alertDialog.dismiss()
        }

        alertDialog.show()
    }
