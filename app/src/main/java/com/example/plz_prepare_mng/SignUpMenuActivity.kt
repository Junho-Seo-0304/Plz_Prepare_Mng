package com.example.plz_prepare_mng

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up_menu.*

class SignUpMenuActivity : AppCompatActivity() {

    var menuList = ArrayList<Menu>()
    val user by lazy { intent.extras!!["User"] as String }
    val category by lazy { intent.extras!!["Category"] as String }
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_menu)

        firebaseStorage= FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val menuGrid = findViewById<GridView>(R.id.menuGrid)
        val addMenuBtn = findViewById<Button>(R.id.addMenuBtn)
        val finishBtn = findViewById<Button>(R.id.finishBtn)
        val adapter = SignUpMenuAdapter(this,user,menuList)
        menuGrid.adapter = adapter

        addMenuBtn.setOnClickListener {
            // 메뉴 추가 버튼
            val menuintent = Intent(this,SignUpMenu2Activity::class.java)
            menuintent.putExtra("User",user)
            startActivityForResult(menuintent,1)
        }

        finishBtn.setOnClickListener {
            // 메뉴 등록 완료 버튼
            database.child("Users").child(category).child(user).child("MenuNum").setValue(menuList.size)
            database.child("Users").child(category).child(user).child("UsedNum").setValue(101)
            database.child("Users").child(category).child(user).child("Menu").setValue(menuList)
                .addOnSuccessListener {
                    Toast.makeText(baseContext,"회원가입 완료!",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == 1) {
            val newMenu = data.extras!!.get("NewMenu") as Menu
            menuList.add(newMenu)
            val adapter = SignUpMenuAdapter(this,user,menuList)
            menuGrid.adapter = adapter
        }
    }
}
