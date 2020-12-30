package com.leva.kmm.shared.domain.model

data class Response<Data>(
    var status: Status,
    var data: Data? = null,
    var errorMessage: String? = null
)

enum class Status { SUCCESSFUL, ERROR, LOADING }


