package com.example.plz_prepare_mng

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.drawToBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.menu.view.*
import java.io.InputStream

class SignUpMenuAdapter (val context : Context,val user : String, val menulist : ArrayList<Menu>):BaseAdapter(){

    private lateinit var firebaseStorage: FirebaseStorage
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
        var storageRef = FirebaseStorage.getInstance().getReference(user+"/"+menulist[position].Fname.toString())
        GlideApp.with(view).load(storageRef).into(view.findViewById(R.id.imgMenu))
        view.findViewById<TextView>(R.id.menuName).text = menulist[position].Fname.toString()
        view.findViewById<TextView>(R.id.menuPrice).text = menulist[position].Fprice.toString() + "원"
        view.findViewById<Button>(R.id.deleteBtn).setOnClickListener {
            menulist.removeAt(position)
            this.notifyDataSetChanged()
        }
        return view
    }
}