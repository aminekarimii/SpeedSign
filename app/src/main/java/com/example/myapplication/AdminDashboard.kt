package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admin_dashboard.*

class AdminDashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        auth = FirebaseAuth.getInstance()

        admin_logout.setOnClickListener{
            signOut()
            this.finish()
        }

        search_button.setOnClickListener {
            sendData()
        }


    }

    private fun sendData(){
        if (!validateForm()) {
            return
        }
        val intent = Intent(this, InfractionsList::class.java)
        intent.putExtra("userID", fielduserMat.text.toString())
        startActivity(intent)

    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, EmailPasswordActivity::class.java))
    }


    private fun validateForm(): Boolean {
        var valid = true

        val userMat = fielduserMat.text.toString()
        if (TextUtils.isEmpty(userMat)) {
            fielduserMat.error = "Required."
            valid = false
        } else {
            fielduserMat.error = null
        }
        return valid
    }
}
