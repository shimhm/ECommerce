package com.example.ecommerce.firestore

import android.app.Activity
import android.util.Log
import com.example.ecommerce.activities.LoginActivity
import com.example.ecommerce.activities.RegisterActivity
import com.example.ecommerce.model.User
import com.example.ecommerce.util.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo : User){

        // mFireStore.collection("users") "users" -> Constants.USERS
        mFireStore.collection(Constants.USERS)
            .document(userInfo.uID).set(userInfo, SetOptions.merge())
            .addOnCompleteListener {


                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { exception ->

                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName,"Error while registering the user",exception)
            }
    }

    // current uID를 반환한다.
    fun getCurrentUID() : String{

        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUID = ""
        if(currentUser != null){
            currentUID = currentUser.uid
        }

        return currentUID

    }

    fun getUSerDetails(activity: Activity){

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())


                val user = document.toObject(User::class.java)!!

                when(activity){
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }

            }
            .addOnFailureListener { e ->

                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName, e.toString())

            }

    }

}