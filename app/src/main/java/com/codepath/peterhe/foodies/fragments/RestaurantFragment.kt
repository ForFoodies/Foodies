package com.codepath.peterhe.foodies.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.peterhe.foodies.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.parse.Parse.getApplicationContext
import java.lang.Exception
import java.util.*


private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "x-mlmzTYMP7jHxTcofpKqz7ou9C780dbLrxFON-B_wrZjIb02f5d3z1-yUQstCmyCOnmqR6pKKj3bq3BboVwU87P0BI_FO5sOth2PFLhlz9bmwwD8GWAqBKlNAxFYnYx"
class RestaurantFragment : Fragment(),LocationListener {
    // declare a global variable of FusedLocationProviderClient
    //private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var address : String
    private lateinit var restaurants : MutableList<YelpRestaurant>
    private lateinit var restaurantAdapter : RestaurantAdapter
    private lateinit var yelpHomeService : YelpHomeService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        //handle logic
        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
        }
        restaurants = mutableListOf<YelpRestaurant>()
        restaurantAdapter = RestaurantAdapter(requireContext(), restaurants)
        view.findViewById<RecyclerView>(R.id.rvRestaurants).adapter = restaurantAdapter
        view.findViewById<RecyclerView>(R.id.rvRestaurants).layoutManager = LinearLayoutManager(requireContext())

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        yelpHomeService = retrofit.create(YelpHomeService::class.java)
        getLocation()
       /* yelpHomeService.searchRestaurants("Bearer $API_KEY", address)
            .enqueue(object : Callback<YelpSearchResult> {
                override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG,"Did not receive valid response body.")
                        return
                    }
                    restaurants.addAll(body.restaurants)
                    restaurantAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }
            })*/

    }
    /**
     * call this method for receive location
     * get location and give callback when successfully retrieve
     * function itself check location permission before access related methods
     *
     */
    /*fun getLastKnownLocation(yelpHomeService: YelpHomeService) : Array<Double> {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                   // return [location.longitude,location.latitude]

                }

            }
    }*/
    /*fun getLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }*/
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        try {
            locationManager = getApplicationContext().getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,5.toFloat(), this)
        } catch (e:Exception) {
            e.printStackTrace()
        }

    }
    companion object {
        const val TAG = "RestaurentFragment"
        //private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    override fun onLocationChanged(location: Location) {
        try {
            var geocoder:Geocoder = Geocoder(requireContext(), Locale.getDefault())
            var addresses : List<Address> = geocoder.getFromLocation(location.latitude,location.longitude,1)
            address = addresses.get(0).getAddressLine(0)
            Log.i(TAG,address)
            yelpHomeService.searchRestaurants("Bearer $API_KEY", address)
                .enqueue(object : Callback<YelpSearchResult> {
                    override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                        Log.i(TAG, "onResponse $response")
                        val body = response.body()
                        if (body == null) {
                            Log.w(TAG,"Did not receive valid response body.")
                            return
                        }
                        restaurants.addAll(body.restaurants)
                        restaurantAdapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                        Log.i(TAG, "onFailure $t")
                    }
                })
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
}
