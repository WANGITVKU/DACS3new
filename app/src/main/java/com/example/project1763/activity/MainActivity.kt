package com.example.project1763.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.project1763.Adapter.BrandAdapter
import com.example.project1763.Adapter.PopularAdapter
import com.example.project1763.Adapter.SliderAdapter
import com.example.project1763.Helper.DataLogin
import com.example.project1763.Model.SliderModel
import com.example.project1763.R
import com.example.project1763.ViewModel.MainViewModel
import com.example.project1763.databinding.ActivityMainBinding


    class MainActivity : BaseActivity() {

        private val viewModel = MainViewModel()
        private lateinit var binding: ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
//            showAllUserData()
            initBanner()
            initBrand()
            initPopular()
            initBottomMenu()

//            viewModel.order.observe(this, Observer { orders ->
//                // Update UI with orders data
//                Log.d("TAG1212", "Orders: $orders")
//            })
//            if (fullname != null) {
//                viewModel.loadOrder(fullname)
//            }

        }
        fun showAllUserData() {
            val DataLogin = DataLogin(this)
            val userData = DataLogin.getUserData()

            val fullname = userData["fullname"]
            val email = userData["email"]
            val username =  userData["username"]
            // Gán giá trị fullname vào TextView profileUsername bằng cách sử dụng binding
            binding.profileUsername.text = fullname

        }
    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    CartActivity::class.java
                )
            )
        }
        binding.Order.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity, OrderManagement::class.java
                )
            )
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items ->
            banners(items)
            binding.progressBarBanner.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners(images: List<SliderModel>) {
        binding.viewpagerSlider.adapter = SliderAdapter(images, binding.viewpagerSlider)
        binding.viewpagerSlider.clipToPadding = false
        binding.viewpagerSlider.clipChildren = false
        binding.viewpagerSlider.offscreenPageLimit = 3
        binding.viewpagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer)
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpagerSlider)
        }
    }

    private fun initBrand() {
        binding.progressBarBrand.visibility = View.VISIBLE
        viewModel.brands.observe(this, Observer {
            binding.viewBrand.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter = BrandAdapter(it)
            binding.progressBarBrand.visibility = View.GONE
        })
        viewModel.loadBrand()
    }

    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.popular.observe(this, Observer {
            binding.viewPopular.layoutManager = GridLayoutManager(this@MainActivity, 2  )
            binding.viewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE

        })
        viewModel.loadPupolar()

    }

}