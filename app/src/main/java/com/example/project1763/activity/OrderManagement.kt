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

import com.example.project1763.Adapter.OrderAdapter
import com.example.project1763.Helper.DataLogin
import com.example.project1763.ViewModel.MainViewModel
import com.example.project1763.databinding.OrderManagementBinding
class OrderManagement : BaseActivity() {
    private lateinit var binding: OrderManagementBinding
    private val viewModel = MainViewModel()
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OrderManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
        setVariable()
        initOrder()

    }

    private fun initOrder() {
        val DataLogin = DataLogin(this)
        val userData = DataLogin.getUserData()
        val fullname = userData["fullname"]

        if (fullname != null) {
            viewModel.loadOrder(fullname)
            Log.d("TAGOrderManagement:",fullname.toString())
        }

        binding.progressBarOrder.visibility = View.VISIBLE
        viewModel.order.observe(this, Observer { orderList ->
            binding.ViewOrder.layoutManager = LinearLayoutManager(this@OrderManagement)
            binding.ViewOrder.adapter = OrderAdapter(orderList.toMutableList())
            binding.progressBarOrder.visibility = View.GONE

        })
       }

    private fun setVariable() {
        binding.backBtn12.setOnClickListener { finish() }
    }
}
