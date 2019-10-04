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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth= FirebaseAuth.getInstance()

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
            Log.d(TAG, "signIn:$email")

            firebaseAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = firebaseAuth!!.currentUser
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "이메일과 비밀번호를 확인해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // [START_EXCLUDE]
                    if (!task.isSuccessful) {
                    }
                    // [END_EXCLUDE]
                }
            // [END sign_in_with_email]

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}
