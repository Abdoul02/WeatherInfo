package com.example.weatherinfo.model.places

data class PlacesModel(
    val placesResponse: PlacesResponse? = null,
    val error: Throwable? = null
)