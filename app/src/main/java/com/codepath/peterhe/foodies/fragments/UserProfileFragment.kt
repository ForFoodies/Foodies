package com.codepath.peterhe.foodies.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.codepath.peterhe.foodies.LogInActivity
import com.codepath.peterhe.foodies.R
import com.codepath.peterhe.foodies.SignUpActivity
import com.parse.ParseFile
import com.parse.ParseUser
import java.lang.reflect.Array.newInstance
import javax.xml.datatype.DatatypeFactory.newInstance

class UserProfileFragment : Fragment() {
    lateinit var user: ParseUser
    lateinit var username: TextView
    lateinit var profileImage: ImageView
    lateinit var bio: TextView

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

        // load in user information
        user = ParseUser.getCurrentUser()
        username = view.findViewById(R.id.tvUsername)
        profileImage = view.findViewById(R.id.ivProfileImage)
        bio = view.findViewById(R.id.tvBio)

        username.text = user.get("username").toString()
        bio.text = user.get("description").toString()
        val image: ParseFile? = user.getParseFile("profile")
        Glide.with(requireContext()).load(image?.url)
            .into(profileImage)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_profile, menu)
        menu.findItem(R.id.action_logout).setOnMenuItemClickListener { item ->
            val intent = Intent(requireContext(), LogInActivity::class.java)
            ParseUser.logOut()
            startActivity(intent)
            requireActivity().finish()
            //Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()
            true
        }
        menu.findItem(R.id.action_edit).setOnMenuItemClickListener {
            // go into edit mode
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flContainer, EditProfileFragment())
            transaction.commit()
            true
        }
    }
}