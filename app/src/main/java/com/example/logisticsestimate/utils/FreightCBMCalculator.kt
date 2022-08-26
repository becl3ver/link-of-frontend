package com.example.logisticsestimate.utils

import kotlin.math.floor

class FreightCBMCalculator(
    var length: Double, var width: Double, var height: Double, var weight: Double, var num: Int, unit: Int
) {
    private val MM = 0
    private val CM = 1
    private val M = 2

    private val CONTAINER = arrayOf(
        doubleArrayOf(5.898 , 2.352, 2.392, 21780.0),
        doubleArrayOf(12.032, 2.352, 2.392, 26750.0),
        doubleArrayOf(12.032, 2.352, 2.689, 26750.0)
    )

    private fun toMeter(unit: Int, num: Double): Double {
        return when(unit) {
            CM -> num / 100.0
            MM -> num / 1000.0
            else -> length
        }
    }

    init {
        length = toMeter(unit, length)
        width = toMeter(unit, width)
        height = toMeter(unit, height)
    }

    fun cbmCalculator(): Double {
        return length * width * height
    }

    fun containerPerFreightOneFun(): DoubleArray { // 1단 적재
        // 한 컨테이너에 들어갈 수 있는 최대 화물 갯수 구하기
        var numPerWeight = 0.0
        var numPerVolume = 0.0

        var maxCPF20ft = doubleArrayOf( // 최대 수량 구하기
            floor(CONTAINER[0][0] / length) * floor(CONTAINER[0][1] / width),
            floor(CONTAINER[0][0] / length) * floor(CONTAINER[0][1] / height),
            floor(CONTAINER[0][0] / width) * floor(CONTAINER[0][1] / length),
            floor(CONTAINER[0][0] / width) * floor(CONTAINER[0][1] / height),
            floor(CONTAINER[0][0] / height) * floor(CONTAINER[0][1] / length),
            floor(CONTAINER[0][0] / height) * floor(CONTAINER[0][1] / width)
        ).max()

        numPerWeight = num / floor(CONTAINER[0][3] / weight)
        numPerVolume = num / maxCPF20ft
        maxCPF20ft = if (numPerVolume > numPerWeight) numPerVolume else numPerWeight

        var maxCPF40ft = doubleArrayOf(
            floor(CONTAINER[1][0] / length) * floor(CONTAINER[1][1] / width),
            floor(CONTAINER[1][0] / length) * floor(CONTAINER[1][1] / height),
            floor(CONTAINER[1][0] / width) * floor(CONTAINER[1][1] / length),
            floor(CONTAINER[1][0] / width) * floor(CONTAINER[1][1] / height),
            floor(CONTAINER[1][0] / height) * floor(CONTAINER[1][1] / length),
            floor(CONTAINER[1][0] / height) * floor(CONTAINER[1][1] / width)
        ).max()

        numPerWeight = num / floor(CONTAINER[1][3] / weight)
        numPerVolume = num / maxCPF40ft
        maxCPF40ft = if (numPerVolume > numPerWeight) numPerVolume else numPerWeight

        var maxCPF40ftHc = doubleArrayOf(
            floor(CONTAINER[2][0] / length) * floor(CONTAINER[2][1] / width),
            floor(CONTAINER[2][0] / length) * floor(CONTAINER[2][1] / height),
            floor(CONTAINER[2][0] / width) * floor(CONTAINER[2][1] / length),
            floor(CONTAINER[2][0] / width) * floor(CONTAINER[2][1] / height),
            floor(CONTAINER[2][0] / height) * floor(CONTAINER[2][1] / length),
            floor(CONTAINER[2][0] / height) * floor(CONTAINER[2][1] / width)
        ).max()

        numPerWeight = num / floor(CONTAINER[2][3] / weight)
        numPerVolume = num / maxCPF40ftHc
        maxCPF40ftHc = if (numPerVolume > numPerWeight) numPerVolume else numPerWeight

        return doubleArrayOf(maxCPF20ft, maxCPF40ft, maxCPF40ftHc)
    }

    fun containerPerFreightTwoFun(): DoubleArray { // 2단 적재
        // 한 컨테이너에 들어갈 수 있는 최대 화물 갯수 구하기
        var numPerWeight = 0.0
        var numPerVolume = 0.0

        var maxCPF20ft = doubleArrayOf(
            floor(CONTAINER[0][0] / length) * floor(CONTAINER[0][1] / width) * floor(CONTAINER[0][2] / height),
            floor(CONTAINER[0][0] / length) * floor(CONTAINER[0][1] / height) * floor(CONTAINER[0][2] / width),
            floor(CONTAINER[0][0] / width) * floor(CONTAINER[0][1] / length) * floor(CONTAINER[0][2] / height),
            floor(CONTAINER[0][0] / width) * floor(CONTAINER[0][1] / height) * floor(CONTAINER[0][2] / length),
            floor(CONTAINER[0][0] / height) * floor(CONTAINER[0][1] / length) * floor(CONTAINER[0][2] / width),
            floor(CONTAINER[0][0] / height) * floor(CONTAINER[0][1] / width) * floor(CONTAINER[0][2] / length)
        ).max()

        numPerWeight = num / floor(CONTAINER[0][3] / weight)
        numPerVolume = num / maxCPF20ft
        maxCPF20ft = if (numPerVolume > numPerWeight) numPerVolume else numPerWeight

        var maxCPF40ft = doubleArrayOf(
            floor(CONTAINER[1][0] / length) * floor(CONTAINER[1][1] / width) * floor(CONTAINER[1][2] / height),
            floor(CONTAINER[1][0] / length) * floor(CONTAINER[1][1] / height) * floor(CONTAINER[1][2] / width),
            floor(CONTAINER[1][0] / width) * floor(CONTAINER[1][1] / length) * floor(CONTAINER[1][2] / height),
            floor(CONTAINER[1][0] / width) * floor(CONTAINER[1][1] / height) * floor(CONTAINER[1][2] / length),
            floor(CONTAINER[1][0] / height) * floor(CONTAINER[1][1] / length) * floor(CONTAINER[1][2] / width),
            floor(CONTAINER[1][0] / height) * floor(CONTAINER[1][1] / width) * floor(CONTAINER[1][2] / length)
        ).max()

        numPerWeight = num / floor(CONTAINER[1][3] / weight)
        numPerVolume = num / maxCPF40ft
        maxCPF40ft = if (numPerVolume > numPerWeight) numPerVolume else numPerWeight

        var maxCPF40ftHc = doubleArrayOf(
            floor(CONTAINER[2][0] / length) * floor(CONTAINER[2][1] / width) * floor(CONTAINER[2][2] / height),
            floor(CONTAINER[2][0] / length) * floor(CONTAINER[2][1] / height) * floor(CONTAINER[2][2] / width),
            floor(CONTAINER[2][0] / width) * floor(CONTAINER[2][1] / length) * floor(CONTAINER[2][2] / height),
            floor(CONTAINER[2][0] / width) * floor(CONTAINER[2][1] / height) * floor(CONTAINER[2][2] / length),
            floor(CONTAINER[2][0] / height) * floor(CONTAINER[2][1] / length) * floor(CONTAINER[2][2] / width),
            floor(CONTAINER[2][0] / height) * floor(CONTAINER[2][1] / width) * floor(CONTAINER[2][2] / length)
        ).max()

        numPerWeight = num / floor(CONTAINER[2][3] / weight)
        numPerVolume = num / maxCPF40ftHc
        maxCPF40ftHc = if (numPerVolume > numPerWeight) numPerVolume else numPerWeight

        return doubleArrayOf(maxCPF20ft, maxCPF40ft, maxCPF40ftHc)
    }
}