package com.example.plz_prepare_mng

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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

        var menuGrid = findViewById<GridView>(R.id.menuGrid)
        var addMenuBtn = findViewById<Button>(R.id.addMenuBtn)
        var finishBtn = findViewById<Button>(R.id.finishBtn)
        val adapter = SignUpMenuAdapter(this,user,menuList)
        menuGrid.adapter = adapter
        addMenuBtn.setOnClickListener {
            val menuintent = Intent(this,SignUpMenu2Activity::class.java)
            menuintent.putExtra("User",user)
            startActivityForResult(menuintent,1)
        }

        finishBtn.setOnClickListener {
            database.child("Users").child(category).child(user).child("Menu").setValue(menuList)
                .addOnSuccessListener {
                    Toast.makeText(baseContext,"회원가입 완료!",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && resultCode == 1) {
            val newMenu = data.extras!!.get("NewMenu") as Menu
            menuList.add(newMenu)
            val adapter = SignUpMenuAdapter(this,user,menuList)
            menuGrid.adapter = adapter
        }
    }
}
