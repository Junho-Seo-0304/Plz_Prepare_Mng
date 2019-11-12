package com.example.plz_prepare_mng

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class UserMainActivity : AppCompatActivity() {

    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    var PermissionList = arrayListOf<CustomerList>()
    var ReadyList = arrayListOf<CustomerList>()
    var category : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)
        database = FirebaseDatabase.getInstance().reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        var PListView = findViewById<ListView>(R.id.waitingPermissionList)
        var RListView = findViewById<ListView>(R.id.waitingReadyList)

        searchCategory()
        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(category!!).hasChild(mAuth.currentUser!!.uid)){
                    PermissionList.clear()
                    ReadyList.clear()
                    for (i in 101 until Integer.parseInt(p0.child(category!!).child(mAuth.currentUser!!.uid).child("UsedNum").value.toString())){
                        if (p0.child(category!!).child(mAuth.currentUser!!.uid).child("PermissionOrder").hasChild(i.toString())){
                            val num = i
                            var totalString = ""
                            for (j in 1 until p0.child(category!!).child(mAuth.currentUser!!.uid).child("PermissionOrder").child(i.toString()).childrenCount) {
                                totalString += p0.child(category!!).child(mAuth.currentUser!!.uid).child("PermissionOrder").child(i.toString()).child((j - 1).toString()).child("food").child("fname").value.toString() + " : " +p0.child(category!!).child(mAuth.currentUser!!.uid).child("PermissionOrder").child(i.toString()).child((j - 1).toString()).child("num").value.toString() + "\n"
                            }
                            PermissionList.add(CustomerList(num,totalString))
                        }
                    }
                    PListView.adapter=PermissionListAdapter(baseContext,PermissionList)
                    for (i in 101 until Integer.parseInt(p0.child(category!!).child(mAuth.currentUser!!.uid).child("UsedNum").value.toString())){
                        if (p0.child(category!!).child(mAuth.currentUser!!.uid).child("ReadyOrder").hasChild(i.toString())){
                            val num = i
                            var totalString = ""
                            for (j in 1 until p0.child(category!!).child(mAuth.currentUser!!.uid).child("ReadyOrder").child(i.toString()).childrenCount) {
                                totalString += p0.child(category!!).child(mAuth.currentUser!!.uid).child("ReadyOrder").child(i.toString()).child((j - 1).toString()).child("food").child("fname").value.toString() + " : " +p0.child(category!!).child(mAuth.currentUser!!.uid).child("ReadyOrder").child(i.toString()).child((j - 1).toString()).child("num").value.toString() + "\n"
                            }
                            ReadyList.add(CustomerList(num,totalString))
                        }
                    }
                    RListView.adapter=ReadyListAdapter(baseContext,ReadyList)
                }
            }
        })
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
}
