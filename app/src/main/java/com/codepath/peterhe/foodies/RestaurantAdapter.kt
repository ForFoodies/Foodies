package com.codepath.peterhe.foodies

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions

class RestaurantAdapter(val context: Context, val restaurants: List<YelpRestaurant>) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {
    private lateinit var mlistner: onItemClickListner

    interface onItemClickListner {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(listner: onItemClickListner) {
        mlistner = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false), mlistner
        )
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListner) :
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(restaurant: YelpRestaurant) {
            Glide.with(context).load(restaurant.imageUrl).apply(
                RequestOptions().transforms(
                    CenterCrop()
                )
            ).into(itemView.findViewById<ImageView>(R.id.restaurantImage))
            var name = ""
            if (restaurant.name?.length > 22) {
                name = restaurant.name.substring(0, 19)
                name += "..."
            } else {
                name = restaurant.name
            }
            itemView.findViewById<TextView>(R.id.tvRestaurantName).text = name
            itemView.findViewById<RatingBar>(R.id.ratingBar).rating = restaurant.rating.toFloat()
            itemView.findViewById<TextView>(R.id.tvNumReviews).text =
                "${restaurant.numReviews} Reviews"
            val address =
                "${restaurant.location.address1}, ${restaurant.location.city}, ${restaurant.location.state}, ${restaurant.location.country}, ${restaurant.location.zip_code}"
            itemView.findViewById<TextView>(R.id.tvAddress).text = address
            var restaurantcategories = ""
            for ((index, value) in restaurant.categories.withIndex()) {
                if (index >= 2) {
                    break
                }
                restaurantcategories += "${value.title}, "
            }
            restaurantcategories = restaurantcategories.dropLast(2)
            itemView.findViewById<TextView>(R.id.tvCategory).text = restaurantcategories
            itemView.findViewById<TextView>(R.id.tvDistance).text = restaurant.displayDistance()
            itemView.findViewById<TextView>(R.id.tvPrice).text = restaurant.price

        }

    }

}
