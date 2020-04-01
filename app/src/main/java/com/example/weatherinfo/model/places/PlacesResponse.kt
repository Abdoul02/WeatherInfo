package com.example.weatherinfo.model.places

data class PlacesResponse(
    val error_message: String? = null,
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)