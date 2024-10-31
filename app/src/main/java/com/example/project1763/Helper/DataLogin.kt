package com.example.project1763.Helper

import android.content.Context

class DataLogin(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)

    fun saveUserData(fullname: String, email: String, username: String, diachi: String?, city: String?, tinh: String?, sdt: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("fullname", fullname)
        editor.putString("email", email)
        editor.putString("username", username)
        editor.putString("diachi", diachi)
        editor.putString("tinh", tinh)
        editor.putString("city", city)
        editor.putString("sdt", sdt)
        editor.apply()
    }

    fun getUserData(): Map<String, String?> {
        val fullname = sharedPreferences.getString("fullname", "")
        val email = sharedPreferences.getString("email", "")
        val username = sharedPreferences.getString("username", "")
        val diachi = sharedPreferences.getString("diachi", null) // Chỉnh null ở đây
        val tinh = sharedPreferences.getString("tinh", null) // Chỉnh null ở đây
        val city = sharedPreferences.getString("city", null) // Chỉnh null ở đây
        val sdt = sharedPreferences.getString("sdt", null) // Chỉnh null ở đây
        return mapOf("fullname" to fullname, "email" to email, "username" to username, "diachi" to diachi, "city" to city, "tinh" to tinh, "sdt" to sdt)
    }

}
