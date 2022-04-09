package com.codepath.peterhe.foodies.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.peterhe.foodies.*
import com.codepath.peterhe.foodies.R
import com.parse.*
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class GroupChatListFragment : Fragment() {
    private lateinit var restaurant: YelpRestaurant
    private lateinit var groupAdapter: GroupAdapter
    // Store a member variable for the listener
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    var allGroups: ArrayList<Group> = arrayListOf()
    lateinit var groupsRecyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).setSupportActionBar(view?.findViewById(R.id.my_toolbar))
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_group_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        groupsRecyclerView = view.findViewById(R.id.rvGroupChats)
        groupAdapter = GroupAdapter(requireContext(),allGroups,true)
        groupsRecyclerView.adapter = groupAdapter
        layoutManager = LinearLayoutManager(requireContext())
        groupsRecyclerView.layoutManager = layoutManager
        groupsRecyclerView.itemAnimator = SlideInUpAnimator()

        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        groupsRecyclerView.addItemDecoration(itemDecoration)
        queryGroups()
        val ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        groupAdapter.setOnItemClickListner(object: GroupAdapter.onItemClickListner{
            override fun onItemClick(position: Int) {
                //final FragmentTransaction ft = getFragmentManager().beginTransaction();
                val bundle = Bundle()
                bundle.putParcelable("GroupChatDetail", allGroups[position])
                val DetailFragment = GroupChatDetailFragment()
                DetailFragment.setArguments(bundle)
                //Log.i(RestaurantFragment.TAG, "Restaurant ${allGroups[position]}")
                ft?.replace(R.id.flContainer, DetailFragment)?.commit()
                requireActivity().actionBar?.title = "${allGroups[position].getName()} Chat"
                ft?.addToBackStack(null)
            }
        })

    }

    fun queryGroups() {
        var user = ParseUser.getCurrentUser()
        var grouplist = user.getJSONArray("groupList")
        groupAdapter.clear()
        if (grouplist != null) {
            for (i in 0 until grouplist!!.length()) {
                val groupId: String = grouplist?.getString(i)!!

                val query: ParseQuery<Group> = ParseQuery.getQuery(Group::class.java)
                query.include("objectId")
                //query.skip = offset * 20
                query.whereEqualTo("objectId", groupId)
                query.getFirstInBackground(object : GetCallback<Group> {
                    override fun done(group: Group?, e: ParseException?) {
                        if (e != null) {
                            //Log.e(TAG, "Error getting posts")
                                e.printStackTrace()
                            Toast.makeText(
                                requireContext(),
                                "Error getting groups",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (group != null) {
                                allGroups.add(group)
                                groupAdapter.notifyDataSetChanged()
                            }
                        }
                    }

                })
                requireActivity().setTitle("Group Chat (${grouplist.length()})")
            }
        } else {
            requireActivity().setTitle("Group Chat (0)")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_groupchat_list, menu)
        menu.findItem(R.id.action_map_Group).setOnMenuItemClickListener { item ->
            val bundle = Bundle()
            bundle.putParcelableArrayList("GroupListMap", allGroups)
            val DetailFragment = RestaurantListMapsFragment()
            DetailFragment.setArguments(bundle)
            val ft:FragmentTransaction? = getFragmentManager()?.beginTransaction()
            ft?.replace(R.id.flContainer, DetailFragment)?.commit()
            ft?.addToBackStack(null)
            true
        }
    }

}