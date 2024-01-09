package com.example.madcamp_2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.madcamp_2.databinding.ActivityMainBinding
import com.example.madcamp_2.databinding.ActivityProfileSettingBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

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
        val user_class = MyApplication.prefs.getString("class", "")

        binding.nickname.setText("닉네임 : " + nickname)
        binding.userid.setText(user_id)
        binding.userclassview.setText(user_class)
        if(profile != ""){
            val profile_bitmap = decodeBase64ToImage(profile)
            Glide.with(this@ProfileSetting).load(profile_bitmap).circleCrop().into(binding.profile)
        }

        binding.userpassword.setOnClickListener{
            Log.d("패스워드 변경 버튼 눌림", "")
            showDialog(user_id)
        }
        binding.profile.setOnClickListener{
            Log.d("프로필 사진 변경 버튼 눌림", "")
            requestPermission_Gallery()
        }
        binding.MyBoardClass.setOnClickListener{
            Log.d("내가 만든 게시판 목록 버튼 눌림", "")
            val intent = Intent(this, MyBoardClass::class.java)
            startActivity(intent)
            finish()
        }
        binding.arrowback.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.logout.setOnClickListener{
            MyApplication.prefs.setString("id", "")
            MyApplication.prefs.setString("nickname", "")
            MyApplication.prefs.setString("profile", "")
            MyApplication.prefs.setString("class", "")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.signout.setOnClickListener{
            api.signOut(signout(user_id)).enqueue(object: Callback<RegisterResult> {
                override fun onResponse(
                    call: Call<RegisterResult>,
                    response: Response<RegisterResult>
                ) {
                    val response: RegisterResult = response.body() ?: return
                    if(response.message == true){
                        Toast.makeText(applicationContext, "계정 삭제 완료", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "계정 삭제 실패. ERROR", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                    Log.d("testt",t.message.toString())
                }
            })
            MyApplication.prefs.setString("id", "")
            MyApplication.prefs.setString("nickname", "")
            MyApplication.prefs.setString("profile", "")
            MyApplication.prefs.setString("class", "")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.MyPost.setOnClickListener{
            val intent = Intent(this, MyBoard::class.java)
            startActivity(intent)
            finish()
        }
        binding.MyComment.setOnClickListener{
            val intent = Intent(this, MyComment::class.java)
            startActivity(intent)
            finish()
        }
        binding.editIcon.setOnClickListener{
            editNickName(user_id)
        }
    }

    private fun showDialog(id: String) {
        // LayoutInflater를 사용하여 XML 레이아웃 파일을 View 객체로 변환
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.editpassword, null)

        // 다이얼로그 생성
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("비밀번호 변경")

        val alertDialog = alertDialogBuilder.create()

        // 다이얼로그 내의 버튼과 에디트 텍스트에 대한 처리
        val inputoldpw = dialogView.findViewById<EditText>(R.id.oldpassword)
        val inputnewpw = dialogView.findViewById<EditText>(R.id.newpassword)
        val inputnewpwcert = dialogView.findViewById<EditText>(R.id.newpasswordcert)
        val submit = dialogView.findViewById<Button>(R.id.editbutton)

        submit.setOnClickListener {
            val oldpw = inputoldpw.text.toString()
            val newpw = inputnewpw.text.toString()
            val newpwcert = inputnewpwcert.text.toString()

            if(oldpw == newpw){
                Toast.makeText(applicationContext, "기존과 같은 비밀번호", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(newpw != newpwcert){
                Toast.makeText(applicationContext, "새 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // TODO: 여기서 새로운 제목을 처리하거나 저장하는 로직을 구현
            api.changePassword(changepassword(id, oldpw, newpw)).enqueue(object: Callback<RegisterResult> {
                override fun onResponse(
                    call: Call<RegisterResult>,
                    response: Response<RegisterResult>
                ) {
                    val response: RegisterResult = response.body() ?: return
                    if(response.message == true){
                        Toast.makeText(applicationContext, "비밀번호 변경 완료", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "기존 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
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

    private fun editNickName(id: String) {
        // LayoutInflater를 사용하여 XML 레이아웃 파일을 View 객체로 변환
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.changenickname, null)

        // 다이얼로그 생성
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("닉네임 변경")

        val alertDialog = alertDialogBuilder.create()

        // 다이얼로그 내의 버튼과 에디트 텍스트에 대한 처리
        val newnickname = dialogView.findViewById<EditText>(R.id.newnickname)
        val editbutton = dialogView.findViewById<Button>(R.id.editbutton)

        editbutton.setOnClickListener {
            val newname = newnickname.text.toString()

            // TODO: 여기서 새로운 제목을 처리하거나 저장하는 로직을 구현
            api.editNickName(editnick(id, newname)).enqueue(object: Callback<RegisterResult> {
                override fun onResponse(
                    call: Call<RegisterResult>,
                    response: Response<RegisterResult>
                ) {
                    val response: RegisterResult = response.body() ?: return
                    if(response.message == true){
                        Toast.makeText(applicationContext, "닉네임 변경 완료", Toast.LENGTH_SHORT).show()
                        MyApplication.prefs.setString("nickname", newname)
                        val intent = Intent(this@ProfileSetting, ProfileSetting::class.java)
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

            // 다이얼로그를 닫음
            alertDialog.dismiss()
        }

        // 다이얼로그 표시
        alertDialog.show()
    }

    private fun requestPermission_Gallery() {
        // 권한이 부여되어 있는지 확인
        val readStoragePermission = ContextCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES")
        // 권한이 부여되어 있지 않다면 권한 요청
        if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
            Log.v("권한 요청 조건성립", "갤러리 읽기")
            ActivityCompat.requestPermissions(this, arrayOf<String>("android.permission.READ_MEDIA_IMAGES"), 1000)
        } else {
            // 이미 권한이 부여된 경우 작업 수행
            Log.v("권한 이미 있음", "갤러리 읽기")
            performActionWithPermissions_Gallery()
        }
    }

    private fun performActionWithPermissions_Gallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*" // 이미지 파일만 표시하도록 설정합니다.
        }
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                2000 -> { //gallery
                    val uri = data?.data
                    if (uri != null) {
                        val user_id = MyApplication.prefs.getString("id", "")
                        uploadFile(this, uri, "http://192.249.29.79:4000/changeprofile", user_id)
                    }
                }
            }
        }
    }

    fun uploadFile(context: Context, fileUri: Uri, serverUrl: String, id: String) {
        val client = OkHttpClient()

        context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
            val fileBytes = inputStream.readBytes()
            val fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), fileBytes)

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "profile.jpg", fileBody)
                .addFormDataPart("id", id) // id를 서버에 보내는 코드 추가
                .build()

            val request = Request.Builder()
                .url(serverUrl)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    e.printStackTrace() // 네트워크 에러 처리
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }

                    // 성공적인 응답 처리
                    val responseBody = response.body?.string()

                    // JSON 파싱
                    val jsonObject = JSONObject(responseBody)
                    val image = jsonObject.getString("image") // 서버에서 받은 url
                    Log.v("응답 결과는?제발", image)
                    MyApplication.prefs.setString("profile", image)
                    val intent = Intent(this@ProfileSetting, ProfileSetting::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        }
    }

}