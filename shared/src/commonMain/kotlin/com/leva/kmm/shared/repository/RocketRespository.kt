package com.leva.kmm.shared.repository

import com.leva.kmm.shared.domain.model.RocketLaunch
import com.leva.kmm.shared.mapper.RocketLaunchMapper
import com.leva.kmm.shared.network.api.SpaceXApi
import com.leva.kmm.shared.network.dto.RocketLaunchDTO
import com.leva.kmm.shared.persistence.Database
import com.leva.kmm.shared.persistence.DatabaseDriverFactory
import com.leva.kmm.shared.persistence.entity.RocketEntity
import com.leva.kmm.shared.persistence.entity.RocketLaunchEntity

class RocketRespository(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()


    fun getAllCachedLaunches(): List<RocketLaunchEntity> {
        return database.getAllLaunches()
    }

    suspend fun getAllLaunches(): List<RocketLaunchDTO> {
        return api.getAllLaunches()
    }

    fun clearDb() {
        database.clearDatabase()
    }
    fun createLaunches(list: List<RocketLaunchEntity>) {
        database.createLaunches(list)
    }
}