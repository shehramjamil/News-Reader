package com.example.news_reader.data.model.json

// Expected Json Format must match this class, otherwise GSON Conversion Exception occurs

data class NewsResponse(val totalResults: Int = 0,
                        val articles: ArrayList<ArticlesItem>,
                        val status: String = "")

data class ArticlesItem(val publishedAt: String = "",
                        val author: String = "",
                        val urlToImage: String = "",
                        val description: String = "",
                        val source: Source,
                        val title: String = "",
                        val url: String = "",
                        val content: String = "")

data class Source(val name: String = "",
                  val id: String ? = null)