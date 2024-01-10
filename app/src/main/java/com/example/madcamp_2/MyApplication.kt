package com.example.madcamp_2

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.kakao.sdk.common.KakaoSdk
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate(){
        super.onCreate()

        prefs = PreferenceUtil(applicationContext)
        KakaoSdk.init(this, "2ac18b5fbc5d1cbc02979d8c51265676")
    }
}

fun downloadFile(context: Context, serverUrl: String, fileName: String) {
    val client = OkHttpClient()

    val request = Request.Builder().url(serverUrl).build()

    client.newCall(request).enqueue(object: okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            e.printStackTrace() // 네트워크 에러 처리
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            // 받은 데이터를 파일로 저장
            val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fos.write(response.body?.bytes())
            fos.close()
        }
    })

}

fun decodeBase64ToImage(encodedImage: String): Bitmap? {
    // Base64 디코딩
    val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)

    // 디코딩된 바이트 배열을 비트맵으로 변환
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun reduceBitmapSize(originalBitmap: Bitmap, sampleSize: Int): Bitmap {
    val width = originalBitmap.width / sampleSize
    val height = originalBitmap.height / sampleSize

    return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
}

fun removeTags(input: String): String {
    return input.replace(Regex("<.*?>"), "")
}