package com.example.go4launch.model.userdetails

import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class CurrentUser(
    var Name: String? = null,
    var Id: String? = null,
    var Photo: Uri? = null,
    var restaurantId:String? = null)