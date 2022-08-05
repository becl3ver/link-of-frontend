package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName


data class TrafficResponseDto (
    @SerializedName("MMSI"      ) var MMSI      : String? = null,
    @SerializedName("LAT"       ) var LAT       : String? = null,
    @SerializedName("LON"       ) var LON       : String? = null,
    @SerializedName("SPEED"     ) var SPEED     : String? = null,
    @SerializedName("HEADING"   ) var HEADING   : String? = null,
    @SerializedName("COURSE"    ) var COURSE    : String? = null,
    @SerializedName("STATUS"    ) var STATUS    : String? = null,
    @SerializedName("TIMESTAMP" ) var TIMESTAMP : String? = null,
    @SerializedName("DSRC"      ) var DSRC      : String? = null

)