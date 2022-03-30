package com.codepath.peterhe.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.orange));
        getSupportActionBar()?.hide()
        findViewById<ImageButton>(R.id.redirect_signin).setOnClickListener{
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.redirect_signup).setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}