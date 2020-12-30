package com.leva.kmm.shared.domain.usecase

import com.leva.kmm.shared.domain.model.Response
import com.leva.kmm.shared.domain.model.RocketLaunch
import com.leva.kmm.shared.domain.model.Status
import com.leva.kmm.shared.mapper.RocketLaunchMapper
import com.leva.kmm.shared.repository.RocketRespository

class GetRocketListUseCase(
    private val rRepository: RocketRespository,
    private var forceReload: Boolean = true
) {
    private val mapper = RocketLaunchMapper()
    fun setForceReload(v: Boolean) {
        forceReload = v
    }

    suspend fun execute(): Response {
        try {
            val cachedLaunches = rRepository.getAllCachedLaunches()
            return if (cachedLaunches.isNotEmpty() && !forceReload) {
                Response(Status.SUCCESSFUL, mapper.entityToDomain(cachedLaunches), "")
            } else {
                Response(Status.SUCCESSFUL, mapper.dtoToDomain(
                    rRepository.getAllLaunches().also {
                        rRepository.clearDb()
                        rRepository.createLaunches(
                            mapper.dtoToEntity(it)
                        )
                    }
                ), "")

            }
        } catch (exception: Exception) {
            return Response(status = Status.ERROR, errorMessage = exception.message ?: "Error")
        }
    }

}