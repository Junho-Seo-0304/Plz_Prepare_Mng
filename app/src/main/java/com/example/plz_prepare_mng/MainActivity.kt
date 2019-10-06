package com.example.plz_prepare_mng

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance().reference

        var edit_Email = findViewById<EditText>(R.id.idEdit)
        var edit_Password = findViewById<EditText>(R.id.passwordEdit)
        var signinBtn = findViewById<Button>(R.id.signinButton)
        var signupBtn = findViewById<Button>(R.id.signupButton)

        signinBtn.setOnClickListener{
            var getEmail = edit_Email.text.toString()
            var getPassword = edit_Password.text.toString()
            if(getEmail.length<=0||getPassword.length<=0){
                Toast.makeText(baseContext,"이메일과 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
            } else {
                signIn(idEdit.text.toString(), passwordEdit.text.toString())
            }
        }
        signupBtn.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivityForResult(intent,1)
        }
    }
    private fun signIn(email: String, password: String) {
           database.addListenerForSingleValueEvent(object : ValueEventListener{
               override fun onCancelled(p0: DatabaseError) {
                   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
               }

               override fun onDataChange(p0: DataSnapshot) {
                   var child : Iterator<DataSnapshot> = p0.child("Users").children.iterator()
                   while (child.hasNext()){
                       if(child.next().key.equals(idEdit.text.toString())
                           &&child.next().child(idEdit.text.toString()).key.equals(passwordEdit.text.toString())){
                           Toast.makeText(baseContext,"성공",Toast.LENGTH_SHORT).show()
                       }
                   }
               }
           }
           )

    }
}
