package com.g1e1.adoptgram.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.g1e1.adoptgram.Common.Common
import com.g1e1.adoptgram.Model.APIResponse
import com.g1e1.adoptgram.Model.Post
import com.g1e1.adoptgram.PetPageActivity
import com.g1e1.adoptgram.PostsAdapter
import com.g1e1.adoptgram.R
import com.g1e1.adoptgram.Remote.IMyAPI
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    internal lateinit var mService: IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root:View = inflater.inflate(R.layout.fragment_home, container, false)
        mService = Common.api
        getMostRecent()

        return root
    }

    override fun onResume() {
        super.onResume()
        getMostRecent()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun getMostRecent() {
        mService.getMostRecent()
                .enqueue(object : Callback<APIResponse> {
                    override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                        if (response!!.body()!!.error)
                            Toast.makeText(context, response!!.body()!!.error_msg, Toast.LENGTH_SHORT).show()
                        else {
                            val arrayPosts = response.body()!!.posts
                            val listaPosts: MutableList<Post> = ArrayList()
                            for (item in arrayPosts!!) {
                                listaPosts.add(item)
                            }
                            val adapter = PostsAdapter(context!!, listaPosts)
                            listGuardados.adapter = adapter
                            listGuardados.setOnItemClickListener { parent, view, position, id ->
                                val intent = Intent(context, PetPageActivity::class.java)
                                intent.putExtra("id", listaPosts[position].id)
                                startActivity(intent)
                            }
                        }
                    }

                    override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                        Toast.makeText(context, t!!.message, Toast.LENGTH_SHORT).show()
                    }
                })

    }

}