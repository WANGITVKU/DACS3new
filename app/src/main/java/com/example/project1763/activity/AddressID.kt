package com.example.project1763.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1763.Helper.ChangeNumberItemsListener
import com.example.project1763.Helper.ManagmentCart
import com.example.project1763.Adapter.CartAdapter
import com.example.project1763.Helper.DataLogin
import com.example.project1763.databinding.ActivityAddressIdBinding
import com.example.project1763.databinding.ActivityCartBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddressID : BaseActivity() {

    private lateinit var binding: ActivityAddressIdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddressIdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showAllUserData()
        setVariable()
        addAddress()
        uploadDB()
    }

    fun showAllUserData() {
        val DataLogin = DataLogin(this)
        val userData = DataLogin.getUserData()

        val fullname = userData["fullname"]
        val email = userData["email"]
        val username  = userData["username"]
        val diachi  = userData["diachi"]
        val city  = userData["city"]
        val tinh  = userData["tinh"]
        val sdt  = userData["sdt"]
        binding.fullname.setText( fullname)
        binding.diachi.setText( diachi)
        binding.tinh.setText( tinh)
        binding.city.setText(city)
        binding.sdt.setText( sdt)
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }
    private fun addAddress() {
        binding.buttonSaveAddress.setOnClickListener {
            val fullname = binding.fullname.text.toString()
            val diachi = binding.diachi.text.toString()
            val tinh = binding.tinh.text.toString()
            val city = binding.city.text.toString()
            val sdt = binding.sdt.text.toString()

            // Kiểm tra xem các trường dữ liệu có trống không
            if (fullname.isEmpty() || diachi.isEmpty() || tinh.isEmpty() || city.isEmpty() || sdt.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val DataLogin = DataLogin(this)
            val userData = DataLogin.getUserData()
            val email = userData["email"]
            val username  = userData["username"]

            val reference = FirebaseDatabase.getInstance().getReference("users")

            // Tạo một Map để lưu trữ dữ liệu mới
            val newAddressMap = hashMapOf<String, Any>(
                "fullname" to fullname,
                "diachi" to diachi,
                "tinh" to tinh,
                "city" to city,
                "sdt" to sdt
            )

            // Cập nhật dữ liệu mới vào Firebase với key là username

                reference.child(username!!).updateChildren(newAddressMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Đã cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show()

                        // Xóa nội dung trong các EditText sau khi cập nhật thành công (nếu cần)
                        binding.fullname.text.clear()
                        binding.diachi.text.clear()
                        binding.tinh.text.clear()
                        binding.city.text.clear()
                        binding.sdt.text.clear()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

    private fun uploadDB() {

        val DataLogin = DataLogin(this)
        val userData = DataLogin.getUserData()
        val userUsername = userData["username"] // Thay bằng username của người dùng hiện tại
        val reference = FirebaseDatabase.getInstance().getReference("users")
        val userQuery = reference.orderByChild("username").equalTo(userUsername)
        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userSnapshot = snapshot.child(userUsername!!)
                    binding.fullname.setText(userSnapshot.child("fullname").getValue(String::class.java))
                    binding.diachi.setText(userSnapshot.child("diachi").getValue(String::class.java) ?: "")
                    binding.city.setText(userSnapshot.child("city").getValue(String::class.java) ?: "")
                    binding.tinh.setText(userSnapshot.child("tinh").getValue(String::class.java) ?: "")
                    binding.sdt.setText(userSnapshot.child("sdt").getValue(String::class.java) ?: "")
                } else {
                    // Xử lý trường hợp không tìm thấy người dùng
                    Toast.makeText(this@AddressID, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(this@AddressID, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}