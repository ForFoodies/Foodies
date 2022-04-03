package com.codepath.peterhe.foodies

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject

class FoodiesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Register your parse models
        ParseObject.registerSubclass(Group::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}