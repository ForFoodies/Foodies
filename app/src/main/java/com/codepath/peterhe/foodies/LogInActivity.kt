package com.codepath.peterhe.foodies

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.google.android.material.button.MaterialButton
import com.parse.ParseException
import com.parse.ParseUser
import com.parse.SaveCallback
import com.parse.SignUpCallback
import com.parse.facebook.ParseFacebookUtils
import org.json.JSONException
import org.json.JSONObject

class LogInActivity : AppCompatActivity() {
    private lateinit var permissions: Collection<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        //check if the user is already logged in
        if (ParseUser.getCurrentUser() != null) {
            gotoMainActivity()
        }
        // transparent status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        getSupportActionBar()?.hide()

        findViewById<MaterialButton>(R.id.redirect_signin).setOnClickListener{
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }

        findViewById<MaterialButton>(R.id.redirect_signup).setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        findViewById<MaterialButton>(R.id.btn_login_facebook).setOnClickListener {
            val dlg = ProgressDialog(this)
            dlg.setTitle("Please, wait a moment.")
            dlg.setMessage("Logging in...")
            dlg.show()
            permissions = listOf("public_profile", "email")
            ParseFacebookUtils.logInWithReadPermissionsInBackground(
                this,
                permissions
            ) { user: ParseUser?, err: ParseException? ->
                dlg.dismiss()
                when {
                    err != null -> {
                        Log.e("FacebookLoginExample", "done: ", err)
                        Toast.makeText(this, err.message, Toast.LENGTH_LONG).show()
                    }
                    user == null -> {
                        Toast.makeText(
                            this,
                            "The user cancelled the Facebook login.",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(
                            "FacebookLoginExample",
                            "Uh oh. The user cancelled the Facebook login."
                        )
                    }
                    user.isNew -> {
                        Toast.makeText(
                            this,
                            "User signed up through Facebook!",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(
                            "FacebookLoginExample",
                            "User signed up and logged in through Facebook!"
                        )
                        getUserDetailFromFB(true,user)
                    }
                    else -> {
                        getUserDetailFromFB(false,user)
                    }
                }
            }
        }
    }

    private fun gotoMainActivity(){
        val intent = Intent(this@LogInActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun getUserDetailFromFB(firstTime:Boolean, user:ParseUser) {
        val request =
            GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { `object`: JSONObject, _: GraphResponse? ->
                //val user = ParseUser()
                Log.i("Check", "Start")
                var username = ""
                var email = ""
                try {
                   // user.username = `object`.getString("name")
                    username = `object`.getString("name")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                try {
                    //user.email = `object`.getString("email")
                    email = `object`.getString("email")

                } catch (e: JSONException) {
                    e.printStackTrace()
                }


                if (firstTime) {
                    Log.i("Check", "firstTime")
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, this, permissions, object: SaveCallback{
                            override fun done(p0: ParseException?) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.i("Check", "firstTime1")
                                    user.setUsername(username)
                                    user.setEmail(email)
                                    user.signUpInBackground(object:SignUpCallback{
                                        override fun done(e: ParseException?) {
                                            if (e == null) {
                                                val intent = Intent(this@LogInActivity, MainActivity::class.java)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                startActivity(intent)
                                                finish()
                                            }else {
                                                Log.i("Check", "firstTime1.1")
                                                e.printStackTrace()
                                            }
                                        }

                                    })
                                }
                            }

                        })
                    }else{
                        Log.i("Check", "firstTime2.1")
                        ParseUser.logOut()
                        showAlert1("Account Created Successfully!","Please log in again via Facebook and verify your email!", false)
                       /* user.setUsername(username)
                        user.setEmail(email)
                        user.saveInBackground()
                        val intent = Intent(this@LogInActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()*/

                    }
                }else{
                    Log.i("Check", "NotfirstTime2.1")
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(
                            user,
                            this,
                            permissions,
                            object : SaveCallback {
                                override fun done(e: ParseException?) {
                                    if (ParseFacebookUtils.isLinked(user)) {
                                       // Log.d(TAG, "Woohoo, user logged in with Facebook!")
                                        user.setUsername(username)
                                        user.setEmail(email)
                                        user.saveInBackground()
                                        val intent = Intent(this@LogInActivity, MainActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            })
                    } else {
                        user.setUsername(username)
                        user.setEmail(email)
                        user.saveInBackground()
                        val intent = Intent(this@LogInActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }

            }
        val parameters = Bundle()
        parameters.putString("fields", "name,email")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun showAlert(title: String, message: String?) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
                dialog.cancel()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        val ok = builder.create()
        ok.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data)
    }

    private fun showAlert1(title: String, message: String, error: Boolean) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
                // don't forget to change the line below with the names of your Activities
            }
        val ok = builder.create()
        ok.show()
    }
}