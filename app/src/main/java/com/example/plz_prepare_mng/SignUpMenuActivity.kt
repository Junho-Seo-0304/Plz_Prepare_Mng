package com.example.plz_prepare_mng

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignUpMenuActivity : AppCompatActivity() {

    var menuList = ArrayList<Menu>()
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
        val adapter = SignUpMenuAdapter(this,menuList)
        menuGrid.adapter = adapter
        menuGrid.setOnItemClickListener { parent, view, position, id ->
            val foodintent = Intent(this,SignUpMenu2Activity::class.java)
            foodintent.putExtra("Food",menuList[position])
            startActivityForResult(foodintent,1)
        }
        addMenuBtn.setOnClickListener {
            val menuintent = Intent(this,SignUpMenu2Activity::class.java)
            menuintent.putExtra("MenuList",menuList)
            startActivityForResult(menuintent,2)
        }

        finishBtn.setOnClickListener {

        }
    }
}
