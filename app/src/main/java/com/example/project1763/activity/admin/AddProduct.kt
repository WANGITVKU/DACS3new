package com.example.project1763.activity.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log

import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide


import com.example.project1763.ViewModel.MainViewModel
import com.example.project1763.activity.BaseActivity
import com.example.project1763.databinding.AdminProductAddUpdateBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID


class AddProduct  : BaseActivity() {
    private lateinit var binding: AdminProductAddUpdateBinding
    private val viewModel = MainViewModel()
    private lateinit var database: DatabaseReference
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var storageReference: StorageReference
    private lateinit var firestore: FirebaseFirestore

    private var title: String = ""
    private var price: Int=0
    private var description: String = ""

    private var picUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminProductAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        binding.uploadButton.setOnClickListener {
            selectImage()
        }

        binding.applyChange.setOnClickListener {
            uploadImageToFirebase()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageUri = data?.data
            Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(binding.addProductImage)
        }
    }

    private fun uploadImageToFirebase() {
        if (imageUri != null) {
            val fileName = UUID.randomUUID().toString()
            val ref = storageReference.child("images/$fileName")

            ref.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        picUrl = downloadUrl // Lưu giá trị downloadUrl vào picUrl

                        // Sau khi có picUrl, bạn có thể lưu dữ liệu sản phẩm vào Realtime Database
                        saveProductToDatabase()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("Firebase", "Image upload failed", e)
                }
        } else {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProductToDatabase() {
        // Lấy các giá trị từ giao diện người dùng
        title = binding.productNameText.text.toString().trim()
        description = binding.addProductDescription.text.toString().trim()
        price = binding.productPriceEditText.text.toString().toInt()
        val defaultRating = 4.1
        val defaultSize = arrayListOf("38", "39", "40", "41", "42", "43", "44")

        if (description.isNotEmpty() && price > 10000 && picUrl.isNotEmpty()) {

            database.child("PruductId").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var currentOrderId = dataSnapshot.getValue(Int::class.java) ?: 0 // Nếu không có giá trị, sử dụng 0
                    currentOrderId++ // Tăng giá trị ID cho lần mua tiếp theo
                    val ordersRef = database.child("Items").child("$currentOrderId")
                    val orderItem = mapOf(
                        "description" to description,
                        "title" to title,
                        "price" to price,
                        "rating" to defaultRating,
                        "size" to defaultSize
                    )

                    val ordersRefurl = database.child("Items").child("$currentOrderId").child("picUrl").child("0")
                    ordersRef.setValue(orderItem)
                    ordersRefurl.setValue(picUrl)
                    // Lưu currentOrderId trở lại Firebase
                    database.child("PruductId").setValue(currentOrderId)
                        .addOnSuccessListener {
                            Toast.makeText(this@AddProduct, "Product saved successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@AddProduct, "Failed to save Product", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Xử lý lỗi nếu cần
                    Toast.makeText(this@AddProduct, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }}
