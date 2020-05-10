package com.example.findphone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_trackers.*
import kotlinx.android.synthetic.main.contact_ticket.view.*

class Trackers : AppCompatActivity() {
    var adapter:ContactAdater?=null
    var listOfContact=ArrayList<UserContact>()
    var userData:UserData?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userData= UserData(applicationContext)
        setContentView(R.layout.activity_trackers)
        //dummyContacts()
        adapter= ContactAdater(this,listOfContact)
        lvUserContact.adapter=adapter
        lvUserContact.onItemClickListener= AdapterView.OnItemClickListener{
                parent,view,position,id ->

            val userInfo = listOfContact[position]
            UserData.myTrackers.remove(userInfo.phoneNumber)
            refreshData()

            //save to shared ref
            userData!!.saveContactInfo()
        }


        userData!!.loadContactInfo()
        refreshData()
    }

    fun dummyContacts(){
        listOfContact.add(UserContact("Adi","500400300"))
        listOfContact.add(UserContact("Elo","500400200"))
        listOfContact.add(UserContact("Moszna","500400100"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.tracker_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.finishActivity ->{
                finish()
            }
            R.id.addContact -> {

                checkPermission()
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }
    val CONTACT_CODE=123
    fun checkPermission(){

        if(Build.VERSION.SDK_INT>=23){

            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS),CONTACT_CODE)
                return
            }
        }
        pickContact()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CONTACT_CODE->{
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    pickContact()
                }else{
                    Toast.makeText(this,"Cannot acces contacts",Toast.LENGTH_SHORT).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }


    }

    val PICK_CODE = 111
    fun pickContact(){

        val intent = Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent,PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            PICK_CODE->{
                if(resultCode== Activity.RESULT_OK){
                    val contactData=data!!.data
                    val c= contentResolver.query(contactData!!,null,null,null,null)

                    if(c!!.moveToFirst()){
                        val id= c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                        val hasPhone= c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        if(hasPhone.equals("1")){
                            val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="+id,null,null
                            )
                            phones!!.moveToFirst()
                            val phoneNumber = phones.getString(phones.getColumnIndex("data1"))
                            val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                            UserData.myTrackers.put(phoneNumber,name)
                            refreshData()


                            userData!!.saveContactInfo()



                        }
                    }

                }
            }
            else->{
                super.onActivityResult(requestCode, resultCode, data)
            }
        }

    }

    fun refreshData(){

        listOfContact.clear()
        for((key,value) in UserData.myTrackers)
        {
            listOfContact.add(UserContact(value,key))
        }
        adapter!!.notifyDataSetChanged()
    }

    class ContactAdater:BaseAdapter {
        var listOfContact=ArrayList<UserContact>()
        var context:Context?=null
        constructor(context: Context,listOfContact:ArrayList<UserContact>){
        this.context = context
            this.listOfContact=listOfContact

        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val userContact = listOfContact[position]
            val inflator= context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val contactTicketView = inflator.inflate(R.layout.contact_ticket,null)
            contactTicketView.tvName.text = userContact.name
            contactTicketView.tvPhoneNumber.text = userContact.phoneNumber

            return contactTicketView

        }

        override fun getItem(position: Int): Any {
            return listOfContact[position]
        }

        override fun getItemId(position: Int): Long {
                return position.toLong()
        }

        override fun getCount(): Int {
            return listOfContact.size
        }


    }
}
