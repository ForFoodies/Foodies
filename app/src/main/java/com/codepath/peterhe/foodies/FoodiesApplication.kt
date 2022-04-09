package com.codepath.peterhe.foodies

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject
import com.parse.facebook.ParseFacebookUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class FoodiesApplication : Application() {

    // TODO: hide api keys in secrets.xml or apikey.properties

    override fun onCreate() {
        super.onCreate()
        // Use for monitoring Parse network traffic
        val builder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor)
        // Register your parse models
        ParseObject.registerSubclass(Group::class.java)
        ParseObject.registerSubclass(Message::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
        // ParseFace
        ParseFacebookUtils.initialize(this);


    }
}