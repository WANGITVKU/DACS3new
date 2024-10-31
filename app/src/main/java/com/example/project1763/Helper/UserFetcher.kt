package com.example.project1763.Helper
import com.google.firebase.database.*

class UserFetcher {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    fun fetchUserByUsername(userUsername: String, callback: (User?) -> Unit) {
        val userQuery = reference.orderByChild("username").equalTo(userUsername)
        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        callback(user)
                        return
                    }
                }
                callback(null)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                callback(null)
            }
        })
    }
}

data class User(
    val username: String? = null,
    val fullname: String? = null,
    val email: String? = null,
    val diachi: String? = null,
    val city: String? = null,
    val tinh: String? = null,
    val sdt: String? = null,


)
