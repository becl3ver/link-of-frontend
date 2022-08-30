package com.example.logisticsestimate.utils

import kotlin.math.ceil
import kotlin.math.floor

class FreightCBMCalculator(var length: Double, var width: Double, var height: Double, var weight: Double, var num: Int, var unit: Int) {
    private val MM = 0
    private val CM = 1
    private val M = 2

    private val CONTAINER = arrayOf(
        doubleArrayOf(5.898 , 2.352, 2.392, 21780.0),
        doubleArrayOf(12.032, 2.352, 2.392, 26750.0),
        doubleArrayOf(12.032, 2.352, 2.689, 26750.0)
    ) // 화물 20ft, 40ft, 40ft_HC 에 대한 정의 (단위 m)

    init {
        length = toMeter(length)
        width = toMeter(width)
        height = toMeter(height)
    }

    private fun toMeter(num: Double): Double {
        return when(unit) {
            CM -> num / 100.0
            MM -> num / 1000.0
            else -> num
        }
    }

    fun cbmCalculator(): Double {
        return printDoubleFourDot(length * width * height)
    }

    fun containerPerFreightOneFun(): DoubleArray { // 1단 적재
        // 한 컨테이너에 들어갈 수 있는 최대 화물 갯수 구하기
        var numPerWeight = 0.0

        var maxCPF20ft = doubleArrayOf( // 최대 수량 구하기
            floor(CONTAINER[0][0] / length) * floor(CONTAINER[0][1] / width) * checkHeightRight(
                height,
                CONTAINER[0][2]
            ),
            floor(CONTAINER[0][0] / length) * floor(CONTAINER[0][1] / height) * checkHeightRight(
                width,
                CONTAINER[0][2]
            ),
            floor(CONTAINER[0][0] / width) * floor(CONTAINER[0][1] / length) * checkHeightRight(
                height,
                CONTAINER[0][2]
            ),
            floor(CONTAINER[0][0] / width) * floor(CONTAINER[0][1] / height) * checkHeightRight(
                length,
                CONTAINER[0][2]
            ),
            floor(CONTAINER[0][0] / height) * floor(CONTAINER[0][1] / length) * checkHeightRight(
                width,
                CONTAINER[0][2]
            ),
            floor(CONTAINER[0][0] / height) * floor(CONTAINER[0][1] / width) * checkHeightRight(
                length,
                CONTAINER[0][2]
            )
        ).max()

        numPerWeight = num / checkWeightCntnNum(maxCPF20ft * weight, CONTAINER[0][3])
        maxCPF20ft = printDoubleFourDot(numPerWeight)

        var maxCPF40ft = doubleArrayOf(
            floor(CONTAINER[1][0] / length) * floor(CONTAINER[1][1] / width) * checkHeightRight(
                height,
                CONTAINER[1][2]
            ),
            floor(CONTAINER[1][0] / length) * floor(CONTAINER[1][1] / height) * checkHeightRight(
                width,
                CONTAINER[1][2]
            ),
            floor(CONTAINER[1][0] / width) * floor(CONTAINER[1][1] / length) * checkHeightRight(
                height,
                CONTAINER[1][2]
            ),
            floor(CONTAINER[1][0] / width) * floor(CONTAINER[1][1] / height) * checkHeightRight(
                length,
                CONTAINER[1][2]
            ),
            floor(CONTAINER[1][0] / height) * floor(CONTAINER[1][1] / length) * checkHeightRight(
                width,
                CONTAINER[1][2]
            ),
            floor(CONTAINER[1][0] / height) * floor(CONTAINER[1][1] / width) * checkHeightRight(
                length,
                CONTAINER[1][2]
            )
        ).max()

        numPerWeight = num / checkWeightCntnNum(maxCPF40ft * weight, CONTAINER[1][3])
        maxCPF40ft = printDoubleFourDot(numPerWeight)

        var maxCPF40ftHc = doubleArrayOf(
            floor(CONTAINER[2][0] / length) * floor(CONTAINER[2][1] / width) * checkHeightRight(
                height,
                CONTAINER[2][2]
            ),
            floor(CONTAINER[2][0] / length) * floor(CONTAINER[2][1] / height) * checkHeightRight(
                width,
                CONTAINER[2][2]
            ),
            floor(CONTAINER[2][0] / width) * floor(CONTAINER[2][1] / length) * checkHeightRight(
                height,
                CONTAINER[2][2]
            ),
            floor(CONTAINER[2][0] / width) * floor(CONTAINER[2][1] / height) * checkHeightRight(
                length,
                CONTAINER[2][2]
            ),
            floor(CONTAINER[2][0] / height) * floor(CONTAINER[2][1] / length) * checkHeightRight(
                width,
                CONTAINER[2][2]
            ),
            floor(CONTAINER[2][0] / height) * floor(CONTAINER[2][1] / width) * checkHeightRight(
                length,
                CONTAINER[2][2]
            )
        ).max()

        numPerWeight = num / checkWeightCntnNum(maxCPF40ftHc * weight, CONTAINER[2][3])
        maxCPF40ftHc = printDoubleFourDot(numPerWeight)

        return doubleArrayOf(maxCPF20ft, maxCPF40ft, maxCPF40ftHc)
    }

    fun containerPerFreightTwoFun(): DoubleArray { // 2단 적재
        // 한 컨테이너에 들어갈 수 있는 최대 화물 갯수 구하기
        var numPerWeight = 0.0

        var maxCPF20ft = doubleArrayOf(
            floor(CONTAINER[0][0] / length) * floor(CONTAINER[0][1] / width) * floor(CONTAINER[0][2] / height),
            floor(CONTAINER[0][0] / length) * floor(CONTAINER[0][1] / height) * floor(CONTAINER[0][2] / width),
            floor(CONTAINER[0][0] / width) * floor(CONTAINER[0][1] / length) * floor(CONTAINER[0][2] / height),
            floor(CONTAINER[0][0] / width) * floor(CONTAINER[0][1] / height) * floor(CONTAINER[0][2] / length),
            floor(CONTAINER[0][0] / height) * floor(CONTAINER[0][1] / length) * floor(CONTAINER[0][2] / width),
            floor(CONTAINER[0][0] / height) * floor(CONTAINER[0][1] / width) * floor(CONTAINER[0][2] / length)
        ).max()

        numPerWeight = num / checkWeightCntnNum(maxCPF20ft * weight, CONTAINER[0][3])
        maxCPF20ft = printDoubleFourDot(numPerWeight)

        var maxCPF40ft = doubleArrayOf(
            floor(CONTAINER[1][0] / length) * floor(CONTAINER[1][1] / width) * floor(CONTAINER[1][2] / height),
            floor(CONTAINER[1][0] / length) * floor(CONTAINER[1][1] / height) * floor(CONTAINER[1][2] / width),
            floor(CONTAINER[1][0] / width) * floor(CONTAINER[1][1] / length) * floor(CONTAINER[1][2] / height),
            floor(CONTAINER[1][0] / width) * floor(CONTAINER[1][1] / height) * floor(CONTAINER[1][2] / length),
            floor(CONTAINER[1][0] / height) * floor(CONTAINER[1][1] / length) * floor(CONTAINER[1][2] / width),
            floor(CONTAINER[1][0] / height) * floor(CONTAINER[1][1] / width) * floor(CONTAINER[1][2] / length)
        ).max()

        numPerWeight = num / checkWeightCntnNum(maxCPF40ft * weight, CONTAINER[1][3])
        maxCPF40ft = printDoubleFourDot(numPerWeight)

        var maxCPF40ftHc = doubleArrayOf(
            floor(CONTAINER[2][0] / length) * floor(CONTAINER[2][1] / width) * floor(CONTAINER[2][2] / height),
            floor(CONTAINER[2][0] / length) * floor(CONTAINER[2][1] / height) * floor(CONTAINER[2][2] / width),
            floor(CONTAINER[2][0] / width) * floor(CONTAINER[2][1] / length) * floor(CONTAINER[2][2] / height),
            floor(CONTAINER[2][0] / width) * floor(CONTAINER[2][1] / height) * floor(CONTAINER[2][2] / length),
            floor(CONTAINER[2][0] / height) * floor(CONTAINER[2][1] / length) * floor(CONTAINER[2][2] / width),
            floor(CONTAINER[2][0] / height) * floor(CONTAINER[2][1] / width) * floor(CONTAINER[2][2] / length)
        ).max()

        numPerWeight = num / checkWeightCntnNum(maxCPF40ftHc * weight, CONTAINER[2][3])
        maxCPF40ftHc = printDoubleFourDot(numPerWeight)

        return doubleArrayOf(maxCPF20ft, maxCPF40ft, maxCPF40ftHc)
    }

    fun printDoubleFourDot(num : Double) : Double {
        val result = ceil(num * 1000) / 1000.0 // 너무 작은 값도 수용하기 위해 round(반올림) 대신 ceil(올림) 사용.
        if (num == Double.POSITIVE_INFINITY || num == Double.NEGATIVE_INFINITY)
            return -1.0
        return result
    }

    fun checkHeightRight(high: Double, cntnHeight: Double): Double {
        return if (high > cntnHeight) -1.0 else 1.0
    }

    fun checkWeightCntnNum(maxVolumeNumW : Double, maxWeight : Double) : Double{
        // maxVolumeNumW --> (maxCPF20ft, 40ft, 40ftHc) * Weight == 부피를 중심으로 화물이 들어갈 수 있는 최대 개수(1단, 2단(다단) 둘 다)의 중량
        // maxWeight --> Container[0, 1, 2]      == 최대 중량
        // checkWeightCntnNum 함수의 목적
        // 중량을 중심으로 화물이 들어갈 수 있는 최대 개수는 (maxCPF20ft, 40ft, 40ftHc)보다 작거나 같다. (부피 우선으로 생각해야 함.)
        if (maxVolumeNumW > maxWeight) {
            var whatWeight = 0.0
            var i = 0.0
            while (maxWeight > whatWeight) {
                whatWeight += weight
                i++
            }
            return i
        } else { // maxVolumeNumW <= maxWeight
            return maxVolumeNumW / weight
        }
    }

    fun checkAllRightInput() : Boolean {
        return ( (length > 0.0) && (width > 0.0) && (height > 0.0) && (num > 0.0) && (weight > 0.00))
    }
}