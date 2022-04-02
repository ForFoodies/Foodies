package com.codepath.peterhe.foodies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.codepath.peterhe.foodies.R
import com.codepath.peterhe.foodies.YelpRestaurant

class DialogueFragment : Fragment() {
    private lateinit var restaurant: YelpRestaurant
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialogue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTitle("Create a new Group")
        view.setBackgroundColor(getResources().getColor(R.color.orange))
        val bundle = this.arguments
        if(bundle != null){
            restaurant = bundle.getParcelable<YelpRestaurant>("RestaurantDialog")!!
        }
        Glide.with(this).load(restaurant.imageUrl).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(10)
            )).into(view.findViewById<ImageView>(R.id.ivDialogRestaurantBackground))
        view.findViewById<TextView>(R.id.tvDialogRestaurantName).text = restaurant.name
        val address = "${restaurant.location.address1}, ${restaurant.location.city}, ${restaurant.location.state}, ${restaurant.location.country}, ${restaurant.location.zip_code}"
        view.findViewById<TextView>(R.id.tvDialogAddress).text = address

        view.findViewById<ImageButton>(R.id.btn_cancel_dialog).setOnClickListener {
            val fm = getFragmentManager()
            fm?.popBackStack()
        }

    }


}