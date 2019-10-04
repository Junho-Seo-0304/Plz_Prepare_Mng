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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseStorage:FirebaseStorage
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    var user : String? = null
    val categoryList = arrayOf("한식","중식","일식","양식","패스트푸드","카페")
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001
    private val GET_LOCATION_CODE = 1002
    var category : String? = null
    var LX : Long = 0
    var LY : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseStorage= FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().reference
        firebaseAuth= FirebaseAuth.getInstance()

        var emailEdit = findViewById<EditText>(R.id.emailEdit)
        var passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        var RnameEdit = findViewById<EditText>(R.id.RnameEdit)
        var cSpinner = findViewById<Spinner>(R.id.categorySpinner)
        var logoBtn = findViewById<ImageView>(R.id.imgBtn)
        var LButton = findViewById<Button>(R.id.locationChangeBtn)
        var finishButton =findViewById<Button>(R.id.finishBtn)
        var email = emailEdit.text.toString()
        user = email.toString()
        var password = passwordEdit.text.toString()
        var Rname = RnameEdit.text.toString()

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
            intent.putExtra("LX",LX)
            intent.putExtra("LY",LY)
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
                category=categoryList[position]
            }
        }

        finishButton.setOnClickListener{
            val restaurant = Restaurant(Rname, category, LX, LY, null)
            database!!.child("User").child(email).setValue(restaurant)
            firebaseAuth!!.createUserWithEmailAndPassword(email,password)
            var intent = Intent(this,SignUpMenuActivity::class.java)
            intent.putExtra("User",email)
            startActivity(intent)
        }
    }


    private fun uploadUri(file: Uri?){
        if (file != null) {
            firebaseStorage.reference.child(user.toString()).child("logo").putFile(file)
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
        if(requestCode==GET_LOCATION_CODE){
            LX = data?.extras?.get("LX") as Long
            LY = data?.extras?.get("LY") as Long
        }
    }
}
