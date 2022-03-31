package com.codepath.peterhe.foodies.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codepath.peterhe.foodies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "x-mlmzTYMP7jHxTcofpKqz7ou9C780dbLrxFON-B_wrZjIb02f5d3z1-yUQstCmyCOnmqR6pKKj3bq3BboVwU87P0BI_FO5sOth2PFLhlz9bmwwD8GWAqBKlNAxFYnYx"
class RestaurantFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //handle logic
        val restaurants = mutableListOf<YelpRestaurant>()
        val restaurantAdapter = RestaurantAdapter(this, restaurants)
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        val yelpHomeService = retrofit.create(YelpHomeService::class.java)
        yelpHomeService.searchRestaurants("Bearer $API_KEY","Avocado Toast", "New York")
            .enqueue(object : Callback<YelpSearchResult> {
                override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                    Log.i(TAG, "onResponse $response")
                }

                override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }
            })

    }
    companion object {
        const val TAG = "RestaurentFragment"
    }
}
