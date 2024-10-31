package com.example.project1763.activity



import com.example.project1763.Model.OrderModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1763.Adapter.OrderDetailAdapter


//import com.example.project1763.Adapter.OrderDetailAdpter
import com.example.project1763.Helper.DataLogin
import com.example.project1763.ViewModel.MainViewModel
import com.example.project1763.databinding.OrderDetailBinding
class OrderDetail : BaseActivity() {

    private lateinit var binding: OrderDetailBinding
    private val viewModel = MainViewModel()
    private lateinit var database: DatabaseReference
    private lateinit var selectedItem: OrderModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
        setVariable()
//        initDetailOrder()
        displaySelectedItemDetails()

    }

//    private fun initDetailOrder() {
//        val DataLogin = DataLogin(this)
//        val userData = DataLogin.getUserData()
//        val fullname = userData["fullname"]
//        if (fullname != null) {
//            viewModel.loadOrder(fullname)
//            Log.d("TAG11",fullname.toString())
//        }
//
//        binding.progressBarOrder1.visibility = View.VISIBLE
//        viewModel.orderDetail.observe(this, Observer { orderList ->
//            Log.d("TAG911", "Received order data: $orderList")
//            binding.ViewOrder1.layoutManager = LinearLayoutManager(this@OrderDetail)
//            binding.ViewOrder1.adapter = OrderDetailAdapter(orderList.toMutableList())
//            binding.progressBarOrder1.visibility = View.GONE
//
//        })
//
//    }
    private fun displaySelectedItemDetails() {
        // Sử dụng đối tượng selectedItem để khởi tạo adapter
        val selectedItem = intent.getParcelableExtra<OrderModel>("selectedItem") ?: return
        Log.d("TAG9111", selectedItem.toString(),)
        val itemList = selectedItem.items.toMutableList()

        Log.d("TAG911", "Received order data: $itemList")// Giả sử OrderModel có một danh sách items
        val adapter = OrderDetailAdapter(itemList)
        binding.ViewOrder1.layoutManager = LinearLayoutManager(this)
        binding.ViewOrder1.adapter = adapter

        // Ẩn progress bar sau khi hoàn tất
        binding.progressBarOrder1.visibility = View.GONE
    }
    private fun setVariable() {
        val DataLogin = DataLogin(this)
        val userData = DataLogin.getUserData()
        val phonename=userData["fullname"]+" • "+userData["tinh"]
        val fulladdress  = userData["diachi"]+userData["city"]+userData["tinh"]
        val totalFull = intent.getDoubleExtra("total", 0.0)
        val shipfee = 10
        val total= totalFull-shipfee

        val date = intent.getStringExtra("date")
        binding.finalPriceOrderDetail.text = totalFull.toString()
        binding.addressShip.text=fulladdress
        binding.orderDay.text=date
        binding.labelShipDescriptionOrderDetail.text=phonename
        binding.totalPriceOrderDetail.text=total.toString()
        binding.transportFeePriceOrderDetail.text= shipfee.toString()
        binding.waybackIconUserInfo.setOnClickListener { finish() }
    }

}
