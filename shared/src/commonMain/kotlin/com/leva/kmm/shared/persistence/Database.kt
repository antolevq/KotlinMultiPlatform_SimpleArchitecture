package com.leva.kmm.shared.persistence

import com.leva.kmm.shared.persistence.entity.LinksEntity
import com.leva.kmm.shared.persistence.entity.RocketEntity
import com.leva.kmm.shared.persistence.entity.RocketLaunchEntity

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllRockets()
            dbQuery.removeAllLaunches()
        }
    }

    internal fun getAllLaunches(): List<RocketLaunchEntity> {
        return dbQuery.selectAllLaunchesInfo(::mapLaunchSelecting).executeAsList()
    }

    private fun mapLaunchSelecting(
        flightNumber: Long,
        missionName: String,
        launchYear: Int,
        rocketId: String,
        details: String?,
        launchSuccess: Boolean?,
        launchDateUTC: String,
        missionPatchUrl: String?,
        articleUrl: String?,
        rocket_id: String?,
        name: String?,
        type: String?
    ): RocketLaunchEntity {
        return RocketLaunchEntity(
            flightNumber = flightNumber.toInt(),
            missionName = missionName,
            launchYear = launchYear,
            details = details,
            launchDateUTC = launchDateUTC,
            launchSuccess = launchSuccess,
            rocketEntity = RocketEntity(
                id = rocketId,
                name = name!!,
                type = type!!
            ),
            linksEntity = LinksEntity(
                missionPatchUrl = missionPatchUrl,
                articleUrl = articleUrl
            )
        )
    }

    internal fun createLaunches(launchEntities: List<RocketLaunchEntity>) {
        dbQuery.transaction {
            launchEntities.forEach { launch ->
                val rocket = dbQuery.selectRocketById(launch.rocketEntity.id).executeAsOneOrNull()
                if (rocket == null) {
                    insertRocket(launch)
                }

                insertLaunch(launch)
            }
        }
    }

    private fun insertRocket(launchEntity: RocketLaunchEntity) {
        dbQuery.insertRocket(
            id = launchEntity.rocketEntity.id,
            name = launchEntity.rocketEntity.name,
            type = launchEntity.rocketEntity.type
        )
    }

    private fun insertLaunch(launchEntity: RocketLaunchEntity) {
        dbQuery.insertLaunch(
            flightNumber = launchEntity.flightNumber.toLong(),
            missionName = launchEntity.missionName,
            launchYear = launchEntity.launchYear,
            rocketId = launchEntity.rocketEntity.id,
            details = launchEntity.details,
            launchSuccess = launchEntity.launchSuccess ?: false,
            launchDateUTC = launchEntity.launchDateUTC,
            missionPatchUrl = launchEntity.linksEntity.missionPatchUrl,
            articleUrl = launchEntity.linksEntity.articleUrl
        )
    }
}