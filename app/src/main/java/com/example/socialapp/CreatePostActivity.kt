package com.example.socialapp

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.socialapp.Dao.PostDao
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {
    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        supportActionBar?.hide()
        postDao=PostDao()
        postbotton.setOnClickListener()
        {
           val input=postinput.text.toString().trim()
            if(input.isNotEmpty())
            {
               postDao.addpost(input)
                finish()
            }
        }
    }
}