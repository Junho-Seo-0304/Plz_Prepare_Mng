package com.example.plz_prepare_mng

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseStorage:FirebaseStorage
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    val categoryList = arrayOf("한식","중식","일식","양식","패스트푸드","카페") // 스피터에 넣을 카테고리 리스트
    private val IMAGE_PICK_CODE = 1000 // 갤러리를 열 때 request code
    private val PERMISSION_CODE = 1001 // 사진첩 접근하기 위한 permission의 승락이 되었는지 확인하기 위한 request code
    private val GET_LOCATION_CODE = 1002 // 지도 액티비티에 대한 request code
    val user by lazy { intent.extras!!["uid"] as String }
    var imgUrl : Uri? = null
    var category : String = "Unknown"
    var LX : Double = 0.00
    var LY : Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseStorage= FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        val RnameEdit = findViewById<EditText>(R.id.RnameEdit)
        val cSpinner = findViewById<Spinner>(R.id.categorySpinner)
        val logoBtn = findViewById<ImageView>(R.id.imgBtn)
        val LButton = findViewById<Button>(R.id.locationChangeBtn)
        val finishButton =findViewById<Button>(R.id.menuBtn)
        val PnumEdit = findViewById<EditText>(R.id.phoneEdit)

        locationText.text="지도 위치 설정을 해주세요."

        logoBtn.setOnClickListener{
            // 로고 사진을 갤러리에서 불러오기 위한 버튼
            // 갤러리 접근을 위한 READ_EXTERNAL_STORAGE permission이 수락이 되있으면 갤러리에서 사진을 가져온다.
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                pickImageFromGallery()
            }
        }

        LButton.setOnClickListener{
            // 레스토랑 위치를 등록하는 버튼
            val intent = Intent(this,MapsActivity::class.java)
            startActivityForResult(intent,GET_LOCATION_CODE)
        }

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        cSpinner.adapter = adapter
        cSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setcategory(categoryList[position])
            }
        }


        finishButton.setOnClickListener{
            // 등록을 하기 위한 버튼
            val Rname = RnameEdit.text.toString()
            val Pnum = PnumEdit.text.toString()
            if(Rname.isEmpty()||imgUrl==null||Pnum.isEmpty()||LX==0.00||LY==0.00){
                Toast.makeText(baseContext,"빈칸을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else {
                val restaurant = Restaurant(Rname, LX, LY, Pnum,null)
                database.child("Users").child(category).child(user).setValue(restaurant)
                uploadUri(imgUrl)
                val intent = Intent(baseContext, SignUpMenuActivity::class.java)
                intent.putExtra("User", user)
                intent.putExtra("Category",category)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setcategory(c : String){
        // 스피너 안에서 바뀐 카테고리를 현재의 카테고리로 바꿔주는 함수
        category=c
    }

    private fun uploadUri(file: Uri?){
        // 갤러리에서 고른 로고를 Firebase storage에 저장하는 함수
        if (file != null) {
            firebaseStorage.reference.child(user).child("logo").putFile(file)
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
                if (grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
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
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            // 갤러리에서 사진을 가져왔으면 사진의 Url을 저장한다.
            imgUrl = data?.data
            imgBtn.setImageURI(imgUrl)
        }
        if(resultCode==GET_LOCATION_CODE){
            // 맵에서 위치 정보를 등록한 위도, 경도를 가져온다.
            LX = data?.extras?.get("LX") as Double
            LY = data?.extras?.get("LY") as Double
            locationText.text="위치는 " + LX.toString() +", " + LY.toString()
        }
    }
    override fun onBackPressed() {
        // 회원가입 도중 뒤로가기를 누르면 이메일 가입이 되어 있기 때문에 다시 회원가입을 하지 못하는 점이 생겨 현재까지 회원가입된 정보를 삭제
        val currentUser = mAuth.currentUser
        if(currentUser != null) {
            currentUser.delete()
        }
        super.onBackPressed()
    }
}
