package com.example.weatherinfo.repository

import androidx.lifecycle.MutableLiveData
import com.example.weatherinfo.data.NetworkData
import com.example.weatherinfo.model.places.PlacesModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlacesRepository @Inject constructor(private val networkData: NetworkData) {

    private val placesDisposable: CompositeDisposable? = CompositeDisposable()
    private val placeModelMutable = MutableLiveData<PlacesModel>()

    fun getLocationInfo(
        url: String,
        location: String,
        radius: Int,
        type: String,
        key: String
    ): MutableLiveData<PlacesModel> {
        placesDisposable?.add(
            networkData.getLocationInformation(url, location, type, radius, key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val placeModel = PlacesModel(placesResponse = it)
                    placeModelMutable.postValue(placeModel)
                }, {
                    val placeModel = PlacesModel(error = it)
                    placeModelMutable.postValue(placeModel)
                })
        )
        return placeModelMutable
    }
}