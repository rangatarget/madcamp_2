package com.example.madcamp_2

import java.io.Serializable

data class BoardModel(
    var id: String,
    var author: String,
    var title: String,
    var content: String,
    var classify: String
)

data class BoardClassModel(
    var name: String,
    var creater: String
) : Serializable