package com.example.plz_prepare_mng

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ChangeMenuAdapter (val context : Context, val user : String, val category : String, val menulist : ArrayList<Menu>, val numlist : ArrayList<Int>): BaseAdapter() {
    // 메뉴 수정 액티비티에 현 메뉴를 리스트뷰에 연결해주는 어뎁터
    private lateinit var database: DatabaseReference
    private lateinit var mAuth : FirebaseAuth

    override fun getCount(): Int {
        return menulist.size
    }

    override fun getItem(position: Int): Any {
        return menulist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.change_menu,parent,false) as View
        val storageRef = FirebaseStorage.getInstance().getReference(user+"/"+menulist[position].Fname.toString())
        GlideApp.with(view).load(storageRef).into(view.findViewById(R.id.imgFood)) //GlideApp을 이용해 view에 있는 이미지뷰에 Firebase storage에 있는 이미지를 가져온다
        database = FirebaseDatabase.getInstance().reference.child("Users").child(category).child(user).child("Menu").child(numlist[position].toString())
        mAuth = FirebaseAuth.getInstance()
        view.findViewById<TextView>(R.id.Fname).text=menulist[position].Fname
        view.findViewById<TextView>(R.id.Fprice).text=menulist[position].Fprice.toString()
        view.findViewById<ImageView>(R.id.settingBtn).setOnClickListener {
            val intent = Intent(context,ChangeMenu3Activity::class.java).addFlags(FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("Menu",menulist[position])
            intent.putExtra("Category",category)
            intent.putExtra("User",user)
            intent.putExtra("Num",numlist[position])
            context.startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.deleteBtn).setOnClickListener {
            database.removeValue()
        }
        return view
    }
}