package com.codepath.peterhe.foodies

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ParseUser
import com.parse.SaveCallback
import java.io.*


class SignUpActivity : AppCompatActivity() {
    var photoFile: File? = null
    val photoFileName = "photo.jpg"
    private var selectedImageUri: Uri? = null
    private lateinit var selectedImage: Bitmap
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // transparent status bar
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        getSupportActionBar()?.hide()
        findViewById<ImageButton>(R.id.btn_add_photo_signup).setOnClickListener {
            imageChooser()
        }
        findViewById<MaterialButton>(R.id.btn_signup).setOnClickListener {
            findViewById<TextInputLayout>(R.id.text_input_layout_email_signup)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_email_signup)?.setErrorEnabled(
                false
            )
            findViewById<TextInputLayout>(R.id.text_input_layout_password1_signup)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_password1_signup)?.setErrorEnabled(
                false
            )
            findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setErrorEnabled(
                false
            )
            findViewById<TextInputLayout>(R.id.text_input_layout_username_signup)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_username_signup)?.setErrorEnabled(
                false
            )
            val email = findViewById<EditText>(R.id.et_email_signup).text.toString()
            val password1 = findViewById<EditText>(R.id.et_password1_signup).text.toString()
            val password2 = findViewById<EditText>(R.id.et_password2_signup).text.toString()
            val username = findViewById<EditText>(R.id.et_username_signup).text.toString()
            val description = findViewById<EditText>(R.id.et_description_signup).text.toString()

            if (email != "" && username != "" && password1 != "" && password2 != "" && password1.equals(
                    password2
                ) && (selectedImageUri != null)
            ) {
                signUpUser(username, password1, email, description)
            } else {
                if (username == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_username_signup)?.setErrorEnabled(
                        true
                    )
                    findViewById<TextInputLayout>(R.id.text_input_layout_username_signup)?.setError(
                        "Username is required for registration."
                    )
                }
                if (email == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_email_signup)?.setErrorEnabled(
                        true
                    )
                    findViewById<TextInputLayout>(R.id.text_input_layout_email_signup)?.setError("Email is required for registration.")
                }
                if (password1 == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_password1_signup)?.setErrorEnabled(
                        true
                    )
                    findViewById<TextInputLayout>(R.id.text_input_layout_password1_signup)?.setError(
                        "Password is required for registration."
                    )
                }
                if (password2 == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setErrorEnabled(
                        true
                    )
                    findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setError(
                        "Please confirm your password."
                    )
                }
                if (Uri.EMPTY.equals(selectedImageUri)) {
                    Toast.makeText(this, "Must inlcude a profile Photo!", Toast.LENGTH_LONG)
                }
                if (!password1.equals(password2)) {
                    findViewById<EditText>(R.id.et_password2_signup)?.text?.clear()
                    findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setErrorEnabled(
                        true
                    )
                    findViewById<TextInputLayout>(R.id.text_input_layout_password2_signup)?.setError(
                        "Passwords don't match."
                    )
                }
            }

        }
    }

    private fun signUpUser(
        username: String,
        password: String,
        email: String,
        description: String = ""
    ) {
        progressDialog?.show()
        // Create the ParseUser
        val user = ParseUser()
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageByte = byteArrayOutputStream.toByteArray()
        val parseFile = ParseFile("image_file.png", imageByte)
        parseFile.saveInBackground(object:SaveCallback{
            override fun done(e: ParseException?) {
                // Log.i(TAG, "done")
                if (e == null) {
                    //Log.i(TAG, "done1")
                   // val user = ParseUser.getCurrentUser()
                    user.put("profile", parseFile)
                    // Set fields for the user to be created
                    user.setUsername(username)
                    user.setPassword(password)
                    user.setEmail(email)
                    user.put("description", description)
                    user.signUpInBackground { e ->
                        if (e == null) {
                            // Hooray! Let them use the app now
                            ParseUser.logOut();
                            showAlert("Account Created Successfully!","Please verify your email before Login", false)
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            // Toast.makeText(this, "Failed to create an account", Toast.LENGTH_SHORT).show()
                            // e.printStackTrace()
                            ParseUser.logOut();
                            showAlert("Error Account Creation failed","Account could not be created" + " :" + e?.message,true)
                        }
                    }
                    /*user.saveInBackground { exception ->
                        progressDialog?.dismiss()
                        if (exception != null) {
                            exception.printStackTrace()
                            ParseUser.logOut();
                            showAlert("Error Account Creation failed","Account could not be created" + " :" + e?.message,true)
                        } else {
                            //Toast.makeText(req, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            /*val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()*/
                            ParseUser.logOut();
                            showAlert("Account Created Successfully!","Please verify your email before Login", false)
                        }
                    }*/
                }else {
                    e.printStackTrace()
                }
            }

        })
        // Set fields for the user to be created
        /*user.setUsername(username)
        user.setPassword(password)
        user.setEmail(email)
        user.put("description", description)
        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! Let them use the app now
                    submitUserUpdate()
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
               // Toast.makeText(this, "Failed to create an account", Toast.LENGTH_SHORT).show()
               // e.printStackTrace()
                ParseUser.logOut();
                showAlert("Error Account Creation failed","Account could not be created" + " :" + e?.message,true)
            }
        }*/
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
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (i.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
            }
        }
        // pass the constant to compare it
        // with the returned requestCode
        // startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                 selectedImageUri = data?.data!! //
                if (null != selectedImageUri) {//
                    // update the preview image in the layout
                    // by this point we have the camera photo on disk
                   // val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview
                    val ivPreview: ImageButton = findViewById(R.id.btn_add_photo_signup)
                    val imageStream:InputStream = getContentResolver().openInputStream(selectedImageUri!!)!!
                    selectedImage = BitmapFactory.decodeStream(imageStream)
                    val selectedImage1 = Bitmap.createScaledBitmap(selectedImage, 110, 110, true)
                    ivPreview.setImageBitmap(selectedImage1)
                }
            }
        }

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
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageByte = byteArrayOutputStream.toByteArray()
        val parseFile = ParseFile("image_file.png", imageByte)
        parseFile.saveInBackground(object:SaveCallback{
            override fun done(e: ParseException?) {
               // Log.i(TAG, "done")
                if (e == null) {
                    //Log.i(TAG, "done1")
                    val user = ParseUser.getCurrentUser()
                    user.put("profile", parseFile)
                    user.saveInBackground { exception ->
                        progressDialog?.dismiss()
                        if (exception != null) {
                            exception.printStackTrace()
                            ParseUser.logOut();
                            showAlert("Error Account Creation failed","Account could not be created" + " :" + e?.message,true)
                        } else {
                            //Toast.makeText(req, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            /*val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()*/
                            ParseUser.logOut();
                            showAlert("Account Created Successfully!","Please verify your email before Login", false)
                        }
                    }
                }else {
                    e.printStackTrace()
                }
            }

        })

    }

    private fun showAlert(title: String, message: String, error: Boolean) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
                // don't forget to change the line below with the names of your Activities
                if (!error) {
                    val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        val ok = builder.create()
        ok.show()
    }

    companion object {
        const val SELECT_PICTURE = 200
        const val TAG = "SIGNUP"
    }
}