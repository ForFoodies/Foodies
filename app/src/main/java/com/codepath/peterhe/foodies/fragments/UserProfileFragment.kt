package com.codepath.peterhe.foodies.fragments

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codepath.peterhe.foodies.LogInActivity
import com.codepath.peterhe.foodies.MainActivity
import com.codepath.peterhe.foodies.R
import com.facebook.login.LoginManager
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ParseUser
import com.parse.facebook.ParseFacebookUtils

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

        // set up buttons
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            // log out user
            ParseUser.logOut()
            goToLoginActivity()
        }

        view.findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
            // go into edit mode
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flContainer, EditProfileFragment())
            transaction.commit()
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(activity, LogInActivity::class.java)
        startActivity(intent)
        // close current activity
        activity?.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_profile, menu)
        menu.findItem(R.id.action_logout).setOnMenuItemClickListener { item ->
            /*val intent = Intent(requireContext(), LogInActivity::class.java)
            ParseUser.logOut()
            startActivity(intent)
            requireActivity().finish()
            //Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()*/
            //val dlg = ProgressDialog(requireContext())
            //dlg.setTitle("Please, wait a moment.")
            //dlg.setMessage("Logging out...")
            //dlg.show()
            LoginManager.getInstance().logOut()
            ParseUser.logOutInBackground { e: ParseException? ->
                if (e == null) {
                    //dlg.dismiss()
                    showAlert("So, you're going...", "Ok...Bye-bye then", true);
                }else {
                    //dlg.dismiss()
                    showAlert("Error...", e.message, false);
                }
            }
           // alertDisplayer("So, you are going back", "Bye Bye...")
            true
        }
    }

   /* private fun alertDisplayer(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext()).setTitle(title).setMessage(message).setPositiveButton("OK", object:
            DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.cancel()
                val intent = Intent(requireContext(), LogInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                requireActivity().finish()
            }

        })
        val ok: AlertDialog = builder.create()
        ok.show()
    }*/

    private fun showAlert(title: String, message: String?, isOk: Boolean) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
                if (isOk) {
                    val intent = Intent(requireContext(), LogInActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        val ok = builder.create()
        ok.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ParseFacebookUtils.onActivityResult(requestCode,resultCode,data)
    }

}