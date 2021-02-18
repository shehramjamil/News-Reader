package com.example.news_reader.domain.models

data class NewsBusinessModel(
    var title: String?,
    var publishedDataTime: String,
    var imageURL: String?,
    var newsDescription: String?,
    var webURL : String?,
    var countryName: String
)