package com.codepath.peterhe.foodies

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.peterhe.foodies.fragments.GroupDetailFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.codepath.peterhe.foodies.fragments.RestaurantListMapsFragment
import com.parse.*
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling


class GroupChatDetailFragment : Fragment() {
    val USER_ID_KEY = "userId"
    val BODY_KEY = "body"
    private lateinit var group: Group
    var allMembers: ArrayList<ParseUser> = arrayListOf()

    var etMessage: EditText? = null
    var ibSend: ImageButton? = null
    private lateinit var rvChat:RecyclerView
    private var mMessages:MutableList<Message> = mutableListOf()
    private var mFirstLoad:Boolean = true
    private lateinit var mAdapter:ChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if(bundle != null){
            group = bundle.getParcelable<Group>("GroupChatDetail")!!
        }
        //requireActivity().setTitle("${group.getName()} Chat")
        requireActivity().actionBar?.title = "${group.getName()} Chat"

        refreshMessages()
        // Enter the websocket URL of your Parse server
        //val websocketUrl = "wss://PASTE_SERVER_WEBSOCKET_URL_HERE"
        val parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()
        val parseQuery = ParseQuery.getQuery(Message::class.java)
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)

        // Connect to Parse server
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId())
        parseQuery.whereEqualTo("groupId", group.objectId)
        // Connect to Parse server
        val subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery)

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { query: ParseQuery<Message>?, `object`: Message? ->
            mMessages.add(
                0,
                `object`!!
            )

            // RecyclerView updates need to be run on the UI thread
            requireActivity().runOnUiThread(object: Runnable {
                override fun run() {
                    mAdapter.notifyDataSetChanged();
                    rvChat.scrollToPosition(0);
                }
            })
        }



        startWithCurrentUser()
        setHasOptionsMenu(true)
    }

    // Get the userId from the cached currentUser object
    fun startWithCurrentUser() {
        setupMessagePosting()
    }

    // Set up button event handler which posts the entered message to Parse
    fun setupMessagePosting() {
        // Find the text field and button
        etMessage = view?.findViewById<View>(R.id.etMessage) as EditText
        ibSend = view?.findViewById<View>(R.id.ibSend) as ImageButton
        rvChat = view?.findViewById(R.id.rvChat) as RecyclerView
       // mMessages = MutableListof()
        mFirstLoad = true
        val userId = ParseUser.getCurrentUser().objectId
        mAdapter = ChatAdapter(requireContext(), userId, mMessages)
        rvChat.setAdapter(mAdapter)


        // associate the LayoutManager with the RecylcerView
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.setReverseLayout(true)
        rvChat.setLayoutManager(linearLayoutManager)

        // When send button is clicked, create message object on Parse
        ibSend?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val data: String = etMessage?.getText().toString()
                val message = Message()
                message.userId = userId
                message.body = data
                message.groupId = group.objectId

                message.saveInBackground(object : SaveCallback {
                    override fun done(e: ParseException?) {
                        if (e == null) {
                            Toast.makeText(
                                requireContext(), "Successfully created message on Parse",
                                Toast.LENGTH_SHORT
                            ).show()
                            refreshMessages()
                        } else {
                            Log.e(TAG, "Failed to save message", e)
                        }
                    }
                })
                etMessage?.setText(null)
            }
        })
    }
    // Query messages from Parse so we can load them into the chat adapter
    private fun refreshMessages() {
        // Construct query to execute
        var query: ParseQuery<Message> = ParseQuery.getQuery(Message::class.java)
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW)
        //query.whereEqualTo()
        query.whereEqualTo("groupId", group.objectId)

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt")
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(object: FindCallback<Message> {
           override fun done(messages:MutableList<Message>, e:ParseException?) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_groupchat_detail, menu)
        menu.findItem(R.id.action_map_members).setEnabled(false)
        queryMembers(menu)
        menu.findItem(R.id.action_GroupDetail).setOnMenuItemClickListener { item ->
            val bundle = Bundle()
            bundle.putParcelable("GroupDetail", group)
            val DetailFragment = GroupDetailFragment()
            DetailFragment.setArguments(bundle)
            val ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
            //Log.i(RestaurantFragment.TAG, "Restaurant ${allGroups[position]}")
            ft?.replace(R.id.flContainer, DetailFragment)?.commit()
           // requireActivity().setTitle("${group.getName()}")
            requireActivity().actionBar?.title = "${group.getName()}"
            ft?.addToBackStack(null)
            //Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()
            true
        }
        menu.findItem(R.id.action_map_members).setOnMenuItemClickListener {item ->
            val bundle = Bundle()
            // val sublist:ArrayList<YelpRestaurant> = arrayListOf()
            //sublist.addAll(restaurants.subList(0,10))

            bundle.putParcelableArrayList("MembersMap", allMembers)
            val DetailFragment = RestaurantListMapsFragment()
            DetailFragment.setArguments(bundle)
            val ft:FragmentTransaction? = getFragmentManager()?.beginTransaction()
            ft?.replace(R.id.flContainer, DetailFragment)?.commit()
            ft?.addToBackStack(null)
            true
        }
    }
    companion object {
        const val TAG = "GroupChatDetail"
        const val MAX_CHAT_MESSAGES_TO_SHOW = 50
    }

    fun queryMembers(menu: Menu) {
        val query: ParseQuery<ParseUser> = ParseQuery.getQuery(ParseUser::class.java)
        query.include(ParseUser.KEY_OBJECT_ID)
        //query.addDescendingOrder("createdAt")
        //query.limit = 1
        //query.skip = offset * 20
       // query.whereEqualTo(ParseUser.KEY_OBJECT_ID, memberId)
        val list: ArrayList<String> = ArrayList()
        for (i in 0 until group.getMemberList()?.length()!!) {
            list.add(group.getMemberList()!!.getString(i))
        }
        query.whereContainedIn(ParseUser.KEY_OBJECT_ID,list)
        query.findInBackground(object : FindCallback<ParseUser> {
            override fun done(members: MutableList<ParseUser>?, e: ParseException?) {
                if (e != null) {
                    //Log.e(TAG, "Error getting posts")
                    Toast.makeText(requireContext(), "Error getting members", Toast.LENGTH_SHORT).show()
                } else {
                    if (members != null) {
                        //memberAdapter.clear()
                        allMembers.addAll(members)
                        Log.i("groupChat",members.toString())
                        menu.findItem(R.id.action_map_members).setEnabled(true)
                    }
                }
            }

        })
    }


}