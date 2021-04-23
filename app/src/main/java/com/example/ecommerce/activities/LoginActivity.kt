package com.example.ecommerce.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.ecommerce.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

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

    }

    // Clickable components are login button, forgot password text and register text
    override fun onClick(v: View?){
        if(v != null) {
            when (v.id) {
                R.id.login_forgot_password -> {

                }

                R.id.login_btn_login -> {
                    loginUser()
                }

                R.id.login_tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(login_edt_email.text.toString().trim(){it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
                false
            }
            TextUtils.isEmpty(login_edt_password.text.toString().trim(){it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
                false
            }
            else -> {
                showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    private fun loginUser() {
        if(validateLoginDetails()){

            showProgress(resources.getString(R.string.please_wait))

            val email = login_edt_email.text.toString().trim { it <= ' ' }
            val password = login_edt_password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){

                        showErrorSnackBar("You are logged in successfully.", false)
                    }
                }

        }
    }
}
