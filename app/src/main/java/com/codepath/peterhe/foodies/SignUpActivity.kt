package com.codepath.peterhe.foodies

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.parse.ParseFile
import com.parse.ParseUser
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.*


class SignUpActivity : AppCompatActivity() {
    var photoFile: File? = null
    val photoFileName = "photo.jpg"
    private lateinit var selectedImageUri: Uri
    private lateinit var mCurrentPhotoPath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.primary));
        getSupportActionBar()?.hide()
        findViewById<ImageButton>(R.id.btn_add_photo_signup).setOnClickListener {
            imageChooser()
        }
        findViewById<ImageButton>(R.id.btn_signup).setOnClickListener {
            findViewById<TextInputLayout>(R.id.text_input_layout_email_signup)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_email_signup)?.setErrorEnabled(false)
            findViewById<TextInputLayout>(R.id.text_input_layout_password1_signup)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_password1_signup)?.setErrorEnabled(false)
            findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setErrorEnabled(false)
            findViewById<TextInputLayout>(R.id.text_input_layout_username_signup)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_username_signup)?.setErrorEnabled(false)
            val email = findViewById<EditText>(R.id.et_email_signup).text.toString()
            val password1 = findViewById<EditText>(R.id.et_password1_signup).text.toString()
            val password2 = findViewById<EditText>(R.id.et_password2_signup).text.toString()
            val username = findViewById<EditText>(R.id.et_username_signup).text.toString()
            val description = findViewById<EditText>(R.id.et_description_signup).text.toString()

            if (email != "" && username != "" && password1 != "" && password2 != "" && password1.equals(password2) &&  (selectedImageUri != null)) {
             /*   var image: ByteArray? = null
                try {
                    image = readInFile(mCurrentPhotoPath)
                } catch (e: Exception) {
                    e.printStackTrace()
                }*/
                // Create the ParseFile
                // Create the ParseFile
               /* photoFile = ParseFile("picturePath", image)
                photoFile!!.saveInBackground(object : SaveCallback {
                    override fun done(e: ParseException?) {
                        // If successful add file to user and signUpInBackground
                        if (null == e) {
                            signUpUser(username, password1, email, description,photoFile!!)
                        } else {
                            Toast.makeText(this@SignUpActivity,"Failed to save profile photo",Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                })*/
               signUpUser(username, password1, email, description)
            } else {
                if (username == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_username_signup)?.setErrorEnabled(true)
                    findViewById<TextInputLayout>(R.id.text_input_layout_username_signup)?.setError("Username is required for registration.")
                }
                if (email == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_email_signup)?.setErrorEnabled(true)
                    findViewById<TextInputLayout>(R.id.text_input_layout_email_signup)?.setError("Email is required for registration.")
                }
                if (password1 == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_password1_signup)?.setErrorEnabled(true)
                    findViewById<TextInputLayout>(R.id.text_input_layout_password1_signup)?.setError("Password is required for registration.")
                }
                if (password2 == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setErrorEnabled(true)
                    findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setError("Please confirm your password.")
                }
                if (Uri.EMPTY.equals(selectedImageUri)) {
                    Toast.makeText(this,"Must inlcude a profile Photo!",Toast.LENGTH_LONG)
                }
                if (!password1.equals(password2)) {
                    findViewById<EditText>(R.id.et_password2_signup)?.text?.clear()
                    findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setErrorEnabled(true)
                    findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setError("Passwords don't match.")
                }
            }

        }
    }
    private fun signUpUser(username:String, password:String,email:String, description:String = "") {
        // Create the ParseUser
        val user = ParseUser()
        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)
        user.setEmail(email)
        user.put("description", description)
        //user.put("profile", ParseFile(picture))
        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! Let them use the app now
                    submitUserUpdate()
                Toast.makeText(this,"Account created successfully!",Toast.LENGTH_SHORT).show()
                intent = Intent(this@SignUpActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(this,"Failed to create an account",Toast.LENGTH_SHORT).show()
                e.printStackTrace()
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
        i.action = Intent.ACTION_GET_CONTENT
        photoFile = getPhotoFileUri(photoFileName)
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (i.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
               // startActivityForResult(intent, SELECT_PICTURE)
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
            }
        }
        // pass the constant to compare it
        // with the returned requestCode
       // startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }
    /*fun imageChooser() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
    }*/

    // this function is triggered when user
    // selects the image from the imageChooser
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data?.data!!
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    //IVPreviewImage.setImageURI(selectedImageUri)
                   //findViewById<ImageButton>(R.id.btn_add_photo_signup).setImageURI(selectedImageUri)
                   // val uriPathHelper = URIPathHelper()
                   // mCurrentPhotoPath = PathUtil.getPath(this, selectedImageUri)
                   // mCurrentPhotoPath = uriPathHelper.getPath(this, selectedImageUri)!!
                   // val file = File(selectedImageUri.getPath()) //create path from uri
                   // val split: List<String> = file.path.split(":") //split the path.
                   // mCurrentPhotoPath = split[1] //assign it to a string(your choice).
                    // by this point we have the camera photo on disk
                    val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview
                    val ivPreview: ImageButton = findViewById(R.id.btn_add_photo_signup)
                    ivPreview.setImageBitmap(takenImage)
                }
            }
        }
       /* if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!= null) {
            selectedImageUri = data.getData()!!

            findViewById<ImageButton>(R.id.btn_add_photo_signup).setImageURI(selectedImageUri)
        }*/
    }

    /*fun convertImageUriToFile(imageUri: Uri?, activity: Activity): File? {
        var cursor: Cursor? = null //  w  w  w.  ja  va  2 s .  c o m
        return try {
            val proj = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.ImageColumns.ORIENTATION
            )
            cursor = activity
                .managedQuery(imageUri, proj, null, null, null)
            val file_ColumnIndex: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val orientation_ColumnIndex: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION)
            if (cursor.moveToFirst()) {
                val orientation: String = cursor
                    .getString(orientation_ColumnIndex)
                return File(cursor.getString(file_ColumnIndex))
            }
            null
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }*/
    /*
This method can parse out the real local file path from a file URI.
*/
    @Throws(IOException::class)
    private fun readInFile(path: String): ByteArray? {
        var data: ByteArray? = null
        val file = File(path)
        val input_stream: InputStream = BufferedInputStream(FileInputStream(file))
        val buffer = ByteArrayOutputStream()
        data = ByteArray(16384) // 16K
        var bytes_read: Int
        while (input_stream.read(data, 0, data.size).also { bytes_read = it } != -1) {
            buffer.write(data, 0, bytes_read)
        }
        input_stream.close()
        return buffer.toByteArray()
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    private fun submitUserUpdate() {
        val user = ParseUser.getCurrentUser()
        user.put("profile", ParseFile(photoFile))
        user.saveInBackground { exception ->
            if (exception != null) {
                //Log.e(TAG, "ERROR submitting a post")
                exception.printStackTrace()
                Toast.makeText(this, "Error adding profile photo", Toast.LENGTH_SHORT).show()
            } else {
                //Log.i(TAG, "Successfully submitted a post")
                Toast.makeText(this, "Successfully added profile photo", Toast.LENGTH_SHORT).show()
                /*view?.findViewById<ImageButton>(R.id.btn_join_GroupDetail)?.setEnabled(false)
                view?.findViewById<ImageButton>(R.id.btn_join_GroupDetail)?.setVisibility(View.GONE)
                view?.findViewById<ImageButton>(R.id.btn_cancel_groupDetail)?.setEnabled(false)
                view?.findViewById<ImageButton>(R.id.btn_cancel_groupDetail)?.setVisibility(View.GONE)
                requireActivity().setTitle("${group.getName()} (Already Joined)")
                allMembers.add(ParseUser.getCurrentUser())
                memberAdapter.notifyDataSetChanged()*/
                //ft.detach(this).attach(this).commit()

                //val fm = getFragmentManager()
                // fm?.popBackStack()
                /*val bundle = Bundle()
                bundle.putParcelable("RestaurantDetail", restaurant)
                val DetailFragment = RestaurantDetailFragment()
                DetailFragment.setArguments(bundle)
                // Log.i(RestaurantFragment.TAG, "Restaurant ${restaurants[position]}")
                ft?.replace(R.id.flContainer, DetailFragment)?.commit()
                requireActivity().setTitle("${restaurant.name}")
                ft?.addToBackStack(null)*/
            }
        }
    }

    companion object {
        const val SELECT_PICTURE = 200
        const val TAG = "SIGNUP"
    }
}