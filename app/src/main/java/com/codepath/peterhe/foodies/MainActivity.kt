package com.codepath.peterhe.foodies

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.peterhe.foodies.fragments.RestaurantFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.orange)))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setIcon(R.drawable.foodies_logo)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.foodies_logo);// set drawable icon
        //getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);*/
       /* getSupportActionBar()?.setDisplayUseLogoEnabled(true)
        getSupportActionBar()?.setTitle("");*/
        //supportActionBar?.setDisplayShowHomeEnabled(true);
       // supportActionBar?.setLogo(R.drawable.foodies_logo);
        //supportActionBar?.setDisplayUseLogoEnabled(true);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.orange))
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.orange));
        //appbar = findViewById(R.id.appbar)
       // setSupportActionBar(appbar)
        val fragmentManager: FragmentManager = supportFragmentManager
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setBackgroundColor(getResources().getColor(R.color.orange))
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {item ->
            var fragmentToShow: Fragment? = null
            when (item.itemId) {
                R.id.action_home -> {
                    this.setTitle("Discover")
                    fragmentToShow = RestaurantFragment()
                }
                R.id.action_chats -> {}
                R.id.action_profile -> {}
                R.id.action_setting -> {}
            }
            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }
            true
        }
        // Set default selection
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       var inflator: MenuInflater = getMenuInflater()
        inflator.inflate(R.menu.appbar, menu)
        return true
    }*/
}