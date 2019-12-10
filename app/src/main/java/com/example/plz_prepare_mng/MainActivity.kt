package com.example.plz_prepare_mng

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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

        val edit_Email = findViewById<EditText>(R.id.idEdit)
        val edit_Password = findViewById<EditText>(R.id.passwordEdit)
        val signinBtn = findViewById<Button>(R.id.signinButton)
        val signupBtn = findViewById<Button>(R.id.signupButton)

        signinBtn.setOnClickListener{
            // sign in 버튼
            val getEmail = edit_Email.text.toString()
            val getPassword = edit_Password.text.toString()
            if(getEmail.length<=0||getPassword.length<=0){
                Toast.makeText(baseContext,"이메일과 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
            } else {
                signIn(idEdit.text.toString(), passwordEdit.text.toString())
            }
        }
        signupBtn.setOnClickListener {
            // sign up 버튼
            val intent = Intent(this,SignUpEmailActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signIn(email: String, password: String) {
        //Firebase Auth에 있는 signInWithEmailAndPassword를 이용하여 이메일과 비밀번호로 로그인을 하는 함수
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val currentUser = mAuth.currentUser
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
