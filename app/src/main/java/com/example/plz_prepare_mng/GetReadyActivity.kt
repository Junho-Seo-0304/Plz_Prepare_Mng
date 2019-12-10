package com.example.plz_prepare_mng

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GetReadyActivity : AppCompatActivity() {

    val order by lazy { intent.extras!!["Order"] as CustomerList }
    val category by lazy { intent.extras!!["Category"] as String }
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    var hour = 0
    var minute = 0
    var complete = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_ready)
        mAuth=FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().reference.child("Users").child(category).child(mAuth.currentUser!!.uid)

        val Hpicker = findViewById<NumberPicker>(R.id.HourPicker)
        val Mpicker = findViewById<NumberPicker>(R.id.MinPicker)
        val RBtn = findViewById<Button>(R.id.readyBtn)

        Hpicker.minValue = 0
        Hpicker.maxValue = 23
        Mpicker.minValue = 0
        Mpicker.maxValue = 59

        Hpicker.setOnValueChangedListener { picker, oldVal, newVal ->
            hour=newVal
        }

        Mpicker.setOnValueChangedListener { picker, oldVal, newVal ->
            minute=newVal
        }

        RBtn.setOnClickListener {
            // 시간을 정하고 주문 수락 버튼을 누르면 Firebase에서 PermissionOrder에 있던 주문이 ReadyOrder로 바뀐다.
            database.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (!complete) {
                        val temp = p0.child("PermissionOrder").child(order.Cnumber.toString()).value
                        val pushKey = p0.child("PermissionOrder").child(order.Cnumber.toString()).child("PushKey").value.toString()
                        database.child("PermissionOrder").child(order.Cnumber.toString())
                            .removeValue()
                        database.child("ReadyOrder").child(order.Cnumber.toString()).setValue(temp)
                        database.child("ReadyOrder").child(order.Cnumber.toString()).child("Time")
                            .child("Hour").setValue(hour)
                        database.child("ReadyOrder").child(order.Cnumber.toString()).child("Time")
                            .child("Minute").setValue(minute)
                        complete=true
                        Thread{
                            FCMPush(pushKey,hour,minute).pushFCMNotification()
                        }.start()
                        finish()
                    }
                }
            })
        }
    }
}
