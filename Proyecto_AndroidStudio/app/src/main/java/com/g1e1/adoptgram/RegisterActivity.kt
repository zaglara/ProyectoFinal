package com.g1e1.adoptgram

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.drawToBitmap
import com.g1e1.adoptgram.Common.Common
import com.g1e1.adoptgram.Model.APIResponse
import com.g1e1.adoptgram.Remote.IMyAPI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.Base64.getEncoder
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var mService:IMyAPI

    val name: TextView get() = findViewById<TextView>(R.id.txtNombre_register)
    val lastname: TextView get() = findViewById<TextView>(R.id.txtApelldio_register)
    val nickname: TextView get() = findViewById<TextView>(R.id.txtUsuario_register)
    val email: TextView get() = findViewById<TextView>(R.id.txtCorreo_register)
    val telephone: TextView get() = findViewById<TextView>(R.id.txtTelefono_register)
    val address: TextView get() = findViewById<TextView>(R.id.txtDireccion_register)
    val password: TextView get() = findViewById<TextView>(R.id.txtContrasena_register)
    val image: ImageView get() = findViewById<ImageView>(R.id.imgProfilePic_register)

    private var imageUri:Uri?=null
    private var imgArray:ByteArray?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //INIT SERVICE
        mService = Common.api

        //EVENT
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnSubmit_register).setOnClickListener(this)
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnPicture_register).setOnClickListener(this)
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnPicture_register2).setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnSubmit_register->{
                var insert:Boolean = true
                //val bitmap = (image.drawable as BitmapDrawable).bitmap
                //val stream = ByteArrayOutputStream()
                //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                //val imageInByte = stream.toByteArray()
                var strEncodeImage:String = ""
                if (imgArray == null){
                    Toast.makeText(this, "Error: Necesita ingresar una imagen", Toast.LENGTH_SHORT).show()
                    insert = false
                }
                else {
                    val encodedString:String = Base64.getEncoder().encodeToString(this.imgArray)
                    strEncodeImage = "data:image/png;base64," + encodedString
                }
                if (!validarEmail(email.text.toString())){
                    email.setError("Email no válido")
                    insert = false
                }
                if (name.text.isEmpty()){
                    name.setError("No puede dejar el campo vacío")
                    insert = false
                }
                if (lastname.text.isEmpty()){
                    lastname.setError("No puede dejar el campo vacío")
                    insert = false
                }
                if (nickname.text.isEmpty()){
                    nickname.setError("No puede dejar el campo vacío")
                    insert = false
                }
                if (telephone.text.isEmpty()){
                    telephone.setError("No puede dejar el campo vacío")
                    insert = false
                }
                if (address.text.isEmpty()){
                    address.setError("No puede dejar el campo vacío")
                    insert = false
                }
                if (!validarPassword(password.text.toString())){
                    password.setError("La contraseña debe tener al menos 8 caracteres, mayúsculas, minúsculas y números")
                    insert = false
                }
                if (insert){
                    insertUser(name.text.toString(), lastname.text.toString(), nickname.text.toString(), email.text.toString(), telephone.text.toString(), address.text.toString(), password.text.toString(), strEncodeImage)
                }
            }
            R.id.btnPicture_register->{ //CAMERA
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1002)
            }
            R.id.btnPicture_register2->{ //GALERY
                val cameraIntent = Intent(Intent.ACTION_PICK)
                cameraIntent.type = "image/*"
                startActivityForResult(cameraIntent, 1001)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 1001) { //GALERY
                imageUri = data!!.data
                image.setImageURI(imageUri)

                imgArray = contentResolver.openInputStream(imageUri!!)?.readBytes()
            }
            if (requestCode == 1002) { //CAMERA
                val photo =  data?.extras?.get("data") as Bitmap
                val stream = ByteArrayOutputStream()

                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                imgArray = stream.toByteArray()
                image!!.setImageBitmap(photo)
            }
        }
    }

    private fun insertUser(name: String, lastname: String, nickname: String, email: String, telephone: String, address: String, password: String, image: String) {
        mService.registerUser(name, lastname, nickname, email, telephone, address, password, image)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@RegisterActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {    //DENTRO DE ESTE ELSE SE PUEDE AGREGAR EL PASO AL INTERIOR DE LA APLICACION
                            Toast.makeText(this@RegisterActivity, response.body()!!.success_msg, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@RegisterActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }

                })

    }

    private fun validarEmail (email: String): Boolean {
        var pattern: Pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches()
    }

    private fun validarPassword (password: String): Boolean {
        val regex = """^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])\S{8,16}$""".toRegex()
        return regex.matches(password)
    }
}