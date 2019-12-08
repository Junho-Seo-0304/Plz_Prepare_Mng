package com.example.plz_prepare_mng

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up_menu2.*

class ChangeMenu2Activity : AppCompatActivity() {

    val user by lazy { intent.extras!!["User"] as String }
    val category by lazy { intent.extras!!["Category"] as String }
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var database : DatabaseReference
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001

    var imgUrl : Uri? = null
    var complete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_menu2)
        firebaseStorage= FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users").child(category).child(user)
        val imgFoodView = findViewById<ImageView>(R.id.imgFood)
        val FnameEdit = findViewById<EditText>(R.id.FnameEdit)
        val FpriceEdit = findViewById<EditText>(R.id.FpriceEdit)
        val FexplainEdit = findViewById<EditText>(R.id.FexplainEdit)
        val setMenuBtn = findViewById<Button>(R.id.setMenuBtn)

        imgFoodView.setOnClickListener{
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                pickImageFromGallery()
            }
        }

        setMenuBtn.setOnClickListener{
            if(FnameEdit.text.toString().isNotEmpty()&&FpriceEdit.text.toString().isNotEmpty()&&FexplainEdit.text.toString().isNotEmpty()) {
                val newMenu = Menu(
                    FnameEdit.text.toString(),
                    Integer.parseInt(FpriceEdit.text.toString()),
                    FexplainEdit.text.toString()
                )
                database.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if(!complete) {
                            var num = Integer.parseInt(p0.child("MenuNum").value.toString())
                            database.child("Menu").child(num.toString()).setValue(newMenu)
                            num++
                            database.child("MenuNum").setValue(num)
                            complete=true
                        }
                    }
                })
                uploadUri(imgUrl, newMenu.Fname.toString())
                finish()
            } else {
                Toast.makeText(baseContext,"빈칸을 채워주세요.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadUri(file: Uri?, name : String){
        if (file != null) {
            firebaseStorage.reference.child(user).child(name).putFile(file)
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imgUrl = data?.data
            imgFood.setImageURI(imgUrl)
        }
    }
}
