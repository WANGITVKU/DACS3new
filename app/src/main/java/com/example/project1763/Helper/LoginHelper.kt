package com.example.project1763

class LoginHelper {
    var fullname: String? = null
    var email: String? = null
    var username: String? = null
    var password: String? = null

    constructor(fullname: String?, email: String?, username: String?, password: String?) {
        this.fullname = fullname
        this.email = email
        this.username = username
        this.password = password
    }

    constructor()
}