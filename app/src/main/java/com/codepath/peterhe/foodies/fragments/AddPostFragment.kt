package com.codepath.peterhe.foodies.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.codepath.peterhe.foodies.Post
import com.codepath.peterhe.foodies.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ParseUser
import com.parse.SaveCallback
import java.io.ByteArrayOutputStream
import java.io.InputStream


/**
 * A simple [Fragment] subclass.
 * Use the [AddPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddPostFragment : Fragment() {
    private var bundle: Bundle? = null
    private var selectedImageUri: Uri? = null
    private lateinit var selectedImage: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bundle = this.arguments
        if (bundle != null) {
            selectedImageUri = bundle?.getParcelable<Uri>("PostDetail")!!
        } else {

        }

        val ivPreview: ImageView = view?.findViewById(R.id.iv_addPost)!!
        val imageStream: InputStream = requireContext().getContentResolver().openInputStream(selectedImageUri!!)!!
        selectedImage = BitmapFactory.decodeStream(imageStream)
        ivPreview.setImageBitmap(selectedImage)
        view?.findViewById<Button>(R.id.btn_PostAddPost).setOnClickListener {
            val caption:String = view?.findViewById<EditText>(R.id.et_addPost).text.toString()
            submitUserUpdate(caption)
        }
        view?.findViewById<Button>(R.id.btn_cancelAddPost).setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flContainer, UserProfileFragment())
            transaction.commit()
        }

    }

    private fun submitUserUpdate(caption:String) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageByte = byteArrayOutputStream.toByteArray()
        val parseFile = ParseFile("post_file.png", imageByte)
        parseFile.saveInBackground(object: SaveCallback {
            override fun done(e: ParseException?) {
                Log.i(EditProfileFragment.TAG, "done")
                if (e == null) {
                    Log.i(EditProfileFragment.TAG, "done1")
                    //val user = ParseUser.getCurrentUser()
                    //user.put("profile", parseFile)
                    val post = Post()
                    post.setCaption(caption)
                    post.setImage(parseFile)
                    post.setUserId(ParseUser.getCurrentUser())
                    post.saveInBackground { exception ->
                        if (exception != null) {
                            exception.printStackTrace()
                        } else {
                            //Toast.makeText(req, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            Log.i(EditProfileFragment.TAG, "done1.1.1.1")
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

}