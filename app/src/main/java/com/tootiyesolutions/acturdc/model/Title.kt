package com.tootiyesolutions.acturdc.model

import com.google.gson.annotations.SerializedName

data class Title(
    @SerializedName("rendered")
    val valueTitle: String?
)