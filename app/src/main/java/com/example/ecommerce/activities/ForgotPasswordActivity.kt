package com.example.ecommerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ecommerce.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.my_toolbar

class ForgotPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()
    }

    // 뒤로가기 버튼
    // !!!!!!!가려져서 안 보임!!!!!!!!!!!!!!
    private fun setupActionBar() {

        setSupportActionBar(my_toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_black_24dp)
        }

        my_toolbar.setNavigationOnClickListener { onBackPressed() }

        forgotpass_btn_submit.setOnClickListener {
            val email: String = forgotpass_edt_email.text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
            } else {
                showProgress(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->

                        hideProgressDialog()

                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "success to send email",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()

                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }

    }
}
