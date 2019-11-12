package com.example.plz_prepare_mng

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Restaurant(
    var Rname : String?,
    var RlocationX : Double?,
    var RlocationY : Double?,
    var Rmenu : List<Menu>?
)