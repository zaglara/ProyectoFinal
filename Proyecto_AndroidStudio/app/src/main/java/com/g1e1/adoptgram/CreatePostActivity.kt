package com.g1e1.adoptgram

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.g1e1.adoptgram.Common.Common
import com.g1e1.adoptgram.Common.Credential
import com.g1e1.adoptgram.Common.UserApplication
import com.g1e1.adoptgram.Data.DataManager
import com.g1e1.adoptgram.Data.Post
import com.g1e1.adoptgram.Model.APIResponse
import com.g1e1.adoptgram.Remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class CreatePostActivity : AppCompatActivity(), View.OnClickListener {

    var arrImg: ArrayList<ImageView> = arrayListOf()
    var arrButton: ArrayList<ImageButton> = arrayListOf()
    var arrImgArray:MutableList<ByteArray> = ArrayList()
    private var imageUri: Uri?=null

    val description: TextView get() = findViewById<TextView>(R.id.txtDescripcion_createPost)
    val age: TextView get() = findViewById<TextView>(R.id.txtEdad_createPost)
    val category: Spinner get() = findViewById<Spinner>(R.id.spinnerEspecie_createPost)
    val details: TextView get() = findViewById<TextView>(R.id.txtDetalles_createPost)

    val credential:Credential = UserApplication.prefs.getCredentials()
    var idAux:Int?=null

    internal lateinit var mService: IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        mService = Common.api

        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnPublicar_createPost).setOnClickListener(this)
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnGuardadBorrador_createPost).setOnClickListener(this)
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnImagenes_createPost).setOnClickListener(this)
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnImagenes2_createPost).setOnClickListener(this)

        //getEspecies()
        val adapterEspecie =  ArrayAdapter<com.g1e1.adoptgram.Data.Especie>(this,android.R.layout.simple_spinner_item, DataManager.especies)
        adapterEspecie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category.adapter =  adapterEspecie

        arrButton = arrayListOf(
            findViewById(R.id.btnPic1),
            findViewById(R.id.btnPic2),
            findViewById(R.id.btnPic3),
            findViewById(R.id.btnPic4),
            findViewById(R.id.btnPic5),
            findViewById(R.id.btnPic6)
        )

        arrImg = arrayListOf(
            findViewById(R.id.imgPic1),
            findViewById(R.id.imgPic2),
            findViewById(R.id.imgPic3),
            findViewById(R.id.imgPic4),
            findViewById(R.id.imgPic5),
            findViewById(R.id.imgPic6)
        )

        for (item in arrButton){
            item.setOnClickListener(this)
        }

        idAux = intent.getIntExtra("idAux", -1)

        if (idAux!! > 0){ //AQUI ADENTRO DE COLOCAN LOS DETALLES DEL POST EN SU LUGAR
            findViewById<TextView>(R.id.lblTitle_createPost).text = "Editar Borrador"
            var post = UserApplication.dbHelper.getPost(idAux!!)
            description.text = post!!.descripcion
            age.text = post!!.edad
            details.text = post!!.detalles
            if (post!!.foto1 != null) {
                arrImgArray.add(post!!.foto1!!)
                arrImg!![0].setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[0],0,arrImgArray[0].size))
                arrImg!![0].visibility = View.VISIBLE
                arrButton!![0].visibility = View.VISIBLE
            }
            if (post!!.foto2 != null) {
                arrImgArray.add(post!!.foto2!!)
                arrImg!![1].setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[1],0,arrImgArray[1].size))
                arrImg!![1].visibility = View.VISIBLE
                arrButton!![1].visibility = View.VISIBLE
            }
            if (post!!.foto3 != null) {
                arrImgArray.add(post!!.foto3!!)
                arrImg!![2].setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[2],0,arrImgArray[2].size))
                arrImg!![2].visibility = View.VISIBLE
                arrButton!![2].visibility = View.VISIBLE
            }
            if (post!!.foto4 != null) {
                arrImgArray.add(post!!.foto4!!)
                arrImg!![3].setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[3],0,arrImgArray[3].size))
                arrImg!![3].visibility = View.VISIBLE
                arrButton!![3].visibility = View.VISIBLE
            }
            if (post!!.foto5 != null) {
                arrImgArray.add(post!!.foto5!!)
                arrImg!![4].setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[4],0,arrImgArray[4].size))
                arrImg!![4].visibility = View.VISIBLE
                arrButton!![4].visibility = View.VISIBLE
            }
            if (post!!.foto6 != null) {
                arrImgArray.add(post!!.foto6!!)
                arrImg!![5].setImageBitmap(BitmapFactory.decodeByteArray(arrImgArray[5],0,arrImgArray[5].size))
                arrImg!![5].visibility = View.VISIBLE
                arrButton!![5].visibility = View.VISIBLE
            }
            category.setSelection(post.especie!!.id - 1)
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnPublicar_createPost -> {
                var campos:Boolean = validarCampos()
                if (campos) {
                    var insertar:Boolean = insertPost(description.text.toString(), age.text.toString(), details.text.toString(), "publicado", (category.selectedItemPosition+1).toString(), credential.user_id)
                    if (insertar){
                        var i:Int = 1
                        for (item in arrImgArray){
                            var strEncodeImage:String = ""
                            val encodedString:String = Base64.getEncoder().encodeToString(item)
                            strEncodeImage = "data:image/png;base64," + encodedString
                            insertImgPost(strEncodeImage, i.toString(), credential.user_id)
                            i++
                        }
                    }
                }
            }
            R.id.btnGuardadBorrador_createPost -> {
                var campos: Boolean = validarCampos()
                if (campos) {
                    if (idAux == 0) {
                        insertDraft()
                        finish()
                    }
                    else if (idAux!! > 0){
                        updateDraft(idAux!!)
                        finish()
                    }
                }
            }
            R.id.btnImagenes_createPost -> { //CAMERA
                if (arrImgArray.size < 6){
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, 1002)
                }
                else {
                    Toast.makeText(this, "Ya no se pueden seleccionar más imagenes", Toast.LENGTH_LONG).show()
                }
            }
            R.id.btnImagenes2_createPost -> { //GALLERY
                if (arrImgArray.size < 6){
                    val cameraIntent = Intent(Intent.ACTION_PICK)
                    cameraIntent.type = "image/*"
                    startActivityForResult(cameraIntent, 1001)
                }
                else {
                    Toast.makeText(this, "Ya no se pueden seleccionar más imagenes", Toast.LENGTH_LONG).show()
                }
            }
            R.id.btnPic1 -> deletePicture(0)
            R.id.btnPic2 -> deletePicture(1)
            R.id.btnPic3 -> deletePicture(2)
            R.id.btnPic4 -> deletePicture(3)
            R.id.btnPic5 -> deletePicture(4)
            R.id.btnPic6 -> deletePicture(5)
        }
    }

    private fun updateDraft(id: Int) {
        var post:Post = Post()
        post.id = id
        post.descripcion = description.text.toString()
        post.edad = age.text.toString()
        post.detalles = details.text.toString()
        post.especie = category.selectedItem as com.g1e1.adoptgram.Data.Especie
        post.id_usuario = credential.user_id
        post.foto1 = null
        post.foto2 = null
        post.foto3 = null
        post.foto4 = null
        post.foto5 = null
        post.foto6 = null
        var i:Int = 0
        for (item in arrImgArray){
            if (i == 0)
                post.foto1 = arrImgArray[i]
            if (i == 1)
                post.foto2 = arrImgArray[i]
            if (i == 2)
                post.foto3 = arrImgArray[i]
            if (i == 3)
                post.foto4 = arrImgArray[i]
            if (i == 4)
                post.foto5 = arrImgArray[i]
            if (i == 5)
                post.foto6 = arrImgArray[i]
            i++
        }
        UserApplication.dbHelper.updatePost(post)
    }

    private fun insertDraft() {
        var post:Post = Post()
        post.descripcion = description.text.toString()
        post.edad = age.text.toString()
        post.detalles = details.text.toString()
        post.especie = category.selectedItem as com.g1e1.adoptgram.Data.Especie
        post.id_usuario = credential.user_id
        post.foto1 = null
        post.foto2 = null
        post.foto3 = null
        post.foto4 = null
        post.foto5 = null
        post.foto6 = null
        var i:Int = 0
        for (item in arrImgArray){
            if (i == 0)
                post.foto1 = arrImgArray[i]
            if (i == 1)
                post.foto2 = arrImgArray[i]
            if (i == 2)
                post.foto3 = arrImgArray[i]
            if (i == 3)
                post.foto4 = arrImgArray[i]
            if (i == 4)
                post.foto5 = arrImgArray[i]
            if (i == 5)
                post.foto6 = arrImgArray[i]
            i++
        }
        UserApplication.dbHelper.insertPost(post)
    }

    private fun insertImgPost(image: String, ind: String, user_id: String) {
        mService.insertImgPost(image, ind, user_id)
            .enqueue(object : Callback<APIResponse> {
                override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                    if (response!!.body()!!.error) {
                        Toast.makeText(this@CreatePostActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                    }
                    else {    //DENTRO DE ESTE ELSE SE PUEDE AGREGAR EL PASO AL INTERIOR DE LA APLICACION
                        if (idAux!! > 0){
                            UserApplication.dbHelper.deletePost(idAux!!)
                        }
                        Toast.makeText(this@CreatePostActivity, response.body()!!.success_msg, Toast.LENGTH_SHORT).show()
                        finish()
                        if (idAux!! > 0){
                            val intent =  Intent(this@CreatePostActivity, SavedPostsActivity::class.java)
                            intent.putExtra("uso", "borradores")
                            startActivity(intent)
                        }
                    }
                }
                override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                    Toast.makeText(this@CreatePostActivity, t!!.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun insertPost(description: String, age: String, details: String, status: String, category_id: String, user_id: String): Boolean {
        var works:Boolean = true
        mService.insertPost(description, age, details, status, category_id, user_id)
            .enqueue(object : Callback<APIResponse> {
                override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                    if (response!!.body()!!.error) {
                        Toast.makeText(this@CreatePostActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        works = false
                    }
                    else {    //DENTRO DE ESTE ELSE SE PUEDE AGREGAR EL PASO AL INTERIOR DE LA APLICACION
                        Toast.makeText(this@CreatePostActivity, response.body()!!.success_msg, Toast.LENGTH_SHORT).show()
                        works = true
                    }
                }
                override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                    Toast.makeText(this@CreatePostActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    works = false
                }
            })
        return works
    }

    private fun validarCampos(): Boolean {
        var insert:Boolean = true
        if (description.text.isEmpty()){
            description.setError("No puede dejar el campo vacío")
            insert = false
        }
        if (age.text.isEmpty()){
            age.setError("No puede dejar el campo vacío")
            insert = false
        }
        if (details.text.isEmpty()){
            details.setError("No puede dejar el campo vacío")
            insert = false
        }
        if (arrImgArray.size == 0){
            Toast.makeText(this, "Debe seleccionar al menos una imagen", Toast.LENGTH_SHORT).show()
            insert = false
        }
        return insert
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 1001) { //GALERY
                imageUri = data!!.data
                arrImgArray.add(contentResolver.openInputStream(imageUri!!)?.readBytes()!!)
                arrImg!![arrImgArray.size - 1].setImageURI(imageUri)
                arrImg!![arrImgArray.size - 1].visibility = View.VISIBLE;
                arrButton!![arrImgArray.size - 1].visibility = View.VISIBLE;
            }
            if (requestCode == 1002) { //CAMERA
                val photo =  data?.extras?.get("data") as Bitmap
                val stream = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                arrImgArray.add(stream.toByteArray())
                arrImg!![arrImgArray.size - 1].setImageBitmap(photo)
                arrImg!![arrImgArray.size - 1].visibility = View.VISIBLE;
                arrButton!![arrImgArray.size - 1].visibility = View.VISIBLE;
            }
        }
    }

    private fun getEspecies() {
        mService.getCategories()
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@CreatePostActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {
                            //Toast.makeText(this@CreatePostActivity, response!!.body()!!.especies!![0].especie, Toast.LENGTH_SHORT).show()
                            val arrayCategories = response.body()!!.especies
                            val listaEspecies: MutableList<String> = ArrayList()
                            for (item in arrayCategories!!){
                                listaEspecies.add(item.especie.toString())
                            }
                            val adapter = ArrayAdapter(this@CreatePostActivity, android.R.layout.simple_spinner_item, listaEspecies)
                            category.adapter = adapter
                        }
                    }
                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@CreatePostActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }
                })

    }

    private fun deletePicture(id:Int){
        arrImgArray.removeAt(id)
        for (item in arrImg)
            item.visibility = View.INVISIBLE
        for (item in arrButton)
            item.visibility = View.INVISIBLE
        var i:Int = 0
        for (item in arrImgArray){
            arrImg!![i].setImageBitmap(BitmapFactory.decodeByteArray(item,0,item.size))
            arrImg!![i].visibility = View.VISIBLE
            arrButton!![i].visibility = View.VISIBLE
            i++
        }
    }

}