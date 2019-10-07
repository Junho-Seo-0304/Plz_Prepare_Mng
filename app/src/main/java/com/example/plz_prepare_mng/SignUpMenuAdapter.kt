package com.example.plz_prepare_mng

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.menu.view.*

class SignUpMenuAdapter (val context : Context, val foodslist : ArrayList<Menu>):BaseAdapter(){
    private lateinit var firebaseStorage: FirebaseStorage

    override fun getCount(): Int {
        return foodslist.size
    }

    override fun getItem(position: Int): Any {
        return foodslist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?:LayoutInflater.from(context).inflate(R.layout.menu,parent,false) as View
        view.imgFood.setImageURI(foodslist[position].imageUri)
        view.findViewById<TextView>(R.id.menuName).text = foodslist[position].Fname
        view.findViewById<TextView>(R.id.menuPrice).text = foodslist[position].Fprice.toString() + "원"
        view.findViewById<Button>(R.id.deleteBtn).setOnClickListener {
            firebaseStorage= FirebaseStorage.getInstance()
            foodslist.removeAt(position)
            this.notifyDataSetChanged()
        }
        return view
    }
}