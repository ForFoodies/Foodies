package com.codepath.peterhe.foodies

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.peterhe.foodies.fragments.GroupChatListFragment
import com.codepath.peterhe.foodies.fragments.RestaurantFragment
import com.codepath.peterhe.foodies.fragments.UserProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark
        window.statusBarColor = resources.getColor(R.color.primary_dark)

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
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.light_gray_1));
//        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        //appbar = findViewById(R.id.appbar)
       // setSupportActionBar(appbar)
        val fragmentManager: FragmentManager = supportFragmentManager
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setBackgroundColor(getResources().getColor(R.color.primary))
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {item ->
            var fragmentToShow: Fragment? = null
            when (item.itemId) {
                R.id.action_home -> {
                    this.supportActionBar?.title = "Discover"
                    fragmentToShow = RestaurantFragment()
                }
                R.id.action_chats -> {
                    this.supportActionBar?.title = "Chat"
                    fragmentToShow = GroupChatListFragment()
                }
                R.id.action_profile -> {
                    this.supportActionBar?.title = "Profile"
                    fragmentToShow = UserProfileFragment()
                }
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