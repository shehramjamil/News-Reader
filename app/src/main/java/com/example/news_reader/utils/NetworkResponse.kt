package com.example.news_reader.utils

data class NetworkResponse<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): NetworkResponse<T> {
            return NetworkResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): NetworkResponse<T> {
            return NetworkResponse(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): NetworkResponse<T> {
            return NetworkResponse(Status.LOADING, data, null)
        }
    }
}