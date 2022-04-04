package com.codepath.peterhe.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.parse.ParseUser

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        //check if the user is already logged in
        if (ParseUser.getCurrentUser() != null) {
            gotoMainActivity()
        }
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.primary));
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

    private fun gotoMainActivity(){
        val intent = Intent(this@LogInActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}