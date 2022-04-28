package com.codepath.peterhe.foodies.fragments

import android.R.attr.bitmap
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.codepath.peterhe.foodies.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.*


class EditProfileFragment : Fragment() {

    lateinit var user: ParseUser
    lateinit var username: EditText
    lateinit var bio: EditText
    lateinit var password: EditText

    var photoFile: File? = null
    val photoFileName = "Photo.jpg"
    private var selectedImageUri: Uri? = null
    private lateinit var selectedImage: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = ParseUser.getCurrentUser()
        username = view.findViewById<EditText>(R.id.etUsername)
        bio = view.findViewById<EditText>(R.id.etBio)
        password = view.findViewById<EditText>(R.id.etPassword)

        // show current info
        username.setText(user.username)
        bio.setText(user.get("description").toString())
        val image: ParseFile? = user.getParseFile("profile")
        val profileImage: ImageView = view.findViewById(R.id.ivProfileImage)
        Glide.with(requireContext()).load(image?.url).centerCrop()
            .into(profileImage)


        view.findViewById<ImageButton>(R.id.ibEditProfileImage).setOnClickListener {
            // change profile image
            // TODO: show image after user uploaded new image and save as profile image, app crashes right now
            imageChooser()
        }
        view.findViewById<Button>(R.id.btnCancelEdit).setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flContainer, UserProfileFragment())
            transaction.commit()
        }
        view.findViewById<Button>(R.id.btnDoneEdit).setOnClickListener {
            // get EditText fields
            val newUsername = username.text.toString()
            val newBio = bio.text.toString()
            val newPassword = password.text.toString()

            // update user info in parse
            user.put("username", newUsername);
            user.put("description", newBio);
            user.setPassword(newPassword)
            user.saveInBackground{exception->
                if (exception == null) {
                    // go back to profile fragment
                    if (photoFile == null) {
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.flContainer, UserProfileFragment())
                        transaction.commit()
                    } else {
                        submitUserUpdate()
                    }
                }else {
                    Toast.makeText(requireContext(),"Error updating profile", Toast.LENGTH_SHORT).show()
                }
            }

        }
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
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PHOTO)
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
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PHOTO) {
                // Get the url of the image from data
                selectedImageUri = data?.data!! //
                if (null != selectedImageUri) {//
                    // update the preview image in the layout
                    // by this point we have the camera photo on disk
                    // val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview
                    val ivPreview: ImageView = view?.findViewById(R.id.ivProfileImage)!!
                    val imageStream:InputStream = requireContext().getContentResolver().openInputStream(selectedImageUri!!)!!
                    selectedImage = BitmapFactory.decodeStream(imageStream)
                    ivPreview.setImageBitmap(selectedImage)
                }
            }
        }

    }

    private fun submitUserUpdate() {
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageByte = byteArrayOutputStream.toByteArray()
        val parseFile = ParseFile("image_file.png", imageByte)
        parseFile.saveInBackground(object: SaveCallback {
            override fun done(e: ParseException?) {
                Log.i(TAG, "done")
                if (e == null) {
                    Log.i(TAG, "done1")
                    val user = ParseUser.getCurrentUser()
                    user.put("profile", parseFile)
                    user.saveInBackground { exception ->
                        if (exception != null) {
                            exception.printStackTrace()
                        } else {
                            //Toast.makeText(req, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            Log.i(TAG, "done1.1.1.1")
                            val d: Drawable = BitmapDrawable(getResources(), Bitmap.createScaledBitmap(selectedImage, 32, 32, true))
                           requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)?.getMenu()
                                ?.getItem(2)?.setIcon(d)

                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.flContainer, UserProfileFragment())
                            transaction.commit()
                        }
                    }
                }else {
                    e.printStackTrace()
                }
            }

        })

    }

    fun queryUser(userId: String) {
        val query: ParseQuery<ParseUser> = ParseQuery.getQuery(ParseUser::class.java)
        query.include(ParseUser.KEY_OBJECT_ID)
        query.limit = 1
        query.whereEqualTo(ParseUser.KEY_OBJECT_ID, userId)
        query.findInBackground(object : FindCallback<ParseUser> {
            override fun done(user: MutableList<ParseUser>?, e: ParseException?) {
                if (e != null) {
                    //Log.e(TAG, "Error getting posts")
                    Toast.makeText(requireContext(), "Error getting members", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (user != null && user.size == 1) {
                        val image: ParseFile? = user[0].getParseFile("profile")
                        // Log.i("Main", image?.url.toString())
                        //val imageUrl:String = user[0].getString("profile_url")!!
                        Glide.with(requireContext()).load(image?.url).override(32, 32).apply(
                            RequestOptions().transforms(
                                CenterCrop(), RoundedCorners(50)
                            )
                        ).listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                p0: GlideException?,
                                p1: Any?,
                                p2: Target<Drawable>?,
                                p3: Boolean
                            ): Boolean {
                                //Log.e(TAG, "onLoadFailed")
                                //do something if error loading
                                return false
                            }

                            override fun onResourceReady(
                                p0: Drawable?,
                                p1: Any?,
                                p2: Target<Drawable>?,
                                p3: DataSource?,
                                p4: Boolean
                            ): Boolean {
                                //Log.d(TAG, "OnResourceReady")
                                //do something when picture already loaded
                                view?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.getMenu()
                                    ?.getItem(2)?.setIconTintMode(PorterDuff.Mode.DST)
                                view?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.getMenu()
                                    ?.getItem(2)?.setIcon(p0)
                                return false
                            }
                        }).into(view?.findViewById<ImageView>(R.id.iv_profilePlaceHolder)!!)

                    }
                }
            }

        })
    }

    companion object {
        const val SELECT_PHOTO = 27
        const val TAG = "EDIT_PROFILE"
    }
}