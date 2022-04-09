package com.codepath.peterhe.foodies.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.codepath.peterhe.foodies.MainActivity
import com.codepath.peterhe.foodies.R
import com.codepath.peterhe.foodies.SignUpActivity
import com.parse.ParseUser
import java.io.File
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.parse.ParseFile
import com.google.android.material.button.MaterialButton
import java.io.*

class EditProfileFragment : Fragment() {

    lateinit var user: ParseUser
    lateinit var username: EditText
    lateinit var bio: EditText
    var photoFile: File? = null
    val photoFileName = "photo.jpg"
    private lateinit var selectedImageUri: Uri

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
            imageChooser()
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

    // this function is triggered when
    // the Select Image Button is clicked
    fun imageChooser() {
        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        photoFile = getPhotoFileUri(photoFileName)
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.foodies.fileprovider", photoFile!!)
            i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (i.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                // startActivityForResult(intent, SELECT_PICTURE)
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
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), SignUpActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(SignUpActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PHOTO) {
                // Get the url of the image from data
                selectedImageUri = data?.data!!
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    // by this point we have the camera photo on disk
                    val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview
                    val ivPreview: ImageButton = view?.findViewById(R.id.btn_add_photo_signup)!!
                    ivPreview.setImageBitmap(takenImage)
                }
            }
        }

    }

    companion object {
        const val SELECT_PHOTO = 200
        const val TAG = "EDIT_PROFILE"
    }
}