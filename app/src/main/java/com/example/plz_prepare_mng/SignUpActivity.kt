package com.example.plz_prepare_mng

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseStorage:FirebaseStorage
    private lateinit var database: DatabaseReference

    val categoryList = arrayOf("한식","중식","일식","양식","패스트푸드","카페")
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001
    private val GET_LOCATION_CODE = 1002
    var category : String? = null
    var LX : Double = 0.00
    var LY : Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseStorage= FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().reference

        var emailEdit = findViewById<EditText>(R.id.emailEdit)
        var passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        var RnameEdit = findViewById<EditText>(R.id.RnameEdit)
        var cSpinner = findViewById<Spinner>(R.id.categorySpinner)
        var logoBtn = findViewById<ImageView>(R.id.imgBtn)
        var LButton = findViewById<Button>(R.id.locationChangeBtn)
        var finishButton =findViewById<Button>(R.id.menuBtn)

        locationText.text="위치는 " + LX.toString() +", " + LY.toString()

        logoBtn.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        LButton.setOnClickListener{
            var intent = Intent(this,MapsActivity::class.java)
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

            var email = emailEdit.text.toString()
            var password = passwordEdit.text.toString()
            var Rname = RnameEdit.text.toString()
            if(email.length<=0||password.length<=0||Rname.length<=0){
                Toast.makeText(baseContext,"빈칸을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else {
                database.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var child : Iterator<DataSnapshot> = p0.child("Users").children.iterator()
                        while (child.hasNext()){
                            if(child.next().key.equals(email)){
                                Toast.makeText(baseContext,"중복된 아이디가 존재합니다.",Toast.LENGTH_SHORT).show()
                                return
                            }
                        }
                        val restaurant = Restaurant(Rname, category, LX, LY, null)
                        database.child("Users").child(email).setValue(password)
                        database.child("Users").child(email).child(password).setValue(restaurant)
                        var intent = Intent(baseContext, SignUpMenuActivity::class.java)
                        intent.putExtra("User", email)
                        startActivity(intent)
                    }
                }
                )
            }
        }
    }

    fun setcategory(c : String){
        category=c
    }


    private fun uploadUri(file: Uri?){
        if (file != null) {
            firebaseStorage.reference.child(idEdit.text.toString()).child("logo").putFile(file)
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            uploadUri(data?.data)
            imgBtn.setImageURI(data?.data)
        }
        if(resultCode==GET_LOCATION_CODE){
            LX = data?.extras?.get("LX") as Double
            LY = data?.extras?.get("LY") as Double
        }
    }
}
