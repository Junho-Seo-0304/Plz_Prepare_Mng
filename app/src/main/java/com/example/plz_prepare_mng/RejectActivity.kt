package com.example.plz_prepare_mng

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RejectActivity : AppCompatActivity() {

    val order by lazy { intent.extras!!["Order"] as CustomerList }
    val category by lazy { intent.extras!!["Category"] as String }

    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    var complete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reject)
        mAuth=FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().reference.child("Users").child(category).child(mAuth.currentUser!!.uid)

        val reasonEdit = findViewById<EditText>(R.id.reasonEdit)
        val rejectBtn = findViewById<Button>(R.id.rejectBtn)

        rejectBtn.setOnClickListener {
            // 거절 이유를 입력하고 버튼을 누르면 거절 사유가 Firebase realtime database에 들어간다.
            database.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (!complete) {
                        val temp = p0.child("PermissionOrder").child(order.Cnumber.toString()).value
                        val reason = reasonEdit.text.toString()
                        val pushKey = p0.child("PermissionOrder").child(order.Cnumber.toString()).child("PushKey").value.toString()
                        database.child("PermissionOrder").child(order.Cnumber.toString())
                            .removeValue()
                        database.child("RejectedOrder").child(order.Cnumber.toString()).setValue(temp)
                        database.child("RejectedOrder").child(order.Cnumber.toString()).child("Reason").setValue(reason)
                        complete=true
                        Thread{
                            FCMRejectPush(pushKey).pushFCMNotification()
                        }.start()
                        finish()
                    }
                }
            })
        }
    }
}
