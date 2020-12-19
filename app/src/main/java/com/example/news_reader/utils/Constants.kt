package com.example.news_reader.utils

// HTTP Error Response Codes
const val CODE400: String = "Bad Request. The request was unacceptable, often due to a missing or misconfigured parameter"
const val CODE401: String = "Unauthorized. Your API key was missing from the request, or wasn't correct"
const val CODE429: String = "Too Many Requests. You made too many requests within a window of time and have been rate limited. Back off for a while"
const val CODE500: String = "Server Error. Something went wrong on our side."