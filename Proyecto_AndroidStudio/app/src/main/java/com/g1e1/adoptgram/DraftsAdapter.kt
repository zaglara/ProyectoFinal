package com.g1e1.adoptgram

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.g1e1.adoptgram.Common.UserApplication
import com.g1e1.adoptgram.Data.Post
import kotlinx.android.synthetic.main.item_own_pet_card.view.*

class DraftsAdapter(private val mContext: Context, private val listDrafts: List<Post>) : ArrayAdapter<Post>(mContext, 0, listDrafts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_own_pet_card, parent, false)
        val draft = listDrafts[position]

        layout.lblEspecie_ownpetCard.text = draft.especie!!.especie
        layout.lblDescripcion_ownpetCard.text = draft.descripcion
        layout.lblEdad_ownpetCard.text = draft.edad
        layout.imgPic_ownpetCard.setImageBitmap(BitmapFactory.decodeByteArray(draft.foto1,0,draft.foto1!!.size))

        layout.btnEditar_ownpetCard.setOnClickListener{
            Toast.makeText(mContext, "Editar borrador", Toast.LENGTH_SHORT).show()
            val intent =  Intent(mContext, CreatePostActivity::class.java)
            intent.putExtra("idAux", draft.id)
            mContext.startActivity(intent)
        }
        layout.btnEliminar_ownpetCard.setOnClickListener{
            Toast.makeText(mContext, "Borrador eliminado",  Toast.LENGTH_SHORT).show()
            UserApplication.dbHelper.deletePost(draft.id!!)
            (mContext as Activity).finish()
            val intent =  Intent(mContext, SavedPostsActivity::class.java)
            intent.putExtra("uso", "borradores")
            mContext.startActivity(intent)
        }

        return layout
    }

}