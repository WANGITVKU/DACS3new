package com.example.project1763.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.project1763.Model.OrderModel
import com.example.project1763.activity.OrderDetail
import com.example.project1763.activity.OrderManagement
import com.example.project1763.databinding.ViewholderOrderBinding

class OrderAdapter(private val itemss: MutableList<OrderModel>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = itemss[position]
        val adress : String = item.city + "_" +item.diachi
        holder.binding.labelPayment.text = item.Total.toString()
        holder.binding.customerIdNumber.text = item.fullname
        holder.binding.orderTotalPrice.text = adress
        holder.binding.dateOrder.text = item.date
        holder.binding.labelPayment.text = "Tổng Tiền"+item.Total.toString()
        // Lặp qua danh sách items và in title của mỗi OrderModelList
        val itemDetails = item.items.joinToString(separator = "\n") { "${it.title} x ${it.numberInCart}" }
        holder.binding.labelPayme.text = itemDetails // Giả sử bạn có một TextView để hiển thị các tiêu đề
//        val requestOptions = RequestOptions().transform(CenterCrop())
        // Giả sử bạn có một ImageView trong layout
        // holder.binding.imageView để gán ảnh từ URL

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, OrderDetail::class.java)
            intent.putExtra("total", item.Total)
            intent.putExtra("date", item.date)
            intent.putExtra("selectedItem", item)
            Log.d("TAG199",item.toString())
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemss.size
}
