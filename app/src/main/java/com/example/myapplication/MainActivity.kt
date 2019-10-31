package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log.w
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.Adapters.RecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.recyclerview_item_row.*

class MainActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var postReference: DatabaseReference
    private lateinit var postReference2: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var infractionsList: MutableList<Models.Infraction>


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Sign Out
        profile_image.setOnClickListener {
            this.finish()
            signOut()
        }

        Glide.with(this)
            .load("https://scontent-mad1-1.xx.fbcdn.net/v/t1.0-9/26815589_890018277870115_8559101798127254589_n.jpg?_nc_cat=101&_nc_oc=AQmZKBsVmIHTzflxP9d48-u4aRHbFzecGGESF88JoywgXb859u7_UA0C_bMGBzsU-pg&_nc_ht=scontent-mad1-1.xx&oh=7f2484c1651d62b96cb8e96b2303c9bd&oe=5DF6BEEE")
            .into(profile_image)

        infractionsList = mutableListOf()

        recyclerView.setSlideOnFlingThreshold(3800);
        recyclerView.setItemTransitionTimeMillis(100)

        recyclerView.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build())

        //val recyclerView = findViewById<DiscreteScrollView>(R.id.recyclerView)
        postReference = FirebaseDatabase.getInstance().reference.child("Infraction")
        postReference2 =
            FirebaseDatabase.getInstance().reference.child("users/" + auth.currentUser!!.uid)

        getUser(object : Callback {
            override fun onSucces(UMat: String) {
                w("insideCallback", UMat)
                getData(UMat)
            }
        })


    }

    private fun getUser(callback: Callback) {

        val postListener2 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot2: DataSnapshot) {

                //send data in Callback
                callback.onSucces(dataSnapshot2.child("mat").value.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //w("dataMain", "loadPost:onCancelled", dataSnapshot2.value.toString())

            }
        }
        postReference2.addValueEventListener(postListener2)

    }

    private fun getData(UMat: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                progressBar.visibility = View.GONE
                infractionsList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val infraction = postSnapshot.getValue(Models.Infraction::class.java)
                    if (infraction?.UMat!!.equals(UMat)) {
                        infractionsList.add(infraction)
                    }
                }

                val adapter = RecyclerAdapter(infractionsList)
                recyclerView.adapter = adapter

                w("myTest", infractionsList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                w("dataMain", "loadPost:onCancelled", databaseError.toException())

            }
        }
        postReference.addValueEventListener(postListener)

    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, EmailPasswordActivity::class.java))
    }


    interface Callback {
        fun onSucces(UMat: String)
    }
}
