package com.codepath.peterhe.foodies.fragments

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codepath.peterhe.foodies.*
import com.codepath.peterhe.foodies.R
import com.codepath.peterhe.foodies.databinding.ActivityMainBinding
import com.facebook.login.LoginManager
import com.parse.*
import com.parse.facebook.ParseFacebookUtils


class UserProfileFragment : Fragment() {
    private lateinit var user: ParseUser
    private lateinit var username: TextView
    private lateinit var profileImage: ImageView
    private lateinit var bio: TextView
    private var progressDialog: ProgressDialog? = null
    private lateinit var binding:ActivityMainBinding
    private var posts: ArrayList<Post> = arrayListOf()
    private lateinit var gridView: GridView
    private lateinit var postAdapter: GridPostAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        gridView = view.findViewById(R.id.PostGridView)
        queryPosts()
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
        progressDialog = ProgressDialog(requireContext())
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
            progressDialog!!.show()
            LoginManager.getInstance().logOut()
            ParseUser.logOutInBackground { e: ParseException? ->
                progressDialog!!.dismiss()
                if (e == null) {
                    //dlg.dismiss()
                    showAlert("So, you're going...", "Ok...Bye-bye then", true);
                } else {
                    //dlg.dismiss()
                    showAlert("Error...", e.message, false);
                }
            }
            // alertDisplayer("So, you are going back", "Bye Bye...")
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
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data)
    }

    fun queryPosts() {
        val user = ParseUser.getCurrentUser()
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.whereEqualTo("userId",user)
        query.orderByDescending("createdAt")
        query.findInBackground(object : FindCallback<Post>{
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Toast.makeText(requireContext(), "Error getting posts", Toast.LENGTH_SHORT).show()
                } else {
                    if (posts != null) {
                        Log.i("Profile","1.0")
                        postAdapter = GridPostAdapter(requireContext(), posts)
                        gridView.setAdapter(postAdapter)
                        //binding.gridView.
                    }
                }
                }

        })


    }

}