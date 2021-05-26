package com.g1e1.adoptgram

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.g1e1.adoptgram.Common.Common
import com.g1e1.adoptgram.Common.Credential
import com.g1e1.adoptgram.Common.UserApplication.Companion.prefs
import com.g1e1.adoptgram.Model.APIResponse
import com.g1e1.adoptgram.Remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var mService: IMyAPI

    val name: TextView get() = findViewById<TextView>(R.id.txtNombre_editProfile)
    val lastname: TextView get() = findViewById<TextView>(R.id.txtApellido_editProfile)
    val nickname: TextView get() = findViewById<TextView>(R.id.txtUsuario_editProfile)
    val telephone: TextView get() = findViewById<TextView>(R.id.txtTelefono_editProfile)
    val address: TextView get() = findViewById<TextView>(R.id.txtDireccion_editProfile)
    val password: TextView get() = findViewById<TextView>(R.id.txtContrasena_editProfile)
    val password2: TextView get() = findViewById<TextView>(R.id.txtContrasena2_editProfile)
    val image: ImageView get() = findViewById<ImageView>(R.id.imgImagen_editProfile)

    private var imgArray:ByteArray?=null
    private var imageUri: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        mService = Common.api

        val btnGuardar_editProfile =  findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnGuardar_editProfile)
        val btnActualizar_editProfile =  findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnActualizar_editProfile)
        btnGuardar_editProfile.setOnClickListener(this)
        btnActualizar_editProfile.setOnClickListener(this)
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnImagenPic_editProfile).setOnClickListener(this)
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnImagenPic_editProfile2).setOnClickListener(this)

        val credential:Credential = prefs.getCredentials()
        name.text = credential.user_name
        lastname.text = credential.user_lastname
        nickname.text = credential.user_nickname
        telephone.text = credential.user_telephone
        address.text = credential.user_address

        var byteArray: ByteArray?
        val strImage:String = credential.user_image.replace("data:image/png;base64," , "")
        byteArray = Base64.getDecoder().decode(strImage)
        image!!.setImageBitmap(BitmapFactory.decodeByteArray(byteArray,0,byteArray.size))
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnGuardar_editProfile->{
                val credential:Credential = prefs.getCredentials()
                var update:Boolean = true
                var withImage:Boolean = false
                var strEncodeImage:String = ""
                if (imgArray != null){
                    withImage = true
                    val encodedString:String = Base64.getEncoder().encodeToString(this.imgArray)
                    strEncodeImage = "data:image/png;base64," + encodedString
                }
                if (name.text.isEmpty()){
                    name.setError("No puede dejar el campo vacío")
                    update = false
                }
                if (lastname.text.isEmpty()){
                    lastname.setError("No puede dejar el campo vacío")
                    update = false
                }
                if (nickname.text.isEmpty()){
                    nickname.setError("No puede dejar el campo vacío")
                    update = false
                }
                if (telephone.text.isEmpty()){
                    telephone.setError("No puede dejar el campo vacío")
                    update = false
                }
                if (address.text.isEmpty()){
                    address.setError("No puede dejar el campo vacío")
                    update = false
                }
                if (update && withImage){
                    updateUser(credential.user_id, name.text.toString(), lastname.text.toString(), nickname.text.toString(), telephone.text.toString(), address.text.toString(), strEncodeImage)
                }
                else if (update && !withImage){
                    updateUser2(credential.user_id, name.text.toString(), lastname.text.toString(), nickname.text.toString(), telephone.text.toString(), address.text.toString())
                }
            }
            R.id.btnActualizar_editProfile->{
                val credential:Credential = prefs.getCredentials()
                var update:Boolean = true
                if (password.text.isEmpty()){
                    password.setError("No puede dejar el campo vacío")
                    update = false
                }
                else if (validarPassword(password.text.toString())){
                    if (!equalPassword(password.text.toString(), password2.text.toString())){
                        //updatePassword (credential.user_id, password.text.toString())
                        password2.setError("Las contraseñas no coinciden")
                        update = false
                    }
                }
                else {
                    password.setError("La contraseña debe tener al menos 8 caracteres, mayúsculas, minúsculas y números")
                    update = false
                }
                if (password2.text.isEmpty()){
                    password2.setError("No puede dejar el campo vacío")
                    update = false
                }
                if (update){
                    updatePassword (credential.user_id, password.text.toString())
                }
            }
            R.id.btnImagenPic_editProfile->{//CAMERA
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1002)
            }
            R.id.btnImagenPic_editProfile2->{//GALLERY
                val cameraIntent = Intent(Intent.ACTION_PICK)
                cameraIntent.type = "image/*"
                startActivityForResult(cameraIntent, 1001)
            }
        }
    }

    private fun updateUser(id: String, name: String, lastname: String, nickname: String, telephone: String, address: String, image: String) {
        mService.updateUser(id, name, lastname, nickname, telephone, address, image)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@EditProfileActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {    //DENTRO DE ESTE ELSE SE PUEDE AGREGAR EL PASO AL INTERIOR DE LA APLICACION
                            Toast.makeText(this@EditProfileActivity, response.body()!!.success_msg, Toast.LENGTH_SHORT).show()
                            val credential:Credential = Credential()
                            credential.user_id = response.body()!!.user!!.id!!
                            credential.user_name = response.body()!!.user!!.name!!
                            credential.user_lastname = response.body()!!.user!!.lastname!!
                            credential.user_nickname = response.body()!!.user!!.nickname!!
                            credential.user_email = response.body()!!.user!!.email!!
                            credential.user_telephone = response.body()!!.user!!.telephone!!
                            credential.user_address = response.body()!!.user!!.address!!
                            credential.user_image = response.body()!!.user!!.image!!
                            prefs.saveCredentials(credential)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@EditProfileActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }

                })

    }

    private fun updateUser2(id: String, name: String, lastname: String, nickname: String, telephone: String, address: String) {
        mService.updateUser2(id, name, lastname, nickname, telephone, address)
            .enqueue(object : Callback<APIResponse> {
                override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                    if (response!!.body()!!.error)
                        Toast.makeText(this@EditProfileActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                    else {    //DENTRO DE ESTE ELSE SE PUEDE AGREGAR EL PASO AL INTERIOR DE LA APLICACION
                        Toast.makeText(this@EditProfileActivity, response.body()!!.success_msg, Toast.LENGTH_SHORT).show()
                        val credential:Credential = Credential()
                        credential.user_id = response.body()!!.user!!.id!!
                        credential.user_name = response.body()!!.user!!.name!!
                        credential.user_lastname = response.body()!!.user!!.lastname!!
                        credential.user_nickname = response.body()!!.user!!.nickname!!
                        credential.user_email = response.body()!!.user!!.email!!
                        credential.user_telephone = response.body()!!.user!!.telephone!!
                        credential.user_address = response.body()!!.user!!.address!!
                        credential.user_image = response.body()!!.user!!.image!!
                        prefs.saveCredentials(credential)
                        finish()
                    }
                }
                override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                    Toast.makeText(this@EditProfileActivity, t!!.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun updatePassword(id: String, password: String) {
        mService.updatePassword(id, password)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@EditProfileActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {    //DENTRO DE ESTE ELSE SE PUEDE AGREGAR EL PASO AL INTERIOR DE LA APLICACION
                            Toast.makeText(this@EditProfileActivity, response.body()!!.success_msg, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@EditProfileActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }

                })

    }

    private fun validarPassword (password: String): Boolean {
        val regex = """^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])\S{8,16}$""".toRegex()
        return regex.matches(password)
    }

    private fun equalPassword (password: String, password2: String): Boolean {
        if (password == password2)
            return true
        return false
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

}