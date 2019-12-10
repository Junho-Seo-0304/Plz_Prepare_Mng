package com.example.plz_prepare_mng

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.ready_list.view.*

class ReadyListAdapter(val context: Context, var ReadyList : ArrayList<CustomerList> ,val category: String) : BaseAdapter(){
    // UserMainActivity에서 수락을 받은 주문들을 리스트뷰에 연결하는 어뎁터
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun getCount(): Int {
        return ReadyList.size
    }

    override fun getItem(position: Int): Any {
        return ReadyList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.ready_list,parent,false) as View
        mAuth=FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().reference.child("Users").child(category).child(mAuth.currentUser!!.uid)
        view.Number.text=ReadyList[position].Cnumber.toString()
        view.OrderList.text=ReadyList[position].orderList
        view.FinishBtn.setOnClickListener {
            // 완료된 주문을 삭제하는 버튼
            database.child("ReadyOrder").child(ReadyList[position].Cnumber.toString()).removeValue()
        }
        return view
    }
}