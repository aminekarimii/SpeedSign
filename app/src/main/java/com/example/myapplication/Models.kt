package com.example.myapplication

import com.google.firebase.database.Exclude
import java.util.*

class Models (){
    data class User(var email:String, var Mat:String, var type:String)
    data class Infraction(var Date:String?,
                          var Paid:Boolean?,
                          var SpeedReal:Int?,
                          var SpeedSign:Int?,
                          var Time:String?,
                          var UMat:String?
    ){
        constructor(): this("", false, 0, 0, "", ""){}
    }
}