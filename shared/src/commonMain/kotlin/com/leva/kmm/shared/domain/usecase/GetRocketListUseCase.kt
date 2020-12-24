package com.leva.kmm.shared.domain.usecase

import com.leva.kmm.shared.domain.model.Result
import com.leva.kmm.shared.domain.model.RocketLaunch
import com.leva.kmm.shared.mapper.RocketLaunchMapper
import com.leva.kmm.shared.network.api.SpaceXApi
import com.leva.kmm.shared.repository.RocketRespository

class GetRocketListUseCase(
    private val rRepository: RocketRespository,
    private var forceReload: Boolean = true
) {
    private val mapper = RocketLaunchMapper()
    fun setForceReload(v: Boolean) {
        forceReload = v
    }
    suspend fun execute(): Result<List<RocketLaunch>> {
        try {
            val cachedLaunches = rRepository.getAllCachedLaunches()
            return if (cachedLaunches.isNotEmpty() && !forceReload) {
                Result.Success(mapper.entityToDomain(cachedLaunches))
            } else {
                Result.Success(
                    mapper.dtoToDomain(
                        rRepository.getAllLaunches().also {
                            rRepository.clearDb()
                            rRepository.createLaunches(
                                mapper.dtoToEntity(it)
                            )
                        }
                    )
                )
            }
        } catch (exception: Exception) {
            return Result.Error(exception.message ?:"Error")
        }
    }

}