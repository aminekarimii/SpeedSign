package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Log.w
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_emailpassword.*
import kotlinx.android.synthetic.main.content_main.*


class EmailPasswordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    // Write a message to the database
    private lateinit var database: DatabaseReference
    private lateinit var postReferenceUsers: DatabaseReference

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emailpassword)

        // Buttons
        emailSignInButton.setOnClickListener(this)
        emailRegisterButton.setOnClickListener(this)
        changeToRegister.setOnClickListener(this)
        changeToLogin.setOnClickListener(this)

        database = FirebaseDatabase.getInstance().reference


        auth = FirebaseAuth.getInstance()
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (currentUser.email.equals("admin@gmail.com")) {
                this.finish()
                startActivity(Intent(this, AdminDashboard::class.java))
            } else {
                this.finish()
                startActivity(Intent(this, MainActivity::class.java))
            }

        }

    }
    // [END on_start_check_user]

    private fun createAccount(
        email: String,
        password: String,
        matricule: String
    ) {
        Log.d(TAG, "createAccount:$email")
        if (!validateFormRegister()) {
            return
        }

        showProgressDialog()

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    Log.d(TAG, "createUserWithEmail:success")
                    //AddUserToBD
                    val currentFirebaseUser = FirebaseAuth.getInstance().currentUser?.uid
                    addUser(email, matricule, currentFirebaseUser.toString())
                    startActivity(Intent(this, MainActivity::class.java))
                    this.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // [START_EXCLUDE]
                hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
    }

    private fun addUser(
        email: String,
        Matricule: String,
        uid: String
    ) {
        val user = Models.User(email, Matricule, "1")
        database.child("users").child(uid).setValue(user)
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    user = auth.currentUser!!
                    getUsertype()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }


    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            valid = false
        } else {
            fieldPassword.error = null
        }
        return valid
    }

    private fun validateFormRegister(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(fieldEmailRegister.text.toString())) {
            fieldEmailRegister.error = "Required."
            valid = false
        } else {
            fieldEmailRegister.error = null
        }

        if (TextUtils.isEmpty(fieldPasswordRegister.text.toString())) {
            fieldPasswordRegister.error = "Required."
            valid = false
        } else {
            fieldPasswordRegister.error = null
        }

        return valid
    }

    private fun updateUI(view: Int) {
        hideProgressDialog()
        if (view == R.id.changeToRegister) {
            emailPasswordFields.visibility = View.GONE
            registerFields.visibility = View.VISIBLE

        } else if (view == R.id.changeToLogin) {
            emailPasswordFields.visibility = View.VISIBLE
            registerFields.visibility = View.GONE
        }


    }


    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.changeToRegister -> updateUI(R.id.changeToRegister)
            R.id.changeToLogin -> updateUI(R.id.changeToLogin)
            R.id.emailSignInButton -> signIn(
                fieldEmail.text.toString(),
                fieldPassword.text.toString()
            )
            R.id.emailRegisterButton -> createAccount(
                fieldEmailRegister.text.toString(),
                fieldPasswordRegister.text.toString(),
                fieldMatriculeRegister.text.toString()
            )
        }
    }

    private fun getUsertype() {
        postReferenceUsers = FirebaseDatabase.getInstance().reference
            .child("users").child(user!!.uid).child("type")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("myTest", "signInWithEmail:success")
                if (dataSnapshot.value!!.equals("1")) {

                    //callback.onSucces("1")
                    finish()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                } else {
                    //callback.onSucces("0")
                    finish()
                    startActivity(Intent(applicationContext, AdminDashboard::class.java))
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                w("dataMain", "loadPost:onCancelled", databaseError.toException())

            }
        }
        postReferenceUsers.addValueEventListener(postListener)

    }

    interface Callback {
        fun onSucces(Type: String)
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}