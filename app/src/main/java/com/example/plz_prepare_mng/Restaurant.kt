package com.example.plz_prepare_mng

// 레스토랑이름, 위치, 핸드폰번호 그리고 메뉴를 가지고 있는 class, Firebase realtime database에 넣기 위해 data class로 선언
data class Restaurant(
    var Rname : String?,
    var RlocationX : Double?,
    var RlocationY : Double?,
    var PhoneNum : String?,
    var Rmenu : List<Menu>?
)