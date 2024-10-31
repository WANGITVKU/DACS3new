package com.example.project1763.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.project1763.Model.OrderModelList
import com.example.project1763.databinding.ViewholderOrderdetailBinding

class OrderDetailAdapter(private val itemss: MutableList<OrderModelList>) :

    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderOrderdetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderOrderdetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemss[position]
        val tong=  item.price * item.numberInCart
        holder.binding.titleTxt.text = item.title+"  size :"+item.selectedSize
        holder.binding.feeEachItem.text = item.price.toString()
        holder.binding.totalEachItem.text = tong.toString()
        holder.binding.numberItemTxt.text = item.numberInCart.toString()
        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(item.urlPic)
            .apply(requestOptions)
            .into(holder.binding.pic1)
        Log.d("TAGOrder", item.urlPic.toString())
        // Giả sử bạn có một ImageView trong layout
        // holder.binding.imageView để gán ảnh từ URL
    }

    override fun getItemCount(): Int = itemss.size
}
