package com.g1e1.adoptgram

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.g1e1.adoptgram.Common.Common
import com.g1e1.adoptgram.Common.Credential
import com.g1e1.adoptgram.Common.UserApplication
import com.g1e1.adoptgram.Model.APIResponse
import com.g1e1.adoptgram.Model.Comment
import com.g1e1.adoptgram.Model.Post
import com.g1e1.adoptgram.Remote.IMyAPI
import kotlinx.android.synthetic.main.activity_comment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var mService: IMyAPI

    val comentario: TextView get() = findViewById<TextView>(R.id.txtComentario_comment)
    var id_post:String?= null
    val credential: Credential = UserApplication.prefs.getCredentials()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        id_post = intent.getStringExtra("id").toString()
        mService = Common.api

        getComments(id_post!!)

        findViewById<Button>(R.id.btnComentario_comment).setOnClickListener(this)

    }

    private fun getComments(post: String) {
        mService.getComments(post)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@CommentActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {
                            val arrayComments = response.body()!!.comments
                            val listaComments: MutableList<Comment> = ArrayList()
                            for (item in arrayComments!!) {
                                listaComments.add(item)
                            }
                            val adapter = CommentsAdapter(this@CommentActivity, listaComments)
                            listComments_comment.adapter = adapter
                        }
                    }

                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@CommentActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.btnComentario_comment -> {
                if (comentario.text.isNotEmpty())
                    insertComment(comentario.text.toString(), id_post!!, credential.user_id)
                else
                    Toast.makeText(this, "Debe escribir un comentario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertComment(comment:String, post: String, user: String) {
        mService.insertComment(comment, post, user)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@CommentActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {    //DENTRO DE ESTE ELSE SE PUEDE AGREGAR EL PASO AL INTERIOR DE LA APLICACION
                            Toast.makeText(this@CommentActivity, response.body()!!.success_msg, Toast.LENGTH_SHORT).show()
                            finish()
                            val intent =  Intent(this@CommentActivity, CommentActivity::class.java)
                            intent.putExtra("id", id_post)
                            startActivity(intent)
                        }
                    }
                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@CommentActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }

                })
    }
}