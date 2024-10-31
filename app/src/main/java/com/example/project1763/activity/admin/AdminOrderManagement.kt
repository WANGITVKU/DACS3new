package com.example.project1763.activity.admin

import com.example.project1763.activity.BaseActivity
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
import com.example.project1763.databinding.AdminOrderManagementBinding
class AdminOrderManagement : BaseActivity() {
    private lateinit var binding: AdminOrderManagementBinding
    private val viewModel = MainViewModel()
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminOrderManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
//        setVariable()
        initOrder()

    }

    private fun initOrder() {

            viewModel.loadAdminOrder()



        binding.progressBarOrder.visibility = View.VISIBLE
        viewModel.order.observe(this, Observer { orderList ->
            binding.ViewOrder.layoutManager = LinearLayoutManager(this@AdminOrderManagement)
            binding.ViewOrder.adapter = OrderAdapter(orderList.toMutableList())
            binding.progressBarOrder.visibility = View.GONE

        })
    }
//    private fun setVariable() {
//        binding.backBtn121.setOnClickListener { finish() }
//    }
}
