package com.example.findphone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class UserData {
    var context:Context?=null
    var sharedPref:SharedPreferences?=null

    constructor(context: Context){
        this.context = context
        this.sharedPref = context.getSharedPreferences("UserData",Context.MODE_PRIVATE)

    }

    fun savePhone(phoneNum:String)
    {
        val editor = sharedPref!!.edit()
        editor.putString("phoneNumber",phoneNum)
        editor.commit()
    }

    fun loadPhoneNum():String{
        val phoneNum = sharedPref!!.getString("phoneNumber","empty")
        if(phoneNum.equals("empty")) {
            val intent = Intent(context,Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(intent)
        }
        return phoneNum!!
    }

    fun saveContactInfo(){
        var listOfTrackers=""
        for((key,value) in myTrackers)
        {
            if(listOfTrackers.length==0)
            {
                listOfTrackers= key+"%"+value
            }else
            {
                listOfTrackers+= key+"%"+value
            }

        }

        if (listOfTrackers.length==0)
        {
            listOfTrackers="empty"
        }

        val editor=sharedPref!!.edit()
        editor.putString("listOfTrackers",listOfTrackers)
        editor.commit()

    }

    fun loadContactInfo(){
        myTrackers.clear()
        val listOfTrackers = sharedPref!!.getString("listOfTrackers","empty")

        if (!listOfTrackers.equals("empty")){

            val userInfo=listOfTrackers!!.split("%").toTypedArray()
            var i=0
            while(i<userInfo.size){
                myTrackers.put(userInfo[i],userInfo[i+1])
                i+=2
            }
        }


    }

    companion object {

        var myTrackers:MutableMap<String,String> = HashMap()

    }

}