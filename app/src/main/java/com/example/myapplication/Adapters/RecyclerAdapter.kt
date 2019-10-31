package com.example.myapplication.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models
import com.example.myapplication.R
import kotlinx.android.synthetic.main.recyclerview_item_row.*


class RecyclerAdapter(val infractionsList: MutableList<Models.Infraction>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(infractionsList.get(position))
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.recyclerview_item_row,
            viewGroup, false
        )
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return infractionsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        lateinit var infraction: Models.Infraction
        val speedSign = itemView.findViewById(R.id.speedSign) as TextView
        val date = itemView.findViewById(R.id.date_infraction) as TextView
        val time = itemView.findViewById(R.id.time_infraction) as TextView
        val your_speed = itemView.findViewById(R.id.your_speed) as TextView
        val titleInfraction = itemView.findViewById(R.id.titleInfraction) as TextView


        @SuppressLint("NewApi")
        internal fun bindModel(infraction: Models.Infraction) {
            this.infraction = infraction
            date.text = "le ${infraction.Date}"
            if (infraction.SpeedSign.toString().equals("0")) {
                titleInfraction.text = "Infraction de stop"
                speedSign.text = "قف"
            } else {
                titleInfraction.text = "Infraction de vitesse"
                speedSign.text = infraction.SpeedSign.toString()
            }
            time.text = infraction.Time.toString()
            your_speed.text = infraction.SpeedReal.toString() + " km/h"

        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {

            Toast.makeText(
                itemView.context,
                "pour payer cette infraction, vous devez aller auprès des guichets de la Trésorerie Générale du Royaume (TGR)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}