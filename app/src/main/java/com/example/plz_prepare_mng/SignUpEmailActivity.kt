package com.example.plz_prepare_mng

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUpEmailActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_email)

        mAuth = FirebaseAuth.getInstance()
        var email = findViewById<EditText>(R.id.emailEdit)
        var password = findViewById<EditText>(R.id.passwordEdit)
        var setBtn = findViewById<Button>(R.id.button)

        setBtn.setOnClickListener{
            signInEmail(email.text.toString(),password.text.toString())
        }
    }

    private fun signInEmail(email:String, password:String){
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var currentUser = mAuth.currentUser
                    if(currentUser != null) {
                        Toast.makeText(this, "이메일 회원가입 완료", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this, SignUpActivity::class.java)
                        intent.putExtra("uid", currentUser.uid)
                        startActivity(intent)
                        finish()
                    }
                }
                else{
                    Toast.makeText(this,"이메일 회원가입 실패",Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onBackPressed() {
        var currentUser = mAuth.currentUser
        if(currentUser != null) {
            currentUser.delete()
        }
        super.onBackPressed()
    }
}
