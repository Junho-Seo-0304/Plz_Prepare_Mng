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
        val email = findViewById<EditText>(R.id.emailEdit)
        val password = findViewById<EditText>(R.id.passwordEdit)
        val setBtn = findViewById<Button>(R.id.button)

        setBtn.setOnClickListener{
            signupEmail(email.text.toString(),password.text.toString())
        }
    }

    private fun signupEmail(email:String, password:String){ // 이메일로 회원가입을 하는 함수
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                // Firebase Auth의 createUserWithEmailAndPassword를 이용해 이메일과 비밀번호로 회원가입
                if (it.isSuccessful) {
                    val currentUser = mAuth.currentUser
                    if(currentUser != null) {
                        Toast.makeText(this, "이메일 회원가입 완료", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignUpActivity::class.java)
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
        // 회원가입 도중 뒤로가기를 누르면 현재까지 생성된 정보를 삭제한다.
        val currentUser = mAuth.currentUser
        if(currentUser != null) {
            currentUser.delete()
        }
        super.onBackPressed()
    }
}
