package com.example.madcamp_2

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.v2.all.BuildConfig

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