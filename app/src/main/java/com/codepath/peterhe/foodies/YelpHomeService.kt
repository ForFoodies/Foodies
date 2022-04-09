package com.codepath.peterhe.foodies

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

public interface YelpHomeService {
    @GET("businesses/search")
    fun searchRestaurants(
        @Header("Authorization") authHeader: String,
        //@Query("term") searchTerm:String,
        @Query("location") location: String,
        @Query("offset") offset: Int
        // @Query("latitude") latitude:Double,
        //@Query("longitude") longitude:Double
    ): Call<YelpSearchResult>

}