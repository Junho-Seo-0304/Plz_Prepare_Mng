package com.example.plz_prepare_mng

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up_menu.*

class ChangeMenuActivity : AppCompatActivity() {

    var menuList = ArrayList<Menu>()
    var numList = ArrayList<Int>()
    val category by lazy { intent.extras!!["Category"] as String }
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var database: DatabaseReference
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_menu)

        firebaseStorage= FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference.child("Users").child(category).child(user)
        var menuGrid = findViewById<GridView>(R.id.menuGrid)
        var addMenuBtn = findViewById<Button>(R.id.addMenuBtn)
        var finishBtn = findViewById<Button>(R.id.finishBtn)

        database.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                menuList.clear()
                numList.clear()
                for (i in 0 until Integer.parseInt(p0.child("MenuNum").value.toString())){
                    if (p0.child("Menu").hasChild(i.toString())) {
                        val fexplain =
                            p0.child("Menu").child((i).toString()).child("fexplain").value.toString()
                        val fname = p0.child("Menu").child((i).toString()).child("fname").value.toString()
                        val fprice =
                            Integer.parseInt(p0.child("Menu").child((i).toString()).child("fprice").value.toString())
                        menuList.add(Menu(fname, fprice, fexplain))
                        numList.add(i)
                    }
                }
                val adapter = ChangeMenuAdapter(baseContext,user,category,menuList,numList)
                menuGrid.adapter = adapter
            }
        })
        addMenuBtn.setOnClickListener {
            val menuintent = Intent(this,ChangeMenu2Activity::class.java)
            menuintent.putExtra("User",user)
            menuintent.putExtra("Category",category)
            startActivity(menuintent)
        }

        finishBtn.setOnClickListener {
            Toast.makeText(baseContext,"메뉴가 수정되었습니다.",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
