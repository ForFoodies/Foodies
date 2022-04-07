package com.codepath.peterhe.foodies.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.codepath.peterhe.foodies.R
import com.codepath.peterhe.foodies.YelpRestaurant
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


class RestaurantListMapsFragment : Fragment() {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var restaurants: ArrayList<YelpRestaurant>
    private lateinit var b : Bitmap
    private lateinit var bitmap : Bitmap

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5,5.toFloat(),locationListener )
                }
                }

        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        //locationManager = Parse.getApplicationContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val userLocation = LatLng(location.getLatitude(),location.getLongitude())
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15.toFloat()))

                for (restaurant in restaurants) {
                    // val restaurantLocation = LatLng(restaurant.region.center.latitude,restaurant.region.center.longitude)
                    Log.i("Map", restaurant.name)
                    val address = "${restaurant.location.address1}, ${restaurant.location.city}, ${restaurant.location.state}, ${restaurant.location.zip_code}"
                    val restaurantLocation: LatLng  = getLocationFromAddress(requireContext(), address)!!

                    googleMap.addMarker(MarkerOptions().position(restaurantLocation).title("${restaurant.name}").icon(BitmapDescriptorFactory.fromBitmap(bitmap!!)))
                }
                val geocoder = Geocoder(requireActivity().getApplicationContext(), Locale.getDefault())
                try{
                    val listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1) as List<Address>
                    if (listAddresses != null && listAddresses.size > 0) {
                        val result:Address = listAddresses.get(0)
                        var address: String = "Your location is at "
                        if (result.getSubThoroughfare() != null) {
                            address += result.getSubThoroughfare() + " "
                        }
                        if (result.getThoroughfare() != null) {
                            address += result.getThoroughfare() + ", "
                        }
                        if (result.getLocality() != null) {
                            address += result.getLocality() + ", "
                        }
                        if (result.getPostalCode() != null) {
                            address += result.getPostalCode() + ", "
                        }
                        if (result.getCountryName() != null) {
                            address += result.getCountryName()
                        }
                        Toast.makeText(requireContext(),address,Toast.LENGTH_SHORT).show()

                    }
                } catch(e: IOException) {
                    e.printStackTrace()
                }


            }

        }
        if (Build.VERSION.SDK_INT < 23) {
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10.toFloat(),locationListener )
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            } else {
                if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10.toFloat(),locationListener )
                    val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val userLocation = LatLng(lastKnownLocation?.getLatitude()!!,lastKnownLocation.getLongitude())
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15.toFloat()))

                    for (restaurant in restaurants) {
                        // val restaurantLocation = LatLng(restaurant.region.center.latitude,restaurant.region.center.longitude)
                        val address = "${restaurant.location.address1}, ${restaurant.location.city}, ${restaurant.location.state}, ${restaurant.location.zip_code}"
                        val restaurantLocation: LatLng  = getLocationFromAddress(requireContext(), address)!!
                        googleMap.addMarker(MarkerOptions().position(restaurantLocation).title("${restaurant.name}").icon(BitmapDescriptorFactory.fromBitmap(bitmap!!)))
                       // googleMap.addMarker(MarkerOptions().position(restaurantLocation).title("${restaurant.name}").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                    }

                }
            }
        }
       /* val umich = LatLng(42.2780436,-83.7404128)
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID)
        googleMap.addMarker(MarkerOptions().position(umich).title("University of Michigan").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(umich,13.toFloat()))*/



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_list_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if(bundle != null){
            restaurants = bundle.getParcelableArrayList<YelpRestaurant>("RestaurantList")!!
        }
        b = AppCompatResources.getDrawable(requireContext(), R.drawable.cutlery)?.toBitmap()!!
        bitmap = Bitmap.createScaledBitmap(b!!, 80, 80, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location = address[0]
            location.latitude
            location.longitude
            p1 = LatLng(location.latitude, location.longitude)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return p1
    }
}