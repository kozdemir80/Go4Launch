package com.example.go4launch.model.userdetails

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth
data class CurrentUser(
    val Name: String? = null,
    val Id: String? = null,
    val restaurantId:String? = null,
    val Photo: Uri? = null)