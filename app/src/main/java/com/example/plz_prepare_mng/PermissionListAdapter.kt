package com.example.plz_prepare_mng

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.permission_list.view.*

class PermissionListAdapter(val context:Context, var PermissionList : ArrayList<CustomerList>, val category : String) :BaseAdapter(){
    // UserMainActivity에서 수락을 받아야하는 주문들을 리스트뷰에 연결하는 어뎁터
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

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
        mAuth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference.child("Users").child(category).child(mAuth.currentUser!!.uid)
        view.Number.text=PermissionList[position].Cnumber.toString()
        view.OrderList.text=PermissionList[position].orderList
        view.getReadyBtn.setOnClickListener {
            // 리스트 뷰 안에 수락 버튼
            val intent = Intent(context,GetReadyActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("Order",PermissionList[position])
            intent.putExtra("Category",category)
            context.startActivity(intent)
            notifyDataSetChanged()
        }
        view.rejectBtn.setOnClickListener {
            // 리스트 뷰 안에 거절 버튼
            val intent = Intent(context,RejectActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("Order",PermissionList[position])
            intent.putExtra("Category",category)
            context.startActivity(intent)
            notifyDataSetChanged()
        }
        return view
    }
}