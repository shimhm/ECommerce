package com.example.ecommerce.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.ecommerce.R
import com.example.ecommerce.firestore.FirestoreClass
import com.example.ecommerce.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        setupActionBar()

        register_tv_login.setOnClickListener {
//            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
            onBackPressed()
        }

        register_btn_register.setOnClickListener {
            registerUser()
        }
    }

    // 뒤로가기 버튼
    private fun setupActionBar(){

        setSupportActionBar(my_toolbar)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_black_24dp)
        }

        my_toolbar.setNavigationOnClickListener { onBackPressed() }

    }

    // new user 등장
    private fun validateRegisterDetails(): Boolean{
        return when {
            TextUtils.isEmpty(register_edt_first_name.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_first_name), true)
                false
            }
            TextUtils.isEmpty(register_edt_last_name.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_last_name), true)
                false
            }
            TextUtils.isEmpty(register_edt_email.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
                false
            }
            TextUtils.isEmpty(register_edt_password.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
                false
            }
            TextUtils.isEmpty(register_edt_password_confirm.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_confirm), true)
                false
            }
            register_edt_password.text.toString().trim { it <= ' ' } !=
                    register_edt_password_confirm.text.toString().trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !register_cb_condition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_check_condition), true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    private fun registerUser() {
        if(validateRegisterDetails()){

            showProgress(resources.getString(R.string.please_wait))

            val email: String = register_edt_email.text.toString().trim { it <= ' ' }
            val password: String = register_edt_password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    //hideProgressDialog()

                    if(task.isSuccessful){

                        // FirebaseFirestore에 users에 user 정보 추가
                        val firebaseUser : FirebaseUser = task.result!!.user

                        val user = User(
                            firebaseUser.uid,
                            register_edt_first_name.text.toString().trim { it <= ' ' },
                            register_edt_last_name.text.toString().trim { it <= ' ' },
                            email
                        )

                        //showErrorSnackBar("Your are registered successfully. Your userID is ${firebaseUser.uid}", false)

                        FirestoreClass().registerUser(this@RegisterActivity, user)

                        //FirebaseAuth.getInstance().signOut()
                        //finish()


                    }else{

                        hideProgressDialog()

                        showErrorSnackBar(task.exception!!.message.toString(), true)

                    }
                }
        }
    }

    fun userRegistrationSuccess(){

        hideProgressDialog()

        Toast.makeText(this@RegisterActivity, "success register", Toast.LENGTH_SHORT).show()
    }
}
