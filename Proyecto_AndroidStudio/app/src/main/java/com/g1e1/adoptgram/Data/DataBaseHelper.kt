package com.g1e1.adoptgram.Data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.lang.Exception

class DataBaseHelper (var context: Context): SQLiteOpenHelper(context,SetDB.DB_NAME,null,SetDB.DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        //CREAR BASE DE DATOS
        try {
            val createPostTable:String ="CREATE TABLE " + SetDB.tblPost.TABLE_NAME + " (" +
                    SetDB.tblPost.COL_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.tblPost.COL_DESCRIPCION + " TEXT," +
                    SetDB.tblPost.COL_EDAD + " VARCHAR(15)," +
                    SetDB.tblPost.COL_DETALLES + " TEXT," +
                    SetDB.tblPost.COL_ESPECIE + " INTEGER," +
                    SetDB.tblPost.COL_IMG + " BLOB," +
                    SetDB.tblPost.COL_IMG_2 + " BLOB," +
                    SetDB.tblPost.COL_IMG_3 + " BLOB," +
                    SetDB.tblPost.COL_IMG_4 + " BLOB," +
                    SetDB.tblPost.COL_IMG_5 + " BLOB," +
                    SetDB.tblPost.COL_IMG_6 + " BLOB," +
                    SetDB.tblPost.COL_NICKNAME + " INTEGER)"

            db?.execSQL(createPostTable)

            val createEspecieTable:String ="CREATE TABLE " + SetDB.tblEspecie.TABLE_NAME + " (" +
                    SetDB.tblEspecie.COL_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.tblEspecie.COL_ESPECIE + " VARCHAR(15))"

            db?.execSQL(createEspecieTable)

        }catch (e: Exception){
            Log.e("Execption", e.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    public fun insertPost(post:Post):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblPost.COL_DESCRIPCION,post.descripcion)
        values.put(SetDB.tblPost.COL_EDAD,post.edad)
        values.put(SetDB.tblPost.COL_DETALLES,post.detalles)
        values.put(SetDB.tblPost.COL_ESPECIE,post.especie!!.id)
        values.put(SetDB.tblPost.COL_IMG,post.foto1)
        values.put(SetDB.tblPost.COL_IMG_2,post.foto2)
        values.put(SetDB.tblPost.COL_IMG_3,post.foto3)
        values.put(SetDB.tblPost.COL_IMG_4,post.foto4)
        values.put(SetDB.tblPost.COL_IMG_5,post.foto5)
        values.put(SetDB.tblPost.COL_IMG_6,post.foto6)
        values.put(SetDB.tblPost.COL_NICKNAME,post.id_usuario)

        try {
            val result =  dataBase.insert(SetDB.tblPost.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    public fun getListOfPost(userId: String):MutableList<Post>{
        val List:MutableList<Post> = ArrayList()

        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> =  arrayOf(SetDB.tblPost.COL_ID,
                SetDB.tblPost.COL_DESCRIPCION,
                SetDB.tblPost.COL_EDAD,
                SetDB.tblPost.COL_DETALLES,
                SetDB.tblPost.COL_ESPECIE,
                SetDB.tblPost.COL_IMG,
                SetDB.tblPost.COL_IMG_2,
                SetDB.tblPost.COL_IMG_3,
                SetDB.tblPost.COL_IMG_4,
                SetDB.tblPost.COL_IMG_5,
                SetDB.tblPost.COL_IMG_6,
                SetDB.tblPost.COL_NICKNAME)

        val data =  dataBase.query(SetDB.tblPost.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                SetDB.tblPost.COL_ID + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                val post:Post = Post()
                post.id = data.getString(data.getColumnIndex(SetDB.tblPost.COL_ID)).toInt()
                post.descripcion =  data.getString(data.getColumnIndex(SetDB.tblPost.COL_DESCRIPCION)).toString()
                post.edad = data.getString(data.getColumnIndex(SetDB.tblPost.COL_EDAD)).toString()
                post.detalles =  data.getString(data.getColumnIndex(SetDB.tblPost.COL_DETALLES)).toString()
                post.especie = DataManager.especies[(data.getString(data.getColumnIndex(SetDB.tblPost.COL_ESPECIE)).toInt() - 1)]
                post.foto1 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG))
                post.foto2 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_2))
                post.foto3 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_3))
                post.foto4 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_4))
                post.foto5 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_5))
                post.foto6 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_6))
                post.id_usuario = data.getString(data.getColumnIndex(SetDB.tblPost.COL_NICKNAME)).toString()
                if (userId == post.id_usuario)
                    List.add(post)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  List
    }

    public fun getPost(intID:Int):Post?{
        var post:Post? = null
        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> =  arrayOf(SetDB.tblPost.COL_ID,
            SetDB.tblPost.COL_DESCRIPCION,
            SetDB.tblPost.COL_EDAD,
            SetDB.tblPost.COL_DETALLES,
            SetDB.tblPost.COL_ESPECIE,
            SetDB.tblPost.COL_IMG,
            SetDB.tblPost.COL_IMG_2,
            SetDB.tblPost.COL_IMG_3,
            SetDB.tblPost.COL_IMG_4,
            SetDB.tblPost.COL_IMG_5,
            SetDB.tblPost.COL_IMG_6,
            SetDB.tblPost.COL_NICKNAME)

        val where:String =  SetDB.tblPost.COL_ID + "= ${intID.toString()}"

        val data =  dataBase.query(SetDB.tblPost.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            SetDB.tblPost.COL_ID + " ASC")

        if(data.moveToFirst()){
            post = Post()
            post.id = data.getString(data.getColumnIndex(SetDB.tblPost.COL_ID)).toInt()
            post.descripcion =  data.getString(data.getColumnIndex(SetDB.tblPost.COL_DESCRIPCION)).toString()
            post.edad = data.getString(data.getColumnIndex(SetDB.tblPost.COL_EDAD)).toString()
            post.detalles =  data.getString(data.getColumnIndex(SetDB.tblPost.COL_DETALLES)).toString()
            post.especie = DataManager.especies[(data.getString(data.getColumnIndex(SetDB.tblPost.COL_ESPECIE)).toInt() - 1)]
            post.foto1 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG))
            post.foto2 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_2))
            post.foto3 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_3))
            post.foto4 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_4))
            post.foto5 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_5))
            post.foto6 = data.getBlob(data.getColumnIndex(SetDB.tblPost.COL_IMG_6))
            post.id_usuario = data.getString(data.getColumnIndex(SetDB.tblPost.COL_NICKNAME)).toString()

        }

        data.close()
        return post
    }

    public fun updatePost(post: Post):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblPost.COL_DESCRIPCION,post.descripcion)
        values.put(SetDB.tblPost.COL_EDAD,post.edad)
        values.put(SetDB.tblPost.COL_DETALLES,post.detalles)
        values.put(SetDB.tblPost.COL_ESPECIE,post.especie!!.id)
        values.put(SetDB.tblPost.COL_IMG,post.foto1)
        values.put(SetDB.tblPost.COL_IMG_2,post.foto2)
        values.put(SetDB.tblPost.COL_IMG_3,post.foto3)
        values.put(SetDB.tblPost.COL_IMG_4,post.foto4)
        values.put(SetDB.tblPost.COL_IMG_5,post.foto5)
        values.put(SetDB.tblPost.COL_IMG_6,post.foto6)
        values.put(SetDB.tblPost.COL_NICKNAME,post.id_usuario)

        val where:String =  SetDB.tblPost.COL_ID + "=?"

        try{

            val result =  dataBase.update(SetDB.tblPost.TABLE_NAME,
                values,
                where,
                arrayOf(post.id.toString()))

            if (result != -1 ) {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
        }

        dataBase.close()
        return  boolResult
    }

    public fun deletePost(id:Int):Boolean{
        val db = this.writableDatabase
        var boolResult:Boolean =  false
        try{

            val where:String =  SetDB.tblPost.COL_ID + "=?"
            val _success = db.delete(SetDB.tblPost.TABLE_NAME, where, arrayOf(id.toString()))
            db.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
        }

        return  boolResult
    }

}