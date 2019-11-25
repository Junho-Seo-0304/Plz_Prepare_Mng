package com.example.plz_prepare_mng

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class UserMainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    var PermissionList = arrayListOf<CustomerList>()
    var ReadyList = arrayListOf<CustomerList>()
    var category : String? = null
    var RestaurantChanged = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)
        database = FirebaseDatabase.getInstance().reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        var Rname = findViewById<TextView>(R.id.RnameText)
        var PListView = findViewById<ListView>(R.id.waitingPermissionList)
        var RListView = findViewById<ListView>(R.id.waitingReadyList)
        var ChangeInfo = findViewById<ImageView>(R.id.changeRestInfo)
        var ChangeMenu = findViewById<Button>(R.id.changeMenuBtn)

        searchCategory()

        if(!RestaurantChanged) {
            database.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.child(category!!).hasChild(mAuth.currentUser!!.uid)) {
                        Rname.text =
                            p0.child(category!!).child(mAuth.currentUser!!.uid).child("rname")
                                .value.toString()
                        PermissionList.clear()
                        ReadyList.clear()
                        for (i in 101..Integer.parseInt(
                            p0.child(category!!).child(mAuth.currentUser!!.uid).child(
                                "UsedNum"
                            ).value.toString()
                        )) {
                            if (p0.child(category!!).child(mAuth.currentUser!!.uid).child("PermissionOrder").hasChild(
                                    i.toString()
                                )
                            ) {
                                val num = i
                                var totalString = ""
                                for (j in 1 until p0.child(category!!).child(mAuth.currentUser!!.uid).child(
                                    "PermissionOrder"
                                ).child(i.toString()).childrenCount-1) {
                                    totalString += p0.child(category!!).child(mAuth.currentUser!!.uid).child(
                                        "PermissionOrder"
                                    ).child(i.toString()).child((j - 1).toString()).child("food").child(
                                        "fname"
                                    ).value.toString() + " : " + p0.child(category!!).child(mAuth.currentUser!!.uid).child(
                                        "PermissionOrder"
                                    ).child(i.toString()).child((j - 1).toString()).child("num").value.toString() + "\n"
                                }
                                PermissionList.add(CustomerList(num, totalString))
                            }
                        }
                        PListView.adapter =
                            PermissionListAdapter(baseContext, PermissionList, category!!)
                        for (i in 101..Integer.parseInt(
                            p0.child(category!!).child(mAuth.currentUser!!.uid).child(
                                "UsedNum"
                            ).value.toString()
                        )) {
                            if (p0.child(category!!).child(mAuth.currentUser!!.uid).child("ReadyOrder").hasChild(
                                    i.toString()
                                )
                            ) {
                                val num = i
                                var totalString = ""
                                for (j in 1 until p0.child(category!!).child(mAuth.currentUser!!.uid).child(
                                    "ReadyOrder"
                                ).child(i.toString()).childrenCount - 2) {
                                    totalString += p0.child(category!!).child(mAuth.currentUser!!.uid).child(
                                        "ReadyOrder"
                                    ).child(i.toString()).child((j - 1).toString()).child("food").child(
                                        "fname"
                                    ).value.toString() + " : " + p0.child(category!!).child(mAuth.currentUser!!.uid).child(
                                        "ReadyOrder"
                                    ).child(i.toString()).child((j - 1).toString()).child("num").value.toString() + "\n"
                                }
                                ReadyList.add(CustomerList(num, totalString))
                            }
                        }
                        RListView.adapter = ReadyListAdapter(baseContext, ReadyList, category!!)
                    }
                }
            })
        }
        ChangeInfo.setOnClickListener {
            RestaurantChanged=true
            val intent = Intent(this,ChangeInfoActivity::class.java)
            intent.putExtra("Category",category)
            startActivityForResult(intent,1)
        }
        ChangeMenu.setOnClickListener {
            val intent = Intent(this,ChangeMenuActivity::class.java)
            intent.putExtra("Category",category)
            startActivityForResult(intent,2)
        }
    }

    fun searchCategory(){
        database = FirebaseDatabase.getInstance().reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.child("한식").hasChild(mAuth.currentUser!!.uid)){
                    category="한식"
                }
                if(p0.child("중식").hasChild(mAuth.currentUser!!.uid)){
                    category="중식"
                }
                if(p0.child("양식").hasChild(mAuth.currentUser!!.uid)){
                    category="양식"
                }
                if(p0.child("일식").hasChild(mAuth.currentUser!!.uid)){
                    category="일식"
                }
                if(p0.child("패스트푸드").hasChild(mAuth.currentUser!!.uid)){
                    category="패스트푸드"
                }
                if(p0.child("카페").hasChild(mAuth.currentUser!!.uid)){
                    category="카페"
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1&&resultCode==1&&data!=null){
            searchCategory()
            RestaurantChanged=false
            finish()
        }
        if (requestCode==1&&resultCode==2){
            finish()
        }
    }
}
