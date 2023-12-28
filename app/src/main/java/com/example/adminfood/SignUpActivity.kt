package com.example.adminfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.adminfood.databinding.ActivitySignUpBinding
import com.example.adminfood.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var userName: String
    private lateinit var nameOfRestaurent: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //FireBase Auth
        auth = Firebase.auth
        //Firebase database
        database = Firebase.database.reference




        binding.createButton.setOnClickListener {
            //get text from edittext
            userName = binding.name.text.toString().trim()
            nameOfRestaurent = binding.restorentName.text.toString().trim()
            email = binding.emailOrPhone.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (userName.isBlank() || nameOfRestaurent.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()

            } else{
                createAccount(email,password)
            }
        }
        binding.alrealyHaveAccountButton.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Tạo tài khoản thành công", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            else{
                Toast.makeText(this,"Tạo tài khoản không thành công", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Thất bại",task.exception)
            }
        }
    }
//save data in to database
    private fun saveUserData() {
        //get text from edittext
        userName = binding.name.text.toString().trim()
        nameOfRestaurent = binding.restorentName.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = UserModel(userName,nameOfRestaurent,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
    //save user data Firebase database
        database.child("user").child(userId).setValue(user)

    }
}