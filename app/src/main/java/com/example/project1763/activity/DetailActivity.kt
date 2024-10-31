package com.example.project1763.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1763.Helper.ManagmentCart
import com.example.project1763.Adapter.ColorAdapter
import com.example.project1763.Adapter.SizeAdapter
import com.example.project1763.Adapter.SliderAdapter

import com.example.project1763.Model.ItemsModel
import com.example.project1763.Model.SliderModel
import com.example.project1763.databinding.ActivityDetailBinding


class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private var numberOder = 1
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        getBundle()
        banners()
        initLists()


    }

    private fun initLists() {
        val sizeList = ArrayList<String>()
        for (size in item.size) {
            sizeList.add(size.toString())
        }

        // Truyền item vào adapter SizeAdapter
        binding.sizeList.adapter = SizeAdapter(sizeList, item)
        binding.sizeList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl) {
            colorList.add(imageUrl)
        }

        binding.colorList.adapter = ColorAdapter(colorList)
        binding.colorList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun banners() {
        val sliderItems = ArrayList<SliderModel>()
        for (imageUrl in item.picUrl) {
            sliderItems.add(SliderModel(imageUrl))
        }

        binding.slider.adapter = SliderAdapter(sliderItems, binding.slider)
        binding.slider.clipToPadding = true
        binding.slider.clipChildren = true
        binding.slider.offscreenPageLimit = 1


        if (sliderItems.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }
    }

    private fun getBundle() {
        item = intent.getParcelableExtra("object")!!

        binding.titleTxt.text = item.title
        binding.descriptionTxt.text = item.description
        binding.priceTxt.text = "$" + item.price
        binding.ratingTxt.text = "${item.rating} Rating"

        binding.addToCartBtn.setOnClickListener {
            if(item.selectedSize==""){
                Toast.makeText(this@DetailActivity, "Vui lòng chọn size ", Toast.LENGTH_SHORT).show()
                Log.d("TAGDetailActivity",item.toString())
            }
            else{
            item.numberInCart = numberOder
            managmentCart.insertFood(item)}
        }
        binding.backBtn.setOnClickListener { finish() }
        binding.cartBtn.setOnClickListener {

                startActivity(Intent(this@DetailActivity, CartActivity::class.java))

        }
    }
}
