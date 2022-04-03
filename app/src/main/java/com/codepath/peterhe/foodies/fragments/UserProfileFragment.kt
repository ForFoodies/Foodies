package com.codepath.peterhe.foodies.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.codepath.peterhe.foodies.LogInActivity
import com.codepath.peterhe.foodies.R
import com.parse.ParseUser

class UserProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_profile, menu)
        menu.findItem(R.id.action_logout).setOnMenuItemClickListener{item ->
            val intent = Intent(requireContext(), LogInActivity::class.java)
            ParseUser.logOut()
            startActivity(intent)
            requireActivity().finish()
           //Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()
            true
        }
    }

}