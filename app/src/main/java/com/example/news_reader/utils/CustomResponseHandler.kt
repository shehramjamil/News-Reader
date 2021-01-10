package com.example.news_reader.utils

data class CustomResponseHandler<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): CustomResponseHandler<T> {
            return CustomResponseHandler(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): CustomResponseHandler<T> {
            return CustomResponseHandler(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): CustomResponseHandler<T> {
            return CustomResponseHandler(Status.LOADING, data, null)
        }
    }
}