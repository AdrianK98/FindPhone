package com.example.findphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun buRegisterEvent(view: View){
        val UserData = UserData(this)
        UserData.savePhone(etNumber.text.toString())
        finish()
    }



}
