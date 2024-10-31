package com.example.project1763.activity.login


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project1763.Helper.DataLogin
import com.example.project1763.activity.login.RegisterActivity
import com.example.project1763.activity.MainActivity
import com.example.project1763.activity.OrderManagement
import com.example.project1763.activity.admin.AdminDetailManagement
//import com.example.project1763.activity.admin.AdminDetailManagement
import com.example.project1763.activity.admin.AdminOrderManagement
import com.example.project1763.databinding.ActivityLoginformBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {
    var binding: ActivityLoginformBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginformBinding.inflate(layoutInflater)
        setContentView(binding?.getRoot())

        binding!!.loginButton.setOnClickListener {
            if (validateUsername() && validatePassword()) {
                checkUser()
            }
        }

        binding!!.signupRedirectText.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateUsername(): Boolean {
        val username = binding?.loginUsername?.text.toString()
        return if (username.isEmpty()) {
            binding?.loginUsername?.error = "Username cannot be empty"
            false
        } else {
            binding?.loginUsername?.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding?.loginPassword?.text.toString()
        return if (password.isEmpty()) {
            binding?.loginPassword?.error = "Password cannot be empty"
            false
        } else {
            binding?.loginUsername?.error = null
            true
        }
    }

    private fun checkUser() {
        val userUsername = binding?.loginUsername?.text.toString().trim()
        val userPassword = binding?.loginPassword?.text.toString().trim()
        if (userUsername == "admin" && userPassword == "123456") {
            val DataLogin = DataLogin(this@LoginActivity)
            intent.putExtra("fullname", userUsername)
            intent.putExtra("email", "")
            intent.putExtra("username", userUsername)
            intent.putExtra("password", userPassword)
            val intent = Intent(this@LoginActivity, AdminDetailManagement::class.java)
            startActivity(intent)
            return
        }
        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(userUsername)
        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding!!.loginUsername.error = null
                    val passwordFromDB = snapshot.child(userUsername).child("password").getValue(String::class.java)
                    if (passwordFromDB == userPassword) {
                        binding!!.loginUsername.error = null
                        val fullnameFromDB = snapshot.child(userUsername).child("fullname").getValue(String::class.java)
                        val emailFromDB = snapshot.child(userUsername).child("email").getValue(String::class.java)
                        val usernameFromDB = snapshot.child(userUsername).child("username").getValue(String::class.java)
                        val diachiFromDB = snapshot.child(userUsername).child("diachi").getValue(String::class.java)
                        val cityFromDB = snapshot.child(userUsername).child("city").getValue(String::class.java)
                        val tinhFromDB = snapshot.child(userUsername).child("tinh").getValue(String::class.java)
                        val sdtFromDB = snapshot.child(userUsername).child("sdt").getValue(String::class.java)

                        val DataLogin = DataLogin(this@LoginActivity)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("fullname", fullnameFromDB)
                        intent.putExtra("email", emailFromDB)
                        intent.putExtra("username", usernameFromDB)
                        intent.putExtra("password", passwordFromDB)
                        if (emailFromDB != null) {
                            if (fullnameFromDB != null) {
                                if (usernameFromDB != null) {
                                    DataLogin.saveUserData(fullnameFromDB, emailFromDB, usernameFromDB,
                                        diachiFromDB,cityFromDB,tinhFromDB,sdtFromDB)
                                }
                            }
                        }
                        startActivity(intent)
                    } else {
                        binding?.loginPassword?.error = "Invalid Credentials"
                        binding?.loginPassword?.requestFocus()
                    }
                } else {
                    binding?.loginUsername?.error = "User does not exist"
                    binding?.loginUsername?.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}