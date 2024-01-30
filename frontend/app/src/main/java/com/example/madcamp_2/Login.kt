package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
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

        binding.buttonsignup.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        binding.kakaologin.setOnClickListener {
            kakaologin()
        }

        binding.buttonlogin.setOnClickListener {
            binding.apply {
                val id = inputid.text.toString()
                val pw = inputpassword.text.toString()

                if (id == "" || pw == "") {
                    Toast.makeText(applicationContext, "입력하지 않은 정보가 있습니다.", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
            }

            val loginUser = LoginModel(binding.inputid.text.toString(), binding.inputpassword.text.toString())
            api.login(loginUser).enqueue(object : Callback<LoginResult> {
                override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                    val user = response.body() ?: return
                    val user_id = user.id
                    val user_nickname = user.nickname
                    val user_profile = user.image
                    val user_class = user.classes
                    if (user_id != "") {
                        Toast.makeText(
                            applicationContext,
                            user_nickname + "로그인 성공",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("로그인 성공", "user_nickname : " + user_nickname + "  profile_url : ")
                        MyApplication.prefs.setString("id", user_id)
                        MyApplication.prefs.setString("nickname", user_nickname)
                        if (user_profile != null) MyApplication.prefs.setString("profile", user_profile)
                        MyApplication.prefs.setString("class", user_class)
                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "로그인 실패, 아이디 또는 비밀번호를 확인해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                    Log.d("testt", t.message.toString())
                }
            })
        }
    }

    private fun kakaologin() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e("카카오", "로그인 실패", error)
            } else if (token != null) {
                Log.i("카카오", "로그인 성공 ${token.accessToken}")
                UserApiClient.instance.me { user, getError ->
                    if (getError != null) {
                        Log.d("loginkakao", "fail error")
                    } else if (user != null) {
                        val userId = user.id.toString()
                        Log.d("유저의 아이디", userId)
                        val nickname = user.kakaoAccount?.profile?.nickname.toString()
                        val profile = user.kakaoAccount?.profile?.profileImageUrl.toString()
                        Log.d("loginkakao", nickname + ", " + userId + ", " + profile)
                        api.kakaoLogin(kakaoExist(userId))
                            .enqueue(object : Callback<kakaologinreturn> {
                                override fun onResponse(
                                    call: Call<kakaologinreturn>,
                                    response: Response<kakaologinreturn>
                                ) {
                                    val user_info = response.body()?: return
                                    if (user_info.id != "") {
                                        Toast.makeText(
                                            applicationContext,
                                            nickname + "님 환영합니다",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.d("카카오 로그인 성공", nickname)
                                        MyApplication.prefs.setString("nickname", user_info.nickname)
                                        MyApplication.prefs.setString("id", user_info.id)
                                        MyApplication.prefs.setString("profile", user_info.image)
                                        MyApplication.prefs.setString("class", user_info.classes)
                                        val intent = Intent(this@Login, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "회원가입 필요함",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.d("카카오 회원가입 필요", nickname)
                                        showDialog(userId, profile, nickname)
                                    }
                                }

                                override fun onFailure(call: Call<kakaologinreturn>, t: Throwable) {
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
            api.kakaoRegister(kakaoregister(id, profile, classes, nickname))
                .enqueue(object : Callback<RegisterResult> {
                    override fun onResponse(
                        call: Call<RegisterResult>,
                        response: Response<RegisterResult>
                    ) {
                        val response: RegisterResult = response.body() ?: return
                        if (response.message == true) {
                            Toast.makeText(applicationContext, "계정 생성 완료", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                        Log.d("testt", t.message.toString())
                    }
                })

            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}