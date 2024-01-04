package com.example.madcamp_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView
    lateinit var changePageButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        changePageButton = findViewById(R.id.changePageButton)

        // WebView 설정
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://todaycode.tistory.com/23") // 초기 웹 페이지 설정

        // 버튼 클릭 이벤트 처리
        changePageButton.setOnClickListener {
            // 웹 페이지 변경
            webView.loadUrl("https://www.notion.so/madcamp/b4629df474414d2189a74bd25ca542b5")
            webView.visibility = View.VISIBLE
        }
    }
}