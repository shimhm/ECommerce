package com.example.ecommerce.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.ecommerce.R
import com.example.ecommerce.firestore.FirestoreClass
import com.example.ecommerce.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // FullScreen
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        login_tv_forgot_password.setOnClickListener(this)

        login_btn_login.setOnClickListener(this)

        login_tv_register.setOnClickListener(this)

    }

    fun userLoggedInSuccess(user: User){

        hideProgressDialog()

        Log.i("First Name : ", user.firstName)
        Log.i("Last Name : ", user.lastName)
        Log.i("Email : ", user.email)

        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()

    }

    // Clickable components are login button, forgot password text and register text
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.login_tv_forgot_password -> {
                    Log.d("Click", "forgot password")

                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.login_btn_login -> {
                    Log.d("Click", "login")

                    loginUser()
                }

                R.id.login_tv_register -> {
                    Log.d("Click", "register")

                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(login_edt_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
                false
            }
            TextUtils.isEmpty(login_edt_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    private fun loginUser() {
        if (validateLoginDetails()) {

            showProgress(resources.getString(R.string.please_wait))

            val email = login_edt_email.text.toString().trim { it <= ' ' }
            val password = login_edt_password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // hideProgressDialog()

                    if (task.isSuccessful) {

                        FirestoreClass().getUSerDetails(this@LoginActivity)

                    } else {

                        hideProgressDialog()

                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }

        }
    }
}
