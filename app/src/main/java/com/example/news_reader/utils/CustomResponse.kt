package com.example.news_reader.utils

data class CustomResponse<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): CustomResponse<T> {
            return CustomResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): CustomResponse<T> {
            return CustomResponse(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): CustomResponse<T> {
            return CustomResponse(Status.LOADING, data, null)
        }
    }
}