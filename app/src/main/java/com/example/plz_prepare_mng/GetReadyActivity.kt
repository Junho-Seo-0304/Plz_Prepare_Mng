package com.example.plz_prepare_mng

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_get_ready.*

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

        var Hpicker = findViewById<NumberPicker>(R.id.HourPicker)
        var Mpicker = findViewById<NumberPicker>(R.id.MinPicker)
        var RBtn = findViewById<Button>(R.id.readyBtn)

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
            database.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
