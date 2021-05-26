package com.g1e1.adoptgram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.g1e1.adoptgram.Common.Common
import com.g1e1.adoptgram.Common.Credential
import com.g1e1.adoptgram.Common.UserApplication.Companion.prefs
import com.g1e1.adoptgram.Model.APIResponse
import com.g1e1.adoptgram.Model.Usuario
import com.g1e1.adoptgram.Remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    //Variable del API
    internal lateinit var mService: IMyAPI
    val email: TextView get() = findViewById<TextView>(R.id.txtCorreo_login)
    val password: TextView get() = findViewById<TextView>(R.id.txtPassword_login)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val credential:Credential = prefs.getCredentials()
        //credential.user_name = ""
        if(!credential.user_id.isEmpty() && !credential.user_name.isEmpty() && !credential.user_lastname.isEmpty() && !credential.user_nickname.isEmpty() && !credential.user_email.isEmpty() && !credential.user_telephone.isEmpty() && !credential.user_address.isEmpty() && !credential.user_image.isEmpty()){
            finish()
            val intent =  Intent(this@LoginActivity, MainNavigationActivity::class.java)
            startActivity(intent)
        }


        //Inicializar API
        mService = Common.api

        //Activamos el OnClickListener para los controles
        val btnLogin_login =  findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnLogin_login)
        val btnReister_login =  findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnRegister_login)
        btnLogin_login.setOnClickListener(this)
        btnReister_login.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            //Click a los botónes
            R.id.btnLogin_login->{
                var enter: Boolean = true
                if (email.text.isEmpty()){
                    email.setError("No puede dejar el campo vacío")
                    enter = false
                }
                if (password.text.isEmpty()){
                    password.setError("No puede dejar el campo vacío")
                    enter = false
                }
                if (enter)
                    loginUser(email.text.toString(), password.text.toString())
            }
            R.id.btnRegister_login->callRegisterActivity()
        }
    }


    //Funicón para llamar a la Actividad de Navegación Principal
    private fun loginUser(email: String, password: String){
        mService.loginUser(email, password)
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(this@LoginActivity, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {
                            finish()
                            val intent =  Intent(this@LoginActivity, MainNavigationActivity::class.java)
                            /*intent.putExtra("user_id", response.body()!!.user!!.id)*/
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
                            startActivity(intent)
                        }
                    }
                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(this@LoginActivity, t!!.message, Toast.LENGTH_SHORT).show()
                    }

                })

    }

    //Función para llamar a la Actividad del Formulario del Registro
    private fun callRegisterActivity(){
        val intent =  Intent(this, RegisterActivity::class.java)
        startActivity(intent)

    }
}