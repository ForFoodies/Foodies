package com.codepath.peterhe.foodies.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codepath.peterhe.foodies.MainActivity
import com.codepath.peterhe.foodies.R
import com.codepath.peterhe.foodies.SignUpActivity
import com.parse.ParseUser

class EditProfileFragment : Fragment() {

    lateinit var user: ParseUser
    lateinit var username: EditText
    lateinit var bio: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: add a back button, also for discover pages so all is consistent?
        // TODO: update password and email?
        user = ParseUser.getCurrentUser()
        username = view.findViewById<EditText>(R.id.etUsername)
        bio = view.findViewById<EditText>(R.id.etBio)

        // show current info
        username.setText(user.username)
        bio.setText(user.get("description").toString())

        view.findViewById<ImageButton>(R.id.ibEditProfileImage).setOnClickListener {
            // change profile image
            // TODO: show image after user uploaded new image and save as profile image, app crashes right now
            (activity as SignUpActivity).imageChooser()
        }

        view.findViewById<Button>(R.id.btnDoneEdit).setOnClickListener {
            // get EditText fields
            val newUsername = username.text.toString()
            val newBio = bio.text.toString()

            // update user info in parse
            user.put("username", newUsername);
            user.put("description", newBio);
            user.saveInBackground()

            // go back to profile fragment
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flContainer, UserProfileFragment())
            transaction.commit()
        }
    }
}