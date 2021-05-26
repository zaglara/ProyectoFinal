package com.g1e1.adoptgram

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.g1e1.adoptgram.Common.Credential
import com.g1e1.adoptgram.Common.UserApplication.Companion.prefs
import kotlinx.android.synthetic.main.activity_main_navigation.*
import com.g1e1.adoptgram.Fragments.CategoriesFragment
import com.g1e1.adoptgram.Fragments.HomeFragment
import com.g1e1.adoptgram.Fragments.MyPostsFragment
import com.g1e1.adoptgram.Fragments.ProfileFragment


class MainNavigationActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)

        val credential:Credential = prefs.getCredentials()

        Toast.makeText(this, "Bienvenido(a) " + credential.user_name + " " + credential.user_lastname, Toast.LENGTH_SHORT).show()

        //Instanciamos los fragmentos
        val homeFragment = HomeFragment()
        val categoriesFragment = CategoriesFragment()
        val myPostsFragment = MyPostsFragment()
        val profileFragment = ProfileFragment()

        //Hacemos de las notificaciones el fragmento actual al inciar
        makeCurrentFragment(homeFragment)

        this.nav_bottom_menu_ctrl.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_ic_home -> makeCurrentFragment(homeFragment)
                R.id.menu_ic_category -> makeCurrentFragment(categoriesFragment)
                R.id.menu_ic_published -> makeCurrentFragment(myPostsFragment)
                R.id.menu_ic_profile -> makeCurrentFragment(profileFragment)
            }
            true

        }


    }


    //Función para cambiar el fragmeto actual
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}