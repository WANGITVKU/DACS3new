package com.example.project1763.activity.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1763.Adapter.OrderAdapter
import com.example.project1763.Adapter.PopularAdapter

import com.example.project1763.ViewModel.MainViewModel
import com.example.project1763.activity.BaseActivity
import com.example.project1763.databinding.AdminProductMainscreenManagementBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AdminDetailManagement : BaseActivity() {

        private lateinit var binding: AdminProductMainscreenManagementBinding
        private val viewModel = MainViewModel()
        private lateinit var database: DatabaseReference

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = AdminProductMainscreenManagementBinding.inflate(layoutInflater)
            setContentView(binding.root)
            database = FirebaseDatabase.getInstance().reference
            initBottomMenu()
            initPopular()

        }

    private fun initPopular() {
        binding.progressBarPopular1.visibility = View.VISIBLE
        viewModel.popular.observe(this@AdminDetailManagement, Observer {
            binding.viewPopular.layoutManager = GridLayoutManager(this@AdminDetailManagement, 2  )
            binding.viewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular1.visibility = View.GONE

        })
        viewModel.loadPupolar()

    }
    private fun initBottomMenu() {
        binding.addNewProduct.setOnClickListener {
            startActivity(
                Intent(
                    this@AdminDetailManagement,
                    AddProduct::class.java
                )
            )
        }
        binding.box3AdminProduct.setOnClickListener {
            startActivity(
                Intent(
                    this, AdminOrderManagement::class.java
                )
            )
        }
    }

}