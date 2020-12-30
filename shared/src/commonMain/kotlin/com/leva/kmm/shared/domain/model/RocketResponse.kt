package com.leva.kmm.shared.domain.model

data class Response(
    var status: Status,
    var data: List<RocketLaunch>? = null,
    var errorMessage: String? = null
)

enum class Status { SUCCESSFUL, ERROR, LOADING }


