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
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.codepath.peterhe.foodies.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.parse.ParseUser
import java.io.IOException
import java.util.*


class RestaurantListMapsFragment : Fragment() {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var restaurants: ArrayList<YelpRestaurant>? = null
    private var groups: ArrayList<Group>? = null
    private var members: ArrayList<ParseUser>? = null
    private var positions: ArrayList<LatLng> = arrayListOf()
    private var userLocation: LatLng? = null
    private lateinit var bitmap: Bitmap
    private lateinit var bitmapGroup: Bitmap

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        10,
                        10.toFloat(),
                        locationListener
                    )
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
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
           /* override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}*/
            override fun onLocationChanged(location: Location) {
                val userLocation = LatLng(location.getLatitude(), location.getLongitude())
                //googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15.toFloat()))

                val geocoder =
                    Geocoder(requireActivity().getApplicationContext(), Locale.getDefault())
                try {
                    val listAddresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        1
                    ) as List<Address>
                    if (listAddresses != null && listAddresses.size > 0) {
                        val result: Address = listAddresses.get(0)
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
                        Toast.makeText(requireContext(), address, Toast.LENGTH_SHORT).show()

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }

        }
        if (Build.VERSION.SDK_INT < 23) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10,
                    10.toFloat(),
                    locationListener
                )
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        10,
                        10.toFloat(),
                        locationListener
                    )
                    val lastKnownLocation =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    userLocation =
                        LatLng(lastKnownLocation?.getLatitude()!!, lastKnownLocation.getLongitude())
                    // googleMap.clear()
                    // googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))


                    /*for (restaurant in restaurants) {
                         // val restaurantLocation = LatLng(restaurant.region.center.latitude,restaurant.region.center.longitude)
                         val address = "${restaurant.location.address1}, ${restaurant.location.city}, ${restaurant.location.state}, ${restaurant.location.zip_code}"
                         val restaurantLocation: LatLng  = getLocationFromAddress(requireContext(), address)!!
                         googleMap.addMarker(MarkerOptions().position(restaurantLocation).title("${restaurant.name}").icon(BitmapDescriptorFactory.fromBitmap(bitmap!!)))
                        // googleMap.addMarker(MarkerOptions().position(restaurantLocation).title("${restaurant.name}").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                     }*/

                }
            }
        }

        if (restaurants != null && restaurants!!.size > 0) {
            googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(requireContext()))
            var restcount = 0
            for (restaurant in restaurants!!) {
                // val restaurantLocation = LatLng(restaurant.region.center.latitude,restaurant.region.center.longitude)
                Log.i("Map", restaurant.name)
                val address =
                    "${restaurant.location.address1}, ${restaurant.location.city}, ${restaurant.location.state}, ${restaurant.location.zip_code}"
                val restaurantLocation: LatLng = getLocationFromAddress(requireContext(), address)!!
                positions.add(restaurantLocation)
                //val snippet:String:
                if (restcount == 0) {
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            restaurantLocation,
                            15.toFloat()
                        )
                    )
                }
                restcount += 1
                val marker = googleMap.addMarker(
                    MarkerOptions().position(restaurantLocation).title("${restaurant.name}")
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap!!))
                )
                marker?.setTag(restaurant)
                // marker?.showInfoWindow()

            }
            val b = LatLngBounds.Builder()
            for (position in positions) {
                b.include(position)
            }
            val bounds = b.build()
            //Change the padding as per needed
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 20)
            googleMap.animateCamera(cu)
            googleMap.setOnInfoWindowClickListener(object : GoogleMap.OnInfoWindowClickListener {
                override fun onInfoWindowClick(marker: Marker) {
                    val ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
                    val restaurant = marker.getTag() as YelpRestaurant
                    val bundle = Bundle()
                    bundle.putParcelable("RestaurantDetail", restaurant)
                    val DetailFragment = RestaurantDetailFragment()
                    DetailFragment.setArguments(bundle)
                    Log.i(RestaurantFragment.TAG, "Restaurant ${restaurant}")
                    ft?.replace(R.id.flContainer, DetailFragment)?.commit()
                    requireActivity().setTitle("${restaurant.name}")
                    ft?.addToBackStack(null)
                }
            })
        }

        if (groups != null && groups!!.size > 0) {
            googleMap.setInfoWindowAdapter(CustomInfoWindowGroupAdapter(requireContext()))
            var groupcount = 0
            for (group in groups!!) {
                val groupLocation: LatLng =
                    getLocationFromAddress(requireContext(), group.getAddress())!!
                positions.add(groupLocation)
                if (groupcount == 0) {
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            groupLocation,
                            15.toFloat()
                        )
                    )
                }
                groupcount += 1
                val marker = googleMap.addMarker(
                    MarkerOptions().position(groupLocation).title("${group.getName()}")
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapGroup!!))
                )
                marker?.setTag(group)
            }
            val b = LatLngBounds.Builder()
            for (position in positions) {
                b.include(position)
            }
            val bounds = b.build()
            //Change the padding as per needed
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 20)
            googleMap.animateCamera(cu)
            googleMap.setOnInfoWindowClickListener(object : GoogleMap.OnInfoWindowClickListener {
                override fun onInfoWindowClick(marker: Marker) {
                    val ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
                    val bundle = Bundle()
                    val group = marker.getTag() as Group
                    bundle.putParcelable("GroupChatDetail", group)
                    val DetailFragment = GroupChatDetailFragment()
                    DetailFragment.setArguments(bundle)
                    //Log.i(RestaurantFragment.TAG, "Restaurant ${allGroups[position]}")
                    ft?.replace(R.id.flContainer, DetailFragment)?.commit()
                    requireActivity().setTitle("${group.getName()} Chat")
                    ft?.addToBackStack(null)
                }
            })


        }

        if (members != null && members!!.size > 0) {
            //googleMap.setInfoWindowAdapter(CustomInfoWindowGroupAdapter(requireContext()))
            googleMap.setInfoWindowAdapter(CustomInfoWindowMemberAdapter(requireContext()))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation!!, 15.toFloat()))
            // var mcount = 0
            for (member in members!!) {
                Log.i("Map", member.username)
                if (member.getBoolean("AllowSharing")) {
                    val memberLocation: LatLng = LatLng(
                        member.getParseGeoPoint("Location")?.getLatitude()!!,
                        member.getParseGeoPoint("Location")?.getLongitude()!!
                    )
                    val imageUrl = member.getParseFile("profile")?.url
                    positions.add(memberLocation)
                    Glide.with(requireContext()).asBitmap().load(imageUrl).override(160, 160).centerCrop().apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                        .diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).into(object : SimpleTarget<Bitmap>() {

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                Log.i("member", "Peter")
                                val bitmapMember = Bitmap.createScaledBitmap(resource!!, 160, 160, false)
                                val marker = googleMap.addMarker(
                                    MarkerOptions().position(memberLocation).title("${member.username}")
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapMember!!))
                                )
                                marker?.setTag(member)
                               /* googleMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        memberLocation!!,
                                        13.toFloat()
                                    )
                                )*/
                            }

                        })
                }

            }
            val b = LatLngBounds.Builder()
            for (p in positions) {
                b.include(p)
            }
            val bounds = b.build()
            //Change the padding as per needed
            val cu = CameraUpdateFactory.newLatLngBounds(bounds,  20)
            googleMap.animateCamera(cu)
            googleMap.setOnInfoWindowClickListener(object : GoogleMap.OnInfoWindowClickListener {
                override fun onInfoWindowClick(marker: Marker) {
                    val ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
                    val member = marker.getTag() as ParseUser
                    val bundle = Bundle()
                    bundle.putParcelable("MemberDetail",member)
                    val DetailFragment = UserProfileFragment()
                    DetailFragment.setArguments(bundle)
                    //Log.i(RestaurantFragment.TAG, "Restaurant ${allGroups[position]}")
                    ft?.replace(R.id.flContainer, DetailFragment)?.commit()
                    requireActivity().setTitle("Profile")
                    ft?.addToBackStack(null)
                }
            })
        }



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
        if (bundle != null) {
            restaurants = bundle.getParcelableArrayList<YelpRestaurant>("RestaurantList")
            if (restaurants != null && restaurants!!.size > 0) {
                val b = AppCompatResources.getDrawable(requireContext(), R.drawable.cutlery)
                    ?.toBitmap()!!
                bitmap = Bitmap.createScaledBitmap(b!!, 80, 80, false)
            }
            groups = bundle.getParcelableArrayList<Group>("GroupListMap")
            if (groups != null && groups!!.size > 0) {
                val b =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.heart)?.toBitmap()!!
                bitmapGroup = Bitmap.createScaledBitmap(b!!, 100, 100, false)
            }
            members = bundle.getParcelableArrayList<ParseUser>("MembersMap")
            /* if (members != null && members!!.size > 0) {
                for (member in members!!) {
                    Glide.with(requireContext()).load(member.getParseFile("profile")?.url).override(80, 80).apply(
                        RequestOptions().transforms(
                            CenterCrop(), RoundedCorners(10)
                        )
                    ).listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            e?.printStackTrace()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            memberProfiles.add(resource!!)
                            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                            mapFragment?.getMapAsync(callback)
                            return false;
                        }
                    })
                }
            }

        }*/
            //if (members == null) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
            //}
        }
    }

        fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
            val coder = Geocoder(context)
            val address: List<Address>?
            var p1: LatLng? = null
            try {
                address = coder.getFromLocationName(strAddress, 1)
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

    fun loadBitmapFromServer(url: String?, callback: OnBitmapLoadedListener) {
        try {
            Glide.with(requireContext()).asBitmap().load(url)
                .priority(Priority.IMMEDIATE)
                .listener(object : RequestListener<Bitmap?> {
                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        callback.onBitmapLoaded(resource)
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
        } catch (e: java.lang.Exception) {
        }
    }


}