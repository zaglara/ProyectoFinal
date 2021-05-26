package com.g1e1.adoptgram

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.g1e1.adoptgram.Common.Common
import com.g1e1.adoptgram.Common.Credential
import com.g1e1.adoptgram.Common.UserApplication
import com.g1e1.adoptgram.Model.APIResponse
import com.g1e1.adoptgram.Model.Post
import com.g1e1.adoptgram.Remote.IMyAPI
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class PetPageActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var mService: IMyAPI

    val especie: TextView get() = findViewById<TextView>(R.id.lblEspecie_petPage)
    val descripcion: TextView get() = findViewById<TextView>(R.id.lblDescripcion_petPage)
    val edad: TextView get() = findViewById<TextView>(R.id.lblEdad_petPage)
    val detalles: TextView get() = findViewById<TextView>(R.id.lblDetalles_petPage)
    val nickname: TextView get() = findViewById<TextView>(R.id.lblNombre_petPage)
    val telefono: TextView get() = findViewById<TextView>(R.id.lblTelefono_petPage)
    val image: ImageView get() = findViewById<ImageView>(R.id.imgFoto_petPage)
    val retirar: Button get() = findViewById<Button>(R.id.button3)

    var arrImg: ArrayList<ImageView> = arrayListOf()
    var arrImgArray:MutableList<ByteArray> = ArrayList()
    var id_post:String?=null
    val credential: Credential = UserApplication.prefs.getCredentials()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_page)

        id_post = intent.getStringExtra("id").toString()
        mService = Common.api

        arrImg = arrayListOf(
                findViewById(R.id.imgFoto1_petPage),
                findViewById(R.id.imgFoto2_petPage),
                findViewById(R.id.imgFoto3_petPage),
                findViewById(R.id.imgFoto4_petPage),
                findViewById(R.id.imgFoto5_petPage),
                findViewById(R.id.imgFoto6_petPage)
        )

        getDetailsPost(id_post!!)
        getPictures(id_post!!)

        for (item in arrImg){
            item.setOnClickListener(this)
        }

        findViewById<Button>(R.id.button).setOnClickListener(this)
        findViewById<Button>(R.id.button2).setOnClickListener(this)
        findViewById<Button>(R.id.button3).setOnClickListener(this)

    }

    private fun getPictures(id: String) {
        mService.getPictures(id)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@PetPageActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {
                            var i:Int = 0
                            for (item in response!!.body()!!.postImages!!){
                                val strImage:String = item.foto!!.replace("data:image/png;base64," , "")
                                arrImgArray.add(Base64.getDecoder().decode(strImage))
                                arrImg[i].setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[i],0,arrImgArray[i].size))
                                arrImg[i].visibility = View.VISIBLE
                                if (i == 0){
                                    image.setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[i],0,arrImgArray[i].size))
                                }
                                i++
                            }
                        }
                    }

                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@PetPageActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getDetailsPost(id:String){
        mService.getDetailsPost(id)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@PetPageActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {
                            especie.text = response.body()!!.post!!.especie
                            descripcion.text = response.body()!!.post!!.descripcion
                            edad.text = response.body()!!.post!!.edad
                            detalles.text = response.body()!!.post!!.detalles
                            nickname.text = "Nickname: " + response.body()!!.post!!.nickname
                            telefono.text = "Teléfono: " + response.body()!!.post!!.telefono
                            if (response!!.body()!!.post!!.id_usuario == credential.user_id)
                                retirar.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@PetPageActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.imgFoto1_petPage -> image.setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[0],0,arrImgArray[0].size))
            R.id.imgFoto2_petPage -> image.setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[1],0,arrImgArray[1].size))
            R.id.imgFoto3_petPage -> image.setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[2],0,arrImgArray[2].size))
            R.id.imgFoto4_petPage -> image.setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[3],0,arrImgArray[3].size))
            R.id.imgFoto5_petPage -> image.setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[4],0,arrImgArray[4].size))
            R.id.imgFoto6_petPage -> image.setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[5],0,arrImgArray[5].size))
            R.id.button -> {
                val intent =  Intent(this, CommentActivity::class.java)
                intent.putExtra("id", id_post)
                startActivity(intent)
            }
            R.id.button2 -> insertGuardado(id_post!!, credential.user_id)
            R.id.button3 -> retirarPost(id_post!!)
        }
    }

    private fun retirarPost(post: String) {
        mService.retirarPost(post)
            .enqueue(object : Callback<APIResponse> {
                override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                    if (response!!.body()!!.error)
                        Toast.makeText(this@PetPageActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                    else {
                        Toast.makeText(this@PetPageActivity, response!!.body()!!.success_msg, Toast.LENGTH_SHORT).show()
                        retirar.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                    Toast.makeText(this@PetPageActivity, t!!.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun insertGuardado(post: String, user: String) {
        mService.insertGuardado(post, user)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@PetPageActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {
                            Toast.makeText(this@PetPageActivity, response!!.body()!!.success_msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@PetPageActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

}
