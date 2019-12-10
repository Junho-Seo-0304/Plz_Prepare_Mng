package com.example.plz_prepare_mng

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up_menu2.*

class SignUpMenu2Activity : AppCompatActivity() {

    val user by lazy { intent.extras!!["User"] as String }
    private lateinit var firebaseStorage: FirebaseStorage

    private val IMAGE_PICK_CODE = 1000 // 갤러리를 열 때 request code
    private val PERMISSION_CODE = 1001 // 사진첩 접근하기 위한 permission의 승락이 되었는지 확인하기 위한 request code

    var imgUrl : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_menu2)
        firebaseStorage= FirebaseStorage.getInstance()
        val imgFoodView = findViewById<ImageView>(R.id.imgFood)
        val FnameEdit = findViewById<EditText>(R.id.FnameEdit)
        val FpriceEdit = findViewById<EditText>(R.id.FpriceEdit)
        val FexplainEdit = findViewById<EditText>(R.id.FexplainEdit)
        val setMenuBtn = findViewById<Button>(R.id.setMenuBtn)

        imgFoodView.setOnClickListener{
            // 음식 사진을 갤러리에서 불러오기 위한 버튼
            // 갤러리 접근을 위한 READ_EXTERNAL_STORAGE permission이 수락이 되있으면 갤러리에서 사진을 가져온다.
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                pickImageFromGallery()
            }
        }

        setMenuBtn.setOnClickListener{
            // 음식 등록를 위한 버튼
            if(FnameEdit.text.toString().isNotEmpty()&&FpriceEdit.text.toString().isNotEmpty()&&FexplainEdit.text.toString().isNotEmpty()) {
                val newMenu = Menu(
                    FnameEdit.text.toString(),
                    Integer.parseInt(FpriceEdit.text.toString()),
                    FexplainEdit.text.toString()
                )
                val menuListintent = Intent(this, SignUpMenuActivity::class.java)
                menuListintent.putExtra("NewMenu", newMenu)
                uploadUri(imgUrl, newMenu.Fname.toString())
                setResult(1, menuListintent)
                Handler().postDelayed({finish()},1000)
            } else{
                Toast.makeText(baseContext,"빈칸을 채워주세요.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadUri(file: Uri?,name : String){
        // 갤러리에서 고른 음식 사진을 Firebase storage에 저장하는 함수
        if (file != null) {
            firebaseStorage.reference.child(user).child(name).putFile(file)
        }
    }

    private fun pickImageFromGallery() {
        // 갤러리로 들어가 사진을 고르는 함수
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // permission 승락 여부를 받았을 때 처리하는 override 함수
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
            // 갤러리에서 사진을 가져왔으면 사진의 Url을 저장한다.
            imgUrl = data?.data
            imgFood.setImageURI(imgUrl)
        }
    }
}
