package com.tootiyesolutions.acturdc.model

import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("rendered")
    val articleContent: String?
)