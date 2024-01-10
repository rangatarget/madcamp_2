package com.example.madcamp_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_2.databinding.ActivityBoardclassBinding
import com.example.madcamp_2.databinding.ActivitySearchBlogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchBlog : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBlogBinding
    val api = RetroInterface.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBlogBinding.inflate(layoutInflater)
        val receivedIntent = intent
        val keyword = receivedIntent.getStringExtra("searchkeyword").toString()
        binding.searchedit.setText(keyword)
        setContentView(binding.root)

        api.getBlog(SearchModel(keyword)).enqueue(object:
            Callback<ArrayList<BlogModel>> {
            override fun onResponse(
                call: Call<ArrayList<BlogModel>>,
                response: Response<ArrayList<BlogModel>>
            ) {
                val blogList: ArrayList<BlogModel> = response.body() ?: return
                for (blog in blogList) {
                    Log.d("board가져오기", "blog: $blog")
                }
                val layoutManager = LinearLayoutManager(this@SearchBlog)
                binding.rcvBlog.layoutManager = layoutManager
                val adapter = BlogAdapter(this@SearchBlog, blogList)
                binding.rcvBlog.adapter = adapter
            }

            override fun onFailure(call: Call<ArrayList<BlogModel>>, t: Throwable) {
                Log.d("testt",t.message.toString())
            }
        })
        binding.search.setOnClickListener {
            val searchkeyword = binding.searchedit.text.toString()
            val intent = Intent(this, SearchBlog::class.java)
            intent.putExtra("searchkeyword", searchkeyword)
            startActivity(intent)
            finish()
        }
        binding.arrowback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}