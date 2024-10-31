package com.example.project1763.ViewModel



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project1763.Model.BrandModel
import com.example.project1763.Model.ItemsModel
import com.example.project1763.Model.OrderModel
import com.example.project1763.Model.OrderModelList
import com.example.project1763.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class MainViewModel() : ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _brand = MutableLiveData<MutableList<BrandModel>>()
    private val _popular = MutableLiveData<MutableList<ItemsModel>>()
    private val _order = MutableLiveData<MutableList<OrderModel>>()
    private val _orderdetail = MutableLiveData<MutableList<OrderModelList>>()

    val brands: LiveData<MutableList<BrandModel>> = _brand
    val popular: LiveData<MutableList<ItemsModel>> = _popular
    val banners: LiveData<List<SliderModel>> = _banner
    val order: MutableLiveData<MutableList<OrderModel>> get() = _order
    val orderDetail: MutableLiveData<MutableList<OrderModelList>> get() = _orderdetail
    fun loadBanners() {
        val Ref = firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun loadBrand() {
        val Ref = firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<BrandModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(BrandModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _brand.value=lists
                Log.d("TAG112", lists.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun loadPupolar() {
        val Ref = firebaseDatabase.getReference("Items")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _popular.value=lists
                Log.d("TAG12", lists.toString())
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun loadOrder(targetFullname: String) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val ref = firebaseDatabase.getReference("donhang")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listss = mutableListOf<OrderModel>()
                val  Fullname = targetFullname
                for (childSnapshot in snapshot.children) {
                    val fullname = childSnapshot.child("fullname").getValue(String::class.java) ?: ""
                    if (fullname == Fullname) {
                    val orderId = childSnapshot.key ?: ""

                    val timestamp = childSnapshot.child("timestamp").getValue(String::class.java) ?: ""
                    val city = childSnapshot.child("city").getValue(String::class.java) ?: ""
                    val diachi = childSnapshot.child("diachi").getValue(String::class.java) ?: ""
                    val total = childSnapshot.child("Total").getValue(String::class.java) ?: ""
                    val itemsSnapshot = childSnapshot.child("chitiet")
                    val itemsList = mutableListOf<OrderModelList>()
                    for (itemSnapshot in itemsSnapshot.children) {
                        val title = itemSnapshot.child("title").getValue(String::class.java) ?: ""
                        val numberInCart =
                            itemSnapshot.child("numberInCart").getValue(Int::class.java) ?: 0
                        val selectedSize =
                            itemSnapshot.child("size").getValue(String::class.java) ?: ""
                        val price =
                            itemSnapshot.child("price").getValue(Int::class.java) ?: 0
                        val urlPic =
                            itemSnapshot.child("img").getValue(String::class.java) ?: ""
                        val item = OrderModelList(
                            title = title,
                            numberInCart = numberInCart,
                            selectedSize = selectedSize,
                            price = price ,
                            urlPic = urlPic

                        )
                        itemsList.add(item)
                    }
                        _orderdetail.value = itemsList
                        val order = OrderModel(
                        orderId = orderId,
                        items = itemsList,
                        city = city,
                        diachi = diachi,
                        fullname = fullname,
                        Total = total.toDouble(),
                        date = timestamp
                    )
                    listss.add(order)
                }
                }
                // Gán dữ liệu vào MutableLiveData
                _order.value = listss

                Log.d("TAG121", listss.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG121", "DatabaseError: ${error.message}")
            }
        })
    }
    fun loadAdminOrder() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val ref = firebaseDatabase.getReference("donhang")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listss = mutableListOf<OrderModel>()
                for (childSnapshot in snapshot.children) {
                        val fullname = childSnapshot.child("fullname").getValue(String::class.java) ?: ""
                        val orderId = childSnapshot.key ?: ""
                        val timestamp = childSnapshot.child("timestamp").getValue(String::class.java) ?: ""
                        val city = childSnapshot.child("city").getValue(String::class.java) ?: ""
                        val diachi = childSnapshot.child("diachi").getValue(String::class.java) ?: ""
                        val total = childSnapshot.child("Total").getValue(String::class.java) ?: ""
                        val itemsSnapshot = childSnapshot.child("chitiet")
                        val itemsList = mutableListOf<OrderModelList>()
                        for (itemSnapshot in itemsSnapshot.children) {
                            val title = itemSnapshot.child("title").getValue(String::class.java) ?: ""
                            val numberInCart =
                                itemSnapshot.child("numberInCart").getValue(Int::class.java) ?: 0
                            val selectedSize =
                                itemSnapshot.child("size").getValue(String::class.java) ?: ""
                            val price =
                                itemSnapshot.child("price").getValue(Int::class.java) ?: 0
                            val urlPic =
                                itemSnapshot.child("img").getValue(String::class.java) ?: ""
                            val item = OrderModelList(
                                title = title,
                                numberInCart = numberInCart,
                                selectedSize = selectedSize,
                                price = price ,
                                urlPic = urlPic

                            )
                            itemsList.add(item)
                        }
                        _orderdetail.value = itemsList
                        val order = OrderModel(
                            orderId = orderId,
                            items = itemsList,
                            city = city,
                            diachi = diachi,
                            fullname = fullname,
                            Total = total.toDouble(),
                            date = timestamp
                        )
                        listss.add(order)

                }
                // Gán dữ liệu vào MutableLiveData
                _order.value = listss

                Log.d("TAG121", listss.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG121", "DatabaseError: ${error.message}")
            }
        })
    }
}
