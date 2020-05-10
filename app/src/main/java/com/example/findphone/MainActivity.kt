package com.example.findphone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val UserData=UserData(this)
        UserData.loadPhoneNum()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addTracker ->{
                val intent = Intent(this,Trackers::class.java)
                startActivity(intent)
            }
            R.id.help -> {

                // ask for help
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }
}
