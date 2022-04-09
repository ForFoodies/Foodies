package com.codepath.peterhe.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.google.android.material.button.MaterialButton
import com.parse.ParseUser

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        //check if the user is already logged in
        if (ParseUser.getCurrentUser() != null) {
            gotoMainActivity()
        }
        // transparent status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        getSupportActionBar()?.hide()

        findViewById<MaterialButton>(R.id.redirect_signin).setOnClickListener{
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
        findViewById<MaterialButton>(R.id.redirect_signup).setOnClickListener{
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