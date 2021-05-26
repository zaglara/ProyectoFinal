package com.g1e1.adoptgram.Fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.g1e1.adoptgram.*
import com.g1e1.adoptgram.Common.Credential
import com.g1e1.adoptgram.Common.UserApplication.Companion.prefs
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val credential: Credential = prefs.getCredentials()
        val root:View = inflater.inflate(R.layout.fragment_profile, container, false)
        root.findViewById<TextView>(R.id.lblUsername_profile).text = credential.user_nickname
        root.findViewById<TextView>(R.id.lblName_profile).text = credential.user_name + " " + credential.user_lastname
        root.findViewById<TextView>(R.id.lblEmail_profile).text = credential.user_email
        root.findViewById<TextView>(R.id.lblPhone_profile).text = credential.user_telephone

        var byteArray: ByteArray?
        val strImage:String = credential.user_image.replace("data:image/png;base64," , "")
        byteArray = Base64.getDecoder().decode(strImage)
        root.findViewById<ImageView>(R.id.imgProfilePic_profile)!!.setImageBitmap(BitmapFactory.decodeByteArray(byteArray,0,byteArray.size))

        root.findViewById<Button>(R.id.btnLogout_profile).setOnClickListener(this)
        root.findViewById<Button>(R.id.btnEdit_profile).setOnClickListener(this)
        root.findViewById<Button>(R.id.btnSaved_profile).setOnClickListener(this)

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnLogout_profile->{
                activity?.finish()
                val intent =  Intent(context, LoginActivity::class.java)
                val credential:Credential = Credential()
                credential.user_id = ""
                credential.user_name = ""
                credential.user_lastname = ""
                credential.user_nickname = ""
                credential.user_email = ""
                credential.user_telephone = ""
                credential.user_address = ""
                credential.user_image = ""
                prefs.saveCredentials(credential)
                startActivity(intent)
            }
            R.id.btnEdit_profile->{
                val intent =  Intent(context, EditProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.btnSaved_profile->{
                //val intent =  Intent(context, SavedPostsActivity::class.java)
                val intent = Intent(context, SavedPostsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}