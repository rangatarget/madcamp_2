package com.example.madcamp_2

import java.io.Serializable

data class BoardModel(
    var _id: Int,
    var author: String,
    var title: String,
    var context: String,
    var author_nickname: String
) : Serializable

data class BoardClassModel(
    var name: String,
    var creater: String,
    var pinned: Boolean
) : Serializable

data class BoardName(
    var name : String
)

data class Createboardclass(
    var newtitle: String,
    var creater: String
)

data class CreateboardclassResponse(
    var success: Boolean
)

data class getboardclass(
    var user_id: String
)

data class Checkedboardclass(
    var id: String
)
data class getcomment(
    var _id: Int
)
data class Comment(
    var writer: String,
    var context: String,
    var writer_nickname: String,
    var _id: Int,
    var image: String
)

data class boardcreate(
    var author: String,
    var author_nickname: String,
    var title: String,
    var context: String,
    var boardclass: String
)

data class commentcreate(
    var writer: String,
    var writer_nickname: String,
    var context: String,
    var _id: Int
)

data class deletecomment(
    var _id: Int
)

data class updatecomment(
    var _id: Int,
    var context: String
)

data class deletepost(
    var _id: Int
)

data class update_post(
    var _id: Int,
    var title: String,
    var context: String
)

data class getmy(
    var id: String
)

data class deleteboardclass(
    var name: String
)

data class pinboardclass(
    var user_id: String,
    var pinned: Boolean,
    var boardclass: String
)

data class onlyimage(
    var image: String
)