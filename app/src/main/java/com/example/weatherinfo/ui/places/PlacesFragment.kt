package com.example.weatherinfo.ui.places

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherinfo.MyApplication

import com.example.weatherinfo.R
import com.example.weatherinfo.other.ReusableData
import javax.inject.Inject

class PlacesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PlacesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.places_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val locationArgs = PlacesFragmentArgs.fromBundle(arguments!!)
        val location = "${locationArgs.latitude},${locationArgs.longitude}"
        activity?.let {
            (it.application as MyApplication).get(it).getApplicationComponent()
                ?.injectPlaceFragment(this)
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlacesViewModel::class.java)
        val placesData = viewModel.getPlaceInfo(
            ReusableData.PLACE_URL,
            location,
            ReusableData.RADIUS,
            ReusableData.PLACE_TYPE,
            ReusableData.PLACES_API_KEY
        )
        placesData.observe(viewLifecycleOwner, Observer {
            Log.d("Places", "$it")
        })

    }

}
