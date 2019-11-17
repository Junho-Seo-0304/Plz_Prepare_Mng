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
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

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
            val intent = Intent(this,SignUpEmailActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signIn(email: String, password: String) {
           mAuth.signInWithEmailAndPassword(email,password)
               .addOnCompleteListener {
                   if(it.isSuccessful){
                       var currentUser = mAuth.currentUser
                       if(currentUser!=null) {
                           val intent = Intent(this, UserMainActivity::class.java)
                           startActivity(intent)
                       }
                       else{
                           Toast.makeText(this,"아이디와 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show()
                       }
                   }
                   else{
                       Toast.makeText(this,"아이디와 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show()
                   }
               }
    }
}
