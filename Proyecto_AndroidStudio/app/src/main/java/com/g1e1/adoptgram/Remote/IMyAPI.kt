package com.g1e1.adoptgram.Remote

import com.g1e1.adoptgram.Model.APIResponse
import com.g1e1.adoptgram.Model.Especie
import retrofit2.Call
import retrofit2.http.*

interface IMyAPI {
    @FormUrlEncoded
    @POST("register.php")
    fun registerUser(@Field("name") name:String, @Field("lastname") lastname:String, @Field("nickname") nickname:String, @Field("email") email:String, @Field("telephone") telephone:String, @Field("address") address:String, @Field("password") password:String, @Field("image") image: String):Call<APIResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(@Field("email") email:String, @Field("password") password:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("updateUser.php")
    fun updateUser(@Field("id") id:String, @Field("name") name:String, @Field("lastname") lastname:String, @Field("nickname") nickname:String, @Field("telephone") telephone:String, @Field("address") address:String, @Field("image") image: String):Call<APIResponse>

    @FormUrlEncoded
    @POST("updateUser.php")
    fun updateUser2(@Field("id") id:String, @Field("name") name:String, @Field("lastname") lastname:String, @Field("nickname") nickname:String, @Field("telephone") telephone:String, @Field("address") address:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("updatePassword.php")
    fun updatePassword(@Field("id") id:String, @Field("password") password:String):Call<APIResponse>

    @POST("getCategories.php")
    fun getCategories():Call<APIResponse>

    @FormUrlEncoded
    @POST("registerPet.php")
    fun insertPost(@Field("description") description:String, @Field("age") age:String, @Field("details") details:String, @Field("status") status:String, @Field("category_id") category_id:String, @Field("user_id") user_id:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("insertImgPost.php")
    fun insertImgPost(@Field("image") image:String, @Field("ind") ind:String, @Field("user_id") user_id:String):Call<APIResponse>

    @POST("getMostRecent.php")
    fun getMostRecent():Call<APIResponse>

    @FormUrlEncoded
    @POST("getPost.php")
    fun getDetailsPost(@Field("id") id:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("getImages.php")
    fun getPictures(@Field("id") id:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("getByCategory.php")
    fun getByCategory(@Field("category") category:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("getMyPosts.php")
    fun getMyPosts(@Field("user") user:String, @Field("status") status:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("getComments.php")
    fun getComments(@Field("post") post:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("insertComment.php")
    fun insertComment(@Field("comment") comment:String, @Field("post") post:String, @Field("user") user:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("insertGuardado.php")
    fun insertGuardado(@Field("post") post:String, @Field("user") user:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("getSavedPosts.php")
    fun getSavedPosts(@Field("user") user:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("retirarPost.php")
    fun retirarPost(@Field("post") post:String):Call<APIResponse>

}