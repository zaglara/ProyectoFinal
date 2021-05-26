package com.g1e1.adoptgram.Model

class APIResponse {
    var error:Boolean=false
    var error_msg:String?=null
    var success_msg:String?=null
    var user:Usuario?=null
    var message:String?=null
    var especie:Especie?=null
    var especies:List<Especie>?=null
    var posts:List<Post>?=null
    var post:Post?=null
    var postImages:List<PostImage>?=null
    var comments:List<Comment>?=null
}