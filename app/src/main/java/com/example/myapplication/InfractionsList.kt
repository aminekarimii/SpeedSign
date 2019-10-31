package com.example.myapplication

import android.os.Bundle
import android.util.Log.w
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapters.AdminRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_infractions_list.*
import kotlinx.android.synthetic.main.recyclerview_item_row.*


class InfractionsList : AppCompatActivity() {

    private lateinit var infractionsList: MutableList<Models.Infraction>
    private lateinit var postReference: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var value1: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infractions_list)

        val intent = intent

        value1 = intent.getStringExtra("userID")

        header_tilte_admin.text = "Les infractions commis par: $value1"

        w("myTest", value1)

        your_speed

        infractionsList = mutableListOf()

        database = FirebaseDatabase.getInstance().reference

        postReference = FirebaseDatabase.getInstance().reference
            .child("Infraction")

        val linearVertical = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        admin_recyclerView.layoutManager = linearVertical

        getData()
    }


    private fun getData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                infractionsList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val infraction = postSnapshot.getValue(Models.Infraction::class.java)
                    if (infraction?.UMat!!.equals(value1)) {
                        infractionsList.add(infraction)
                    }

                }

                val adapter =
                    AdminRecyclerAdapter(infractionsList)
                admin_recyclerView.adapter = adapter

                //w("myTest", infractionsList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                w("dataMain", "loadPost:onCancelled", databaseError.toException())

            }
        }
        postReference.addValueEventListener(postListener)

    }
}
