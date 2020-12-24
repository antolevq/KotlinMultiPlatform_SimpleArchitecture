package com.leva.kmm.shared.mapper

import com.leva.kmm.shared.domain.model.Links
import com.leva.kmm.shared.domain.model.Rocket
import com.leva.kmm.shared.domain.model.RocketLaunch
import com.leva.kmm.shared.network.dto.RocketDTO
import com.leva.kmm.shared.network.dto.RocketLaunchDTO
import com.leva.kmm.shared.persistence.entity.LinksEntity
import com.leva.kmm.shared.persistence.entity.RocketEntity
import com.leva.kmm.shared.persistence.entity.RocketLaunchEntity

class RocketLaunchMapper() {
    fun entityToDomain(from: RocketLaunchEntity): RocketLaunch =
        RocketLaunch(
            flightNumber = from.flightNumber,
            missionName = from.missionName,
            launchYear = from.launchYear,
            launchDateUTC = from.launchDateUTC,
            rocket = Rocket(
                id = from.rocketEntity.id,
                name = from.rocketEntity.name,
                type = from.rocketEntity.type
            ),
            details = from.details,
            launchSuccess = from.launchSuccess,
            links = Links(
                missionPatchUrl = from.linksEntity.missionPatchUrl,
                articleUrl = from.linksEntity.articleUrl
            )
        )

    fun entityToDomain(from: List<RocketLaunchEntity>): List<RocketLaunch> =
        from.map { entityToDomain(it) }

    fun dtoToDomain(from: RocketLaunchDTO): RocketLaunch =
        RocketLaunch(
            flightNumber = from.flightNumber,
            missionName = from.missionName,
            launchYear = from.launchYear,
            launchDateUTC = from.launchDateUTC,
            rocket = Rocket(
                id = from.rocketDTO.id,
                name = from.rocketDTO.name,
                type = from.rocketDTO.type
            ),
            details = from.details,
            launchSuccess = from.launchSuccess,
            links = Links(
                missionPatchUrl = from.linksDTO.missionPatchUrl,
                articleUrl = from.linksDTO.articleUrl
            )
        )

    fun dtoToDomain(from: List<RocketLaunchDTO>): List<RocketLaunch> =
        from.map { dtoToDomain(it) }

    fun dtoToEntity(from: RocketLaunchDTO): RocketLaunchEntity =
        RocketLaunchEntity(
            flightNumber = from.flightNumber,
            missionName = from.missionName,
            launchYear = from.launchYear,
            launchDateUTC = from.launchDateUTC,
            rocketEntity = RocketEntity(
                id = from.rocketDTO.id,
                name = from.rocketDTO.name,
                type = from.rocketDTO.type
            ),
            details = from.details,
            launchSuccess = from.launchSuccess,
            linksEntity = LinksEntity(
                missionPatchUrl = from.linksDTO.missionPatchUrl,
                articleUrl = from.linksDTO.articleUrl
            )
        )

    fun dtoToEntity(from: List<RocketLaunchDTO>): List<RocketLaunchEntity> =
        from.map { dtoToEntity(it) }
}