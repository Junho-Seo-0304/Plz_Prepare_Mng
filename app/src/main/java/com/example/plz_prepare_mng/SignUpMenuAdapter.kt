package com.example.plz_prepare_mng

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage

class SignUpMenuAdapter (val context : Context,val user : String, val menulist : ArrayList<Menu>):BaseAdapter(){
    // 메뉴 등록 액티비티에 현 메뉴를 리스트뷰에 연결해주는 어뎁터
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
        val view = convertView ?:LayoutInflater.from(context).inflate(R.layout.menu,parent,false) as View
        val storageRef = FirebaseStorage.getInstance().getReference(user+"/"+menulist[position].Fname.toString())
        GlideApp.with(view).load(storageRef).into(view.findViewById(R.id.imgMenu)) //GlideApp을 이용해 view에 있는 이미지뷰에 Firebase storage에 있는 이미지를 가져온다
        view.findViewById<TextView>(R.id.menuName).text = menulist[position].Fname.toString()
        view.findViewById<TextView>(R.id.menuPrice).text = menulist[position].Fprice.toString() + "원"
        view.findViewById<Button>(R.id.deleteBtn).setOnClickListener {
            menulist.removeAt(position)
            this.notifyDataSetChanged()
        }
        return view
    }
}