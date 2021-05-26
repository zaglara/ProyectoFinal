package com.g1e1.adoptgram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.g1e1.adoptgram.Common.Common
import com.g1e1.adoptgram.Common.Credential
import com.g1e1.adoptgram.Common.UserApplication
import com.g1e1.adoptgram.Common.UserApplication.Companion.dbHelper
import com.g1e1.adoptgram.Model.APIResponse
import com.g1e1.adoptgram.Model.Post
import com.g1e1.adoptgram.Remote.IMyAPI
import kotlinx.android.synthetic.main.activity_saved_posts.*
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_my_posts.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedPostsActivity : AppCompatActivity() {

    val credential: Credential = UserApplication.prefs.getCredentials()
    internal lateinit var mService: IMyAPI
    var uso:String?=null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_posts)

        mService = Common.api
        uso = intent.getStringExtra("uso").toString()

        if (uso == "null")
            getSavedPosts(credential.user_id)
        else if (uso == "retirados"){
            Toast.makeText(this, uso, Toast.LENGTH_SHORT).show()
            findViewById<TextView>(R.id.lblTitle_savedPosts).text = "Retirados"
            getMyPosts(credential.user_id, "adoptado")
        }
        else if (uso == "borradores"){
            findViewById<TextView>(R.id.lblTitle_savedPosts).text = "Borradores"
            getDrafts(credential.user_id)
        }

    }

    private fun getDrafts(user: String) {
        val draftAdapter = DraftsAdapter(this, dbHelper.getListOfPost(credential.user_id))
        listGuardados.adapter = draftAdapter

    }

    private fun getSavedPosts(user: String) {
        mService.getSavedPosts(user)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@SavedPostsActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {
                            val arrayPosts = response.body()!!.posts
                            val listaPosts: MutableList<Post> = ArrayList()
                            for (item in arrayPosts!!) {
                                listaPosts.add(item)
                            }
                            val adapter = PostsAdapter(this@SavedPostsActivity, listaPosts)
                            listGuardados.adapter = adapter
                            listGuardados.setOnItemClickListener { parent, view, position, id ->
                                val intent = Intent(this@SavedPostsActivity, PetPageActivity::class.java)
                                intent.putExtra("id", listaPosts[position].id)
                                startActivity(intent)
                            }
                        }
                    }

                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@SavedPostsActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getMyPosts(user: String, status:String) {
        mService.getMyPosts(user, status)
            .enqueue(object : Callback<APIResponse> {
                override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                    if (response!!.body()!!.error)
                        Toast.makeText(this@SavedPostsActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                    else {
                        val arrayPosts = response.body()!!.posts
                        val listaPosts: MutableList<Post> = ArrayList()
                        for (item in arrayPosts!!) {
                            listaPosts.add(item)
                        }
                        val adapter = PostsAdapter(this@SavedPostsActivity, listaPosts)
                        listGuardados.adapter = adapter
                        listGuardados.setOnItemClickListener { parent, view, position, id ->
                            val intent = Intent(this@SavedPostsActivity, PetPageActivity::class.java)
                            intent.putExtra("id", listaPosts[position].id)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                    Toast.makeText(this@SavedPostsActivity, t!!.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

}