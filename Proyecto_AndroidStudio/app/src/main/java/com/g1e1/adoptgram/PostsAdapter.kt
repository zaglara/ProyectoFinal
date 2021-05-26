package com.g1e1.adoptgram

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.g1e1.adoptgram.Model.Post
import kotlinx.android.synthetic.main.item_pet_card.view.*
import java.util.*

class PostsAdapter(private val mContext: Context, private val listPosts: List<Post>) : ArrayAdapter<Post> (mContext, 0, listPosts) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_pet_card, parent, false)
        val post = listPosts[position]

        layout.lblEspecie_petCard.text = post.especie
        layout.lblDescripcion_petCard.text = post.descripcion
        layout.lblEdad_petCard.text = post.edad
        var byteArray: ByteArray?
        val strImage:String = post.foto!!.replace("data:image/png;base64," , "")
        byteArray = Base64.getDecoder().decode(strImage)
        layout.imgPic_petCard.setImageBitmap(BitmapFactory.decodeByteArray(byteArray,0,byteArray.size))

        return layout
    }
}