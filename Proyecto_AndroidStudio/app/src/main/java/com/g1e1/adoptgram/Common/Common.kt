package com.g1e1.adoptgram.Common

import com.g1e1.adoptgram.Remote.IMyAPI
import com.g1e1.adoptgram.Remote.RetrofitClient

object Common {
    val BASE_URL="http://leonardosantosgrc.com/Adoptgram/"
    val api:IMyAPI
    get() = RetrofitClient.getClient(BASE_URL).create(IMyAPI::class.java)
}