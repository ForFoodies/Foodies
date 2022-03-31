package com.codepath.peterhe.foodies

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.orange)))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setIcon(R.drawable.foodies_logo)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.foodies_logo);// set drawable icon
        //getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)
        getSupportActionBar()?.setTitle("");
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.orange))
        //getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.orange));

    }
}