package com.codepath.peterhe.foodies

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.parse.*
import java.io.File


class SignUpActivity : AppCompatActivity() {
    var photoFile: ParseFile? = null
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.orange));
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
                //val file:File = File(filePath)
                //photoFile = ParseFile(file)
                /*photoFile!!.saveInBackground(object:SaveCallback {
                    override fun done(e: ParseException?) {
                        if (e == null) {
                            signUpUser(username, password1, email, description,photoFile!!)
                        } else {
                            e.printStackTrace()
                        }
                    }
                })*/
                signUpUser(username, password1, email, description,photoFile!!)

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
    private fun signUpUser(username:String, password:String,email:String, description:String = "", parseFile: ParseFile) {
        // Create the ParseUser
        val user = ParseUser()
        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)
        user.setEmail(email)
        user.put("description", description)
        user.put("profile", parseFile)
        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! Let them use the app now.
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

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
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
                selectedImageUri = data?.data!!
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    //IVPreviewImage.setImageURI(selectedImageUri)
                    findViewById<ImageButton>(R.id.btn_add_photo_signup).setImageURI(selectedImageUri)
                }
            }
        }
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

    companion object {
        const val SELECT_PICTURE = 200
    }
}