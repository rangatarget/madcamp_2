package com.example.madcamp_2

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Part

interface RetroInterface{
    @POST("/register")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun register(
        @Body jsonparams: RegisterModel
    ) : Call<RegisterResult>

    @POST("/login") //url 받음
    fun login(
        @Body jsonparams: LoginModel
    ) : Call<LoginResult>

    @POST("/login/idcert")
    fun idCert(
        @Body jsonparams: IdCertification
    ) : Call<IdCertificationResult>

    @POST("/boardclass")
    fun getBoardClass(
        @Body jsonparams: getboardclass
    ) : Call<ArrayList<BoardClassModel>>

    @POST("/boardclass/create")
    fun createBoardClass(
        @Body jsonparams: Createboardclass
    ) : Call<CreateboardclassResponse>

    @POST("/board")
    fun getBoard(
        @Body jsonparams: BoardName
    ) : Call<ArrayList<BoardModel>>

    @POST("/checkedboardclass")
    fun getCheckedBoardClass(
        @Body jsonparams: Checkedboardclass
    ) : Call<ArrayList<BoardClassModel>>

    @POST("/getcomments") //url 받음
    fun getComments(
        @Body jsonparams: getcomment
    ) : Call<ArrayList<Comment>>

    @POST("/kakaologin")
    fun kakaoLogin(
        @Body jsonparams: kakaoExist
    ) : Call<kakaologinreturn>

    @POST("/kakaoregister")
    fun kakaoRegister(
        @Body jsonparams: kakaoregister
    ) : Call<RegisterResult>

    @POST("/createboard")
    fun createBoard(
        @Body jsonparams: boardcreate
    ) : Call<RegisterResult>

    @POST("/createcomment")
    fun createComment(
        @Body jsonparams: commentcreate
    ) : Call<RegisterResult>

    @POST("/deletecomment")
    fun deleteComment(
        @Body jsonparams: deletecomment
    ) : Call<RegisterResult>

    @POST("/updatecomment")
    fun updateComment(
        @Body jsonparams: updatecomment
    ) : Call<RegisterResult>

    @POST("/deletepost")
    fun deletePost(
        @Body jsonparams: deletepost
    ) : Call<RegisterResult>

    @POST("/updatepost")
    fun updatePost(
        @Body jsonparams: update_post
    ) : Call<RegisterResult>

    @POST("/myboardclass")
    fun getMyBoardClass(
        @Body jsonparams: getmy
    ) : Call<ArrayList<BoardClassModel>>

    @POST("/deleteboardclass")
    fun deleteBoardClass(
        @Body jsonparams: deleteboardclass
    ) : Call<RegisterResult>

    @POST("/pinboardclass")
    fun pinBoardClass(
        @Body jsonparams: pinboardclass
    ) : Call<RegisterResult>

    @POST("/changepassword")
    fun changePassword(
        @Body jsonparams: changepassword
    ) : Call<RegisterResult>

    @POST("/changeprofile")
    fun uploadFile(@Part file: MultipartBody.Part, @Part("id") id: RequestBody): Call<UrlResult>

    @POST("/signout")
    fun signOut(
        @Body jsonparams: signout
    ) : Call<RegisterResult>

    @POST("/myboard")
    fun getMyBoard(
        @Body jsonparams: getmy
    ) : Call<ArrayList<BoardModel>>

    @POST("/mycomment")
    fun getMyComment(
        @Body jsonparams: getmy
    ) : Call<ArrayList<Comment>>

    @POST("/editnickname")
    fun editNickName(
        @Body jsonparams: editnick
    ) : Call<RegisterResult>

    @POST("/getauthorimage")
    fun getAuthorImage(
        @Body jsonparams: giveuserandpost
    ) : Call<imageandisrecommend>

    @POST("/getrecommend")
    fun getRecommend(
        @Body jsonparams: giveidnum
    ) : Call<onlynumber>

    @POST("/recommendpost")
    fun isRecommended(
        @Body jsonparams: giveforrecommend
    ) : Call<RegisterResult>

    @POST("/search/blog")
    fun getBlog(
        @Body jsonparams: SearchModel
    ) : Call<ArrayList<BlogModel>>

    @POST("/rankingpost")
    fun getTop5(
        @Body jsonparams: getmy
    ) : Call<ArrayList<BoardModel>>


    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "http://192.249.29.79:4000" //

        fun create(): RetroInterface {
            val gson : Gson =   GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetroInterface::class.java)
        }
    }
}