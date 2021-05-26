package com.g1e1.adoptgram.Data

class Especie (
    var id:Int, var especie:String
){
    override fun toString(): String {
        return this.especie
    }
}

class Post(
    var id:Int?=null,
    var descripcion:String?=null,
    var edad:String?=null,
    var detalles:String?=null,
    var especie:Especie?=null,
    var foto1:ByteArray?=null,
    var foto2:ByteArray?=null,
    var foto3:ByteArray?=null,
    var foto4:ByteArray?=null,
    var foto5:ByteArray?=null,
    var foto6:ByteArray?=null,
    var id_usuario:String?=null
){}