package com.apoorv.e_pushtak.database

class Users {
    var name: String? = null
    var phoneno: String? = null
    var email: String? = null
    var password: String? = null
    var confirmPassword: String? = null
    var gender: String? = null

    constructor(){}
    constructor(
        name: String?, phoneno: String, email: String?, password: String?, confirmPassword: String?, gender:
        String?
    ) {
        this.name = name
        this.phoneno = phoneno
        this.email = email
        this.password = password
        this.confirmPassword = confirmPassword
        this.gender = gender
    }

}