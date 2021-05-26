package com.g1e1.adoptgram.Data

//Clase para definir el esquema de la base de datos
class SetDB {
    companion object{
        //Nombre de la base de datos y versión
        val DB_NAME = "AdoptgramLocalDB"
        val DB_VERSION = 1
    }

    //Definimos el esquema de las tablas
    abstract class tblPost{
        //DEFINIMOS LOS ATRIBUTOS DE LA CLASE USANDO CONTANTES
        companion object{
            val TABLE_NAME = "Posts"
            val COL_ID =  "intID"
            val COL_DESCRIPCION =  "strDescripcion"
            val COL_EDAD = "strEdad"
            val COL_DETALLES =  "strDetalles"
            val COL_ESPECIE = "intEspecie"
            val COL_IMG =  "imgArray" // byte Array image
            val COL_IMG_2 =  "imgArray2"
            val COL_IMG_3 =  "imgArray3"
            val COL_IMG_4 =  "imgArray4"
            val COL_IMG_5 =  "imgArray5"
            val COL_IMG_6 =  "imgArray6"
            val COL_NICKNAME =  "intNickname"
        }
    }

    abstract class tblEspecie{
        companion object{
            val TABLE_NAME = "Especies"
            val COL_ID =  "intID"
            val COL_ESPECIE =  "strEspecie"
        }
    }
}