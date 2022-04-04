package com.codepath.peterhe.foodies.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.codepath.peterhe.foodies.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import java.lang.Boolean


class RestaurantDetailFragment : Fragment() {
    private lateinit var restaurant: YelpRestaurant
    private lateinit var groupAdapter: GroupAdapter
    // Store a member variable for the listener
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    var allGroups: MutableList<Group> = mutableListOf()
    lateinit var groupsRecyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(getResources().getColor(R.color.white))
        val bundle = this.arguments
        if(bundle != null){
            restaurant = bundle.getParcelable<YelpRestaurant>("RestaurantDetail")!!
        }
        requireActivity().setTitle("${restaurant.name}")
        Glide.with(this).load(restaurant.imageUrl).apply(
            RequestOptions().transforms(
            CenterCrop(), RoundedCorners(10)
        )).into(view.findViewById<ImageView>(R.id.ivRestaurantBackground))
        view.findViewById<TextView>(R.id.tvRestaurantDetailName).text = restaurant.name
        view.findViewById<TextView>(R.id.tvRestaurantDetailRating).text = restaurant.rating.toString()
        view.findViewById<RatingBar>(R.id.RestaurantDetailRatingBar).rating = restaurant.rating.toFloat()
        view.findViewById<TextView>(R.id.tvNumReviewsDetail).text = "${restaurant.numReviews} Reviews"
        var restaurantcategories = ""
        for ((index,value) in restaurant.categories.withIndex()) {
            if (index >= 2) {
                break
            }
            restaurantcategories +="${value.title}, "
        }
        restaurantcategories = restaurantcategories.dropLast(2)
        view.findViewById<TextView>(R.id.tvCategoryDetail).text = restaurantcategories
        view.findViewById<TextView>(R.id.tvDistanceDetail).text = restaurant.displayDistance()
        view.findViewById<TextView>(R.id.tvPriceDetail).text = restaurant.price
        view.findViewById<TextView>(R.id.tvPhone).text = restaurant.phone
        view.findViewById<TextView>(R.id.tvUrl).text = restaurant.url
        val address = "${restaurant.location.address1}, ${restaurant.location.city}, ${restaurant.location.state}, ${restaurant.location.country}, ${restaurant.location.zip_code}"
        view.findViewById<TextView>(R.id.tvAddressDetail).text = address
        if (restaurant.is_closed == true) {
            view.findViewById<TextView>(R.id.tvIsClosed).text = "Closed"
            view.findViewById<TextView>(R.id.tvIsClosed).setTextColor(getResources().getColor(R.color.red))
            view.findViewById<TextView>(R.id.tvIsClosed).setCompoundDrawables(getResources().getDrawable(R.drawable.ic_baseline_close_24),null,null,null)
        } else {
            view.findViewById<TextView>(R.id.tvIsClosed).text = "Open"
            view.findViewById<TextView>(R.id.tvIsClosed).setTextColor(getResources().getColor(R.color.green))
            view.findViewById<TextView>(R.id.tvIsClosed).setCompoundDrawables(getResources().getDrawable(R.drawable.ic_baseline_check_24),null,null,null)
        }
        var transactions = ""
        for (item in restaurant.transactions) {
            transactions += "$item, "
        }
        transactions = transactions.dropLast(2)
        view.findViewById<TextView>(R.id.tvTransactions).text = transactions
        //view.findViewById<TextView>(R.id.tvTransactions).setTextColor(getResources().getColor(R.color.green))
        view.findViewById<ImageButton>(R.id.btn_startGroup).setOnClickListener {
            val ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
            showDialog(ft!!)
        }
        groupsRecyclerView = view.findViewById(R.id.rvGroups)
        groupAdapter = GroupAdapter(requireContext(),allGroups)
        groupsRecyclerView.adapter = groupAdapter
        layoutManager = LinearLayoutManager(requireContext())
        groupsRecyclerView.layoutManager = layoutManager
        groupsRecyclerView.itemAnimator = SlideInUpAnimator()
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        groupsRecyclerView.addItemDecoration(itemDecoration)
        queryGroups()
        val ft:FragmentTransaction? = getFragmentManager()?.beginTransaction()
        groupAdapter.setOnItemClickListner(object: GroupAdapter.onItemClickListner{
            override fun onItemClick(position: Int) {
                //final FragmentTransaction ft = getFragmentManager().beginTransaction();
                val bundle = Bundle()
                bundle.putParcelable("GroupDetail", allGroups[position])
                val DetailFragment = GroupDetailFragment()
                DetailFragment.setArguments(bundle)
                //Log.i(RestaurantFragment.TAG, "Restaurant ${allGroups[position]}")
                ft?.replace(R.id.flContainer, DetailFragment)?.commit()
                requireActivity().setTitle("${allGroups[position].getName()}")
                ft?.addToBackStack(null)
            }
        })
    }

    private fun showDialog(ft:FragmentTransaction) {
        val bundle = Bundle()
        bundle.putParcelable("RestaurantDialog", restaurant)
        val dialogFragment = DialogueFragment()
        dialogFragment.setArguments(bundle)
        ft.replace(R.id.flContainer, dialogFragment).commit()
        ft.addToBackStack(null)
    }

    fun queryGroups() {
        val query: ParseQuery<Group> = ParseQuery.getQuery(Group::class.java)
        query.include(Group.RESTAURANT_KEY)
        query.addDescendingOrder("createdAt")
        query.limit = 20
        //query.skip = offset * 20
        query.whereEqualTo(Group.RESTAURANT_KEY, restaurant.id)
        query.whereEqualTo(Group.FULL_KEY,false)
        query.findInBackground(object : FindCallback<Group> {
            override fun done(groups: MutableList<Group>?, e: ParseException?) {
                if (e != null) {
                    //Log.e(TAG, "Error getting posts")
                    Toast.makeText(requireContext(), "Error getting groups", Toast.LENGTH_SHORT).show()
                } else {
                    if (groups != null) {
                        groupAdapter.clear()
                        allGroups.addAll(groups)
                        groupAdapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }
    /*override fun onBackPressed() {
        val fromNewActivity = true
        val mainIntent = Intent(view!!.context, MainActivity::class.java)
        val bundleObj = Bundle()
        bundleObj.putString("fromNewActivity", Boolean.toString(fromNewActivity))
        mainIntent.putExtras(bundleObj)
        startActivityForResult(mainIntent, 0)
    }*/

}