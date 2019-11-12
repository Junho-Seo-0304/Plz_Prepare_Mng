package com.example.plz_prepare_mng

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.permission_list.view.*

class PermissionListAdapter(val context:Context, var PermissionList : ArrayList<CustomerList>) :BaseAdapter(){

    override fun getCount(): Int {
        return PermissionList.size
    }

    override fun getItem(position: Int): Any {
        return PermissionList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.permission_list,parent,false) as View
        view.Number.text=PermissionList[position].Cnumber.toString()
        view.OrderList.text=PermissionList[position].orderList
        view.getReadyBtn.setOnClickListener {

        }
        view.rejectBtn.setOnClickListener {

        }
        return view
    }
}