package com.example.plz_prepare_mng

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Restaurant(
    var Rname : String?,
    var Rcategory : String?,
    var RlocationX : Long?,
    var RlocationY : Long?,
    var Rmenu : List<Menu>?
)