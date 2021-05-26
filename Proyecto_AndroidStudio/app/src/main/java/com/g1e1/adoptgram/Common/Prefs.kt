package com.g1e1.adoptgram.Common

import android.content.Context

class Prefs (val context: Context) {

    //CONSTANTS
    val SHARED_NAME = "USER_PREFERENCES"

    val managerPrefs = context.getSharedPreferences(SHARED_NAME,Context.MODE_PRIVATE)

    fun saveCredentials (credential:Credential) {
        var editor = managerPrefs.edit()
        editor.putString("user_id", credential.user_id)
        editor.putString("user_name", credential.user_name)
        editor.putString("user_lastname", credential.user_lastname)
        editor.putString("user_nickname", credential.user_nickname)
        editor.putString("user_email", credential.user_email)
        editor.putString("user_telephone", credential.user_telephone)
        editor.putString("user_address", credential.user_address)
        editor.putString("user_image", credential.user_image)
        editor.commit()
    }

    fun getCredentials():Credential {
        val credential:Credential = Credential()
        val user_id:String? = managerPrefs.getString("user_id", "")
        val user_name:String? = managerPrefs.getString("user_name", "")
        val user_lastname:String? = managerPrefs.getString("user_lastname", "")
        val user_nickname:String? = managerPrefs.getString("user_nickname", "")
        val user_email:String? = managerPrefs.getString("user_email", "")
        val user_telephone:String? = managerPrefs.getString("user_telephone", "")
        val user_address:String? = managerPrefs.getString("user_address", "")
        val user_image:String? = managerPrefs.getString("user_image", "")

        credential.user_id = user_id!!
        credential.user_name = user_name!!
        credential.user_lastname = user_lastname!!
        credential.user_nickname = user_nickname!!
        credential.user_email = user_email!!
        credential.user_telephone = user_telephone!!
        credential.user_address = user_address!!
        credential.user_image = user_image!!

        return credential
    }

}