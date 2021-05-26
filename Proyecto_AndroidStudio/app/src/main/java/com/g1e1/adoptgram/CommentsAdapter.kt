package com.g1e1.adoptgram

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.g1e1.adoptgram.Model.Comment
import kotlinx.android.synthetic.main.item_comment_card.view.*
import java.util.*

class CommentsAdapter(private val mContext: Context, private val listComments: List<Comment>) : ArrayAdapter<Comment>(mContext, 0, listComments) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_comment_card, parent, false)
        val comment = listComments[position]

        layout.lblUsuario_commentCard.text = comment.nickname
        layout.lblComentario_commentCard.text = comment.comentario
        var byteArray: ByteArray?
        val strImage:String = comment.foto!!.replace("data:image/png;base64," , "")
        byteArray = Base64.getDecoder().decode(strImage)
        layout.imgPic_commentCard.setImageBitmap(BitmapFactory.decodeByteArray(byteArray,0,byteArray.size))

        return layout
    }
}