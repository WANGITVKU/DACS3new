package com.example.project1763.activity

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1763.Helper.ChangeNumberItemsListener
import com.example.project1763.Helper.ManagmentCart
import com.example.project1763.Adapter.CartAdapter
import com.example.project1763.Helper.DataLogin
import com.example.project1763.Helper.UserFetcher
import com.example.project1763.databinding.ActivityCartBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart

    private lateinit var database: DatabaseReference
    private var total: Double = 0.0
    private var tax: Int = 0

    private var username: String = ""
    private var fullname: String = ""
    private var diachi: String = ""
    private var city: String = ""
    private var tinh: String = ""
    private var totalText: String = ""
    private var picUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
        managmentCart = ManagmentCart(this)
//        showAllUserData()
        buyIT()
        initBottomMenu()
        setVariable()
        initCartList()
        calculateCart()
        showAllUserData()
//        showCartItems()
    }
    fun showAllUserData() {
        val DataLogin = DataLogin(this)
        val userData = DataLogin.getUserData()

        val username = userData["username"]
        val userFetcher = UserFetcher()

        if (username != null) {
            userFetcher.fetchUserByUsername(username) { user ->
                if (user != null) {
                    fullname = user.fullname ?: "Unknown"
                    diachi = user.diachi ?: "Unknown"
                    city = user.city ?: "Unknown"
                    tinh = user.tinh ?: "Unknown"
                    // Xử lý thông tin user đã tìm được
                    binding.textView25.text = "${user.fullname}-${user.username} -${user.diachi} - ${user.city} - ${user.tinh}"
                } else {
                    // Xử lý trường hợp không tìm thấy user
                    binding.textView25.text = "Chưa có địa chỉ"
                }
            }
        }

    }
    private fun initBottomMenu() {
        binding.ConstraintLayout4.setOnClickListener {
            startActivity(
                Intent(
                    this@CartActivity,
                    AddressID::class.java
                )
            )
        }
    }
    private fun initCartList() {
        binding.viewCart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.viewCart.adapter =
            CartAdapter(managmentCart.getListCart(), this, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                }
            })

        with(binding) {
            emptyTxt.visibility =
                if (managmentCart.getListCart().isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility =
                if (managmentCart.getListCart().isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun calculateCart() {
//        val percentTax = 0.02
        val delivery = 10.0
//        tax = Math.round((managmentCart.getTotalFee() * percentTax) * 100) / 100.0
        val total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100
        val itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100
        totalText = "$total"
        with(binding) {
            totalFeeTxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }
    private fun buyIT() {
        binding.btnBuy.setOnClickListener {
            val cartItems = managmentCart.getListCart()

            // Lấy currentOrderId từ Firebase
            val dateFormat = SimpleDateFormat("mm:HH dd-MM-yyy ", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
            val currentDateTime = dateFormat.format(Date())
            database.child("currentOrderId").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var currentOrderId = dataSnapshot.getValue(Int::class.java) ?: 0 // Nếu không có giá trị, sử dụng 0
                    currentOrderId++ // Tăng giá trị ID cho lần mua tiếp theo
                    val ordersRef = database.child("donhang").child("$currentOrderId")
                        val orderItem = mapOf(
                            "fullname" to fullname,
                            "diachi" to diachi,
                            "city" to city,
                            "Total" to totalText,
                            "currentOrderId" to currentOrderId,
                            "timestamp" to currentDateTime,
                            // Thêm timestamp


                        )
                        ordersRef.setValue(orderItem)

                    var itemIndex = 1
                    for (item in cartItems) {
                        val picUrl = (item.picUrl as? List<*>)?.getOrNull(0)?.toString() ?: ""

                        val chitiet = mapOf(
                            "title" to item.title,
                            "price" to item.price,
                            "numberInCart" to item.numberInCart,
                            "size" to item.selectedSize,
                            "img" to picUrl
                        )

                        ordersRef.child("chitiet").child("item_$itemIndex").setValue(chitiet)
                            .addOnSuccessListener {
                                Toast.makeText(this@CartActivity, "Order placed successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@CartActivity, "Failed to place order", Toast.LENGTH_SHORT).show()
                            }
                        itemIndex++
                    }
                    // Lưu currentOrderId trở lại Firebase
                    database.child("currentOrderId").setValue(currentOrderId)
                        .addOnSuccessListener {
                            Toast.makeText(this@CartActivity, "Order ID saved successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@CartActivity    , "Failed to save Order ID", Toast.LENGTH_SHORT).show()
                        }
                    database.child("truycuudonghang").child("$currentOrderId").setValue(fullname)
                        .addOnSuccessListener {
                            Toast.makeText(this@CartActivity, "Order ID saved successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@CartActivity    , "Failed to save Order ID", Toast.LENGTH_SHORT).show()
                         }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Xử lý lỗi nếu cần
                }
            })
        }
    }


    private fun showCartItems() {
        val cartItems = managmentCart.getListCart()
        val stringBuilder = StringBuilder()
        for (item in cartItems) {
            // Lấy thông tin từ mỗi đối tượng ItemsModel
            val title = item.title
            val price = item.price
            val numberInCart = item.numberInCart
            val size = item.selectedSize
//            val picUrl = item.picUrl.joinToString(", ") // Để nối nhiều URL hình ảnh thành một chuỗi

            // Tạo một chuỗi chứa thông tin của mỗi mục
//            val itemInfo = "Title: $title\nPrice: $$price\nQuantity: $numberInCart\nImage URLs: $picUrl\n\n"
            val itemInfo = "Title: $title\nPrice: $price\nQuantity: $numberInCart\n Size: $size\n"

            // Thêm thông tin của mỗi mục vào chuỗi chính
            stringBuilder.append(itemInfo)
        }
        // Hiển thị chuỗi chứa thông tin của tất cả các mục trên TextView
        binding.textView2.text = stringBuilder.toString()
    }


}