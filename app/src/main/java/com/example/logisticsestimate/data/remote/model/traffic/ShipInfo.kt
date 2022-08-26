package com.example.logisticsestimate.data.remote.model.traffic

import java.io.Serializable

/**
 * 조회된 선박 정보 파싱
 */
class ShipInfo(trafficResponseDto: TrafficResponseDto): Serializable {
    val mmsi = trafficResponseDto[0][0]
    val lat = trafficResponseDto[0][1].toDouble()
    val lon = trafficResponseDto[0][2].toDouble()
    val speed = trafficResponseDto[0][3].toDouble() / 10.0
    val heading = trafficResponseDto[0][4].toDouble()
    val course = trafficResponseDto[0][5].toDouble()
    val status = trafficResponseDto[0][6].toDouble()
    val timestamp = trafficResponseDto[0][7]
    val dsrc = trafficResponseDto[0][8]
}