package com.example.plz_prepare_mng

import com.google.gson.JsonObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class FCMRejectPush(val pushToken : String){
    val AUTH_Key_FCM = "AAAAlWmg7E8:APA91bG4r6BSyzyWT1js5WWD8r50l2FMQjIlO7aADqsCH95ycSaAgyN2xFHfmGVuAZ517tyEbrmbwuk3ImOz9jOP_pZ300xmnSbdI9EoCmF72xyNA0Z_Nrhc5UyTUEndFXcc_GtoIPZV"
    val URL_FCM = "https://fcm.googleapis.com/fcm/send"

    val url : URL = URL(URL_FCM)
    val conn : HttpURLConnection = url.openConnection() as HttpURLConnection
    lateinit var wr : OutputStreamWriter

    fun pushFCMNotification(){
        conn.useCaches = false
        conn.doInput = true
        conn.doOutput = true
        conn.requestMethod = "POST"
        conn.setRequestProperty("Authorization","key="+AUTH_Key_FCM)
        conn.setRequestProperty("Content-Type","application/json")

        val json = JsonObject()
        val info = JsonObject()

        val msgBody  = "주문이 거절되었습니다. 거절 이유를 확인해보세요."
        info.addProperty("title","알림")
        info.addProperty("body",msgBody)
        info.addProperty("sound","default")

        json.add("notification",info)
        json.addProperty("to",pushToken)
        try {
            wr = OutputStreamWriter(conn.outputStream, "UTF-8")
            wr.write(json.toString())
            wr.flush()

        } catch (e: Exception) {
            connFinish()
            throw Exception("OutputStreamException : $e")
        }


        if (conn.responseCode != HttpURLConnection.HTTP_OK) {
            connFinish()
            throw RuntimeException("Failed : HTTP error code : " + conn.responseCode)
        }
    }
    private fun connFinish(){
        wr.close()
        conn.disconnect()
    }
}