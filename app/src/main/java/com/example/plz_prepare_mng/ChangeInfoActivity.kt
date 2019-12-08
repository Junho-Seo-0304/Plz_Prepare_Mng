package com.example.plz_prepare_mng

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*

class ChangeInfoActivity: AppCompatActivity() {
    private lateinit var firebaseStorage:FirebaseStorage
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    val categoryList = arrayOf("한식","중식","일식","양식","패스트푸드","카페")
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001
    private val GET_LOCATION_CODE = 1002
    var imgUrl : Uri? = null
    val category by lazy { intent.extras!!["Category"] as String }
    var newCategory : String? = null
    var LX : Double = 0.00
    var LY : Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_info)

        firebaseStorage= FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        val RnameEdit = findViewById<EditText>(R.id.RnameEdit)
        val cSpinner = findViewById<Spinner>(R.id.categorySpinner)
        val logoBtn = findViewById<ImageView>(R.id.imgBtn)
        val LButton = findViewById<Button>(R.id.locationChangeBtn)
        val ChangeInfoButton =findViewById<Button>(R.id.ChangeInfoBtn)
        val DeleteButton = findViewById<Button>(R.id.DeleteRestBtn)
        val PnumEdit = findViewById<EditText>(R.id.phoneEdit)

        locationText.text="지도 위치 설정을 해주세요."

        logoBtn.setOnClickListener{
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                pickImageFromGallery()
            }
        }

        LButton.setOnClickListener{
            val intent = Intent(this,ChangeMapsActivity::class.java)
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


        ChangeInfoButton.setOnClickListener{

            val Rname = RnameEdit.text.toString()
            val Pnum = PnumEdit.text.toString()
            if(Rname.isEmpty()||imgUrl==null||Pnum.isEmpty()||LX==0.00||LY==0.00){
                Toast.makeText(baseContext,"빈칸을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else {
                val restaurant = Restaurant(Rname, LX, LY, Pnum,null)
                database.child("Users").child(newCategory!!).child(mAuth.currentUser!!.uid).setValue(restaurant)
                database.child("Users").child(newCategory!!).child(mAuth.currentUser!!.uid).child("MenuNum").setValue(0)
                database.child("Users").child(newCategory!!).child(mAuth.currentUser!!.uid).child("UsedNum").setValue(101)
                database.child("Users").child(category).child(mAuth.currentUser!!.uid).removeValue()
                uploadUri(imgUrl)
                val intent = Intent(baseContext, UserMainActivity::class.java)
                setResult(1,intent)
                Handler().postDelayed({finish()},1000)
            }
        }
        DeleteButton.setOnClickListener {
            database.child("Users").child(category).child(mAuth.currentUser!!.uid).removeValue()
            val storageRef = firebaseStorage.getReference(mAuth.currentUser!!.uid)
            storageRef.delete()
            val cUser = mAuth.currentUser
            cUser!!.delete().addOnSuccessListener {
                Toast.makeText(baseContext,"회원이 탈퇴되었습니다.",Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this,UserMainActivity::class.java)
            setResult(2,intent)
            finish()
        }
    }

    fun setcategory(c : String){
        newCategory=c
    }


    private fun uploadUri(file: Uri?){
        if (file != null) {
            firebaseStorage.reference.child(mAuth.currentUser!!.uid).child("logo").putFile(file)
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
                if (grantResults.size >0 && grantResults[0] ==
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
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imgUrl = data?.data
            imgBtn.setImageURI(imgUrl)
        }
        if(resultCode==GET_LOCATION_CODE){
            LX = data?.extras?.get("LX") as Double
            LY = data.extras?.get("LY") as Double
            locationText.text="위치는 " + LX.toString() +", " + LY.toString()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(baseContext, UserMainActivity::class.java)
        setResult(1,intent)
        super.onBackPressed()
    }
}
