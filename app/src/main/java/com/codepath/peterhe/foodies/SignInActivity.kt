package com.codepath.peterhe.foodies

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.parse.ParseUser


class SignInActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        // transparent status bar
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        getSupportActionBar()?.hide()
        progressDialog = ProgressDialog(this@SignInActivity)
        findViewById<MaterialButton>(R.id.btn_login).setOnClickListener {
            findViewById<TextInputLayout>(R.id.text_input_layout_email_signin)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_email_signin)?.setErrorEnabled(
                false
            )
            findViewById<TextInputLayout>(R.id.text_input_layout_password_signin)?.setError(null)
            findViewById<TextInputLayout>(R.id.text_input_layout_password_signin)?.setErrorEnabled(
                false
            )

            val email = findViewById<EditText>(R.id.tv_user_email_login).text.toString()
            val password = findViewById<EditText>(R.id.et_password_signin).text.toString()
            if (email != "" && password != "") {
                loginUser(email, password)
            } else {
                if (email == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_email_signin)?.setErrorEnabled(
                        true
                    )
                    findViewById<TextInputLayout>(R.id.text_input_layout_email_signin)?.setError("Please enter your registered email or username.")

                }
                if (password == "") {
                    findViewById<TextInputLayout>(R.id.text_input_layout_password_signin)?.setErrorEnabled(
                        true
                    )
                    findViewById<TextInputLayout>(R.id.text_input_layout_password_signin)?.setError(
                        "Please enter your password."
                    )
                }
            }
        }

    }

    private fun loginUser(username: String, password: String) {
        progressDialog?.show()
        ParseUser.logInInBackground(
            username, password, ({ user, e ->
                progressDialog?.dismiss()
                if (user != null) {
                    // Hooray!  The user is logged in.
                   // Log.i(TAG, "Log in success")
                    //Toast.makeText(this, "Welcome to Foodies!", Toast.LENGTH_SHORT).show()
                   // gotoMainActivity()
                    showAlert("Login Successful", "Welcome, $username!", false)
                } else {
                    // Log in failed.  Look at the ParseException to see what happened.
                    ParseUser.logOut()
                    showAlert("Login Fail", e?.message + " Please try again", true)
                    //e.printStackTrace()
                   // findViewById<EditText>(R.id.et_password_signin).text.clear()
                    findViewById<TextInputLayout>(R.id.text_input_layout_password_signin)?.setErrorEnabled(
                        true
                    )
                    findViewById<TextInputLayout>(R.id.text_input_layout_email_signin)?.setErrorEnabled(
                        true
                    )
                   /* findViewById<TextInputLayout>(R.id.text_input_layout_password_signin)?.setError(
                        "Error Logging in. Invalid username and password combination."
                    )*/
                }
            })
        )
    }

   /* private fun gotoMainActivity() {
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }*/

    private fun showAlert(title: String, message: String, error: Boolean) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.cancel()
                // don't forget to change the line below with the names of your Activities
                if (!error) {
                    val intent = Intent(this@SignInActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }

            }
        val ok = builder.create()
        ok.show()
    }

    companion object {
        const val TAG = "SignInActivity"
    }
}