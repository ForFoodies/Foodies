package com.codepath.peterhe.foodies.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.codepath.peterhe.foodies.*
import com.codepath.peterhe.foodies.R
import com.codepath.peterhe.foodies.databinding.ActivityMainBinding
import com.facebook.login.LoginManager
import com.parse.*
import com.parse.facebook.ParseFacebookUtils
import java.io.File
import java.io.InputStream


class UserProfileFragment : Fragment() {
    private lateinit var user: ParseUser
    private lateinit var username: TextView
    private lateinit var time: TextView
    private lateinit var number: TextView
    private lateinit var profileImage: ImageView
    private lateinit var bio: TextView
    private var progressDialog: ProgressDialog? = null
    private lateinit var binding:ActivityMainBinding
    private var posts: ArrayList<Post> = arrayListOf()
    private lateinit var gridView: GridView
    private lateinit var postAdapter: GridPostAdapter
    private var bundle: Bundle? = null
    var photoFile: File? = null
    val photoFileName = "Post.jpg"
    private var selectedImageUri: Uri? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bundle = this.arguments
        if(bundle != null){
            Log.i("parceable","1")
            user = bundle?.getParcelable<ParseUser>("MemberDetail")!!

        } else {
            Log.i("parceable","2")
            user = ParseUser.getCurrentUser()
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        gridView = view.findViewById(R.id.PostGridView)
        queryPosts(user)
        setHasOptionsMenu(true)

        // load in user information
        username = view.findViewById(R.id.tvUsername)
        profileImage = view.findViewById(R.id.ivProfileImage)
        bio = view.findViewById(R.id.tvBio)
        time = view.findViewById(R.id.tv_ProfileTime)
        number = view.findViewById(R.id.tv_ProfileNumber)

        username.text = user.get("username").toString()
        bio.text = user.get("description").toString()
        val timeText = "${user.getCreatedAt()}".substring(4, 10) +" " + "${user.getCreatedAt()}".substring(24, 28)
        time.text = timeText
        number.text = user.getJSONArray("groupList")?.length().toString()
        val gender:String? = user.get("gender")?.toString()
        if ("male".equals(gender)) {
            Log.i("Profile","1")
            view.findViewById<TextView>(R.id.tv_FemaleProfile).setVisibility(GONE)
            view.findViewById<TextView>(R.id.tv_maleProfile).setVisibility(VISIBLE)
        } else if ("female".equals(gender)) {
            Log.i("Profile","2")
            view.findViewById<TextView>(R.id.tv_FemaleProfile).setVisibility(VISIBLE)
            view.findViewById<TextView>(R.id.tv_maleProfile).setVisibility(GONE)
        } else {
            Log.i("Profile","3")
            view.findViewById<TextView>(R.id.tv_FemaleProfile).setVisibility(GONE)
            view.findViewById<TextView>(R.id.tv_maleProfile).setVisibility(GONE)
        }
        val image: ParseFile? = user.getParseFile("profile")
        Glide.with(requireContext()).load(image?.url).centerCrop()
            .into(profileImage)
        progressDialog = ProgressDialog(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_profile, menu)
        menu.findItem(R.id.action_logout).setOnMenuItemClickListener { item ->
            progressDialog!!.show()
            LoginManager.getInstance().logOut()
            ParseUser.logOutInBackground { e: ParseException? ->
                progressDialog!!.dismiss()
                if (e == null) {
                    //dlg.dismiss()
                    showAlert("Are you sure you want to leave?", "Foodies hope to see you again!", true);
                } else {
                    //dlg.dismiss()
                    showAlert("Error...", e.message, false);
                }
            }
            // alertDisplayer("So, you are going back", "Bye Bye...")
            true
        }
        menu.findItem(R.id.action_edit).isEnabled = true
        menu.findItem(R.id.action_edit).isVisible = true
        menu.findItem(R.id.action_edit).setOnMenuItemClickListener {
            // go into edit mode
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flContainer, EditProfileFragment())
            transaction.commit()
            true
        }

        menu.findItem(R.id.action_addPost).isEnabled = true
        menu.findItem(R.id.action_addPost).isVisible = true
        menu.findItem(R.id.action_addPost).setOnMenuItemClickListener {
            // go into edit mode
            imageChooser()
            true
        }


    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (bundle != null) {
            menu.findItem(R.id.action_edit).isEnabled = false
            menu.findItem(R.id.action_edit).isVisible = false
        }
    }

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
        if (requestCode != SELECT_PHOTO) {
            ParseFacebookUtils.onActivityResult(requestCode, resultCode, data)
        } else {
            if (resultCode == Activity.RESULT_OK) {
                selectedImageUri = data?.data!! //
                if (null != selectedImageUri) {//
                    val bundle = Bundle()
                    bundle.putParcelable("PostDetail", selectedImageUri)
                    val DetailFragment = AddPostFragment()
                    DetailFragment.setArguments(bundle)
                    val ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
                    ft?.replace(R.id.flContainer, DetailFragment)?.commit()
                    requireActivity().setTitle("Add Post")
                    ft?.addToBackStack(null)
                }
            }
        }

    }

    fun queryPosts(user: ParseUser) {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.whereEqualTo("userId",user)
        query.orderByDescending("createdAt")
        query.findInBackground(object : FindCallback<Post>{
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.i("Get posts",e.message.toString())
                    Toast.makeText(requireContext(), "Error getting posts", Toast.LENGTH_SHORT).show()
                } else {
                    if (posts != null) {
                        postAdapter = GridPostAdapter(requireContext(), posts)
                        gridView.setAdapter(postAdapter)
                        //binding.gridView.
                    }
                }
                }

        })


    }

    // this function is triggered when
    // the Select Image Button is clicked
    fun imageChooser() {
        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_PICK
        photoFile = getPhotoFileUri(photoFileName)
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (i.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(Intent.createChooser(i, "Select Picture"),
                    SELECT_PHOTO
                )
            }
        }
        // pass the constant to compare it
        // with the returned requestCode
        // startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), EditProfileFragment.TAG)


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(EditProfileFragment.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    companion object {
        const val SELECT_PHOTO = 25
        const val TAG = "EDIT_PROFILE"
    }


}