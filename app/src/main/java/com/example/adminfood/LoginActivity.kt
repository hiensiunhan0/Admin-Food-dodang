package com.example.adminfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.adminfood.databinding.ActivityLoginBinding
import com.example.adminfood.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    private lateinit var userName: String
    private lateinit var nameOfRestaurent: String
    private lateinit var email: String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        auth = Firebase.auth
        database = Firebase.database.reference

        binding.loginButton.setOnClickListener {
            //get text from edit text
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()


            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }else{
                createUserAccount(email,password)
            }
        }
        binding.dontHaveAccountButton.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val user = auth.currentUser
                Toast.makeText(this,"Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                updateUi(user)
            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { tasl ->
                   if (task.isSuccessful){
                       val user = auth.currentUser
                       Toast.makeText(this,"Tạo tài khoàn & đăng nhập thành công", Toast.LENGTH_SHORT).show()
                       saveUserData()
                       updateUi(user)
                   }else{
                       Toast.makeText(this,"Xác thực thất bại!", Toast.LENGTH_SHORT).show()
                       Log.d("Account", "createUserAccount: Xác thực thất bại", task.exception)
                   }
                }
            }
        }
    }

    private fun saveUserData() {
        //get text from edit text
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user = UserModel(email, password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }

    }

    private fun updateUi(user: FirebaseUser?) {
       startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}