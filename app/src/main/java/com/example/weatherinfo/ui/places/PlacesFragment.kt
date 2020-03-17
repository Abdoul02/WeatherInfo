package com.example.weatherinfo.ui.places

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherinfo.MyApplication

import com.example.weatherinfo.R
import com.example.weatherinfo.model.places.PlacesResponse
import com.example.weatherinfo.other.ReusableData
import com.squareup.picasso.Picasso
import javax.inject.Inject

class PlacesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PlacesViewModel
    lateinit var imgPhoto: ImageView
    lateinit var tvName: TextView
    lateinit var tvRate: TextView
    lateinit var tvType: TextView
    lateinit var tvOpen: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.places_fragment, container, false)
        imgPhoto = root.findViewById(R.id.picture)
        tvName = root.findViewById(R.id.tvPlaceName)
        tvRate = root.findViewById(R.id.tvPlaceRate)
        tvType = root.findViewById(R.id.tvPlaceType)
        tvOpen = root.findViewById(R.id.tvPlaceOpen)
        return root
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
            displayInfo(it)
        })

    }

    private fun displayInfo(placesResponse: PlacesResponse) {


        if (placesResponse.results.isNotEmpty()) {
            val result = placesResponse.results[0]
            val picasso = Picasso.get()
            //picasso.load(result).into(holder.imgBeer)
            tvName.text = this.getString(R.string.place_name, result.name)
            tvOpen.text = if (result.opening_hours.open_now) this.getString(
                R.string.place_open,
                "YES"
            ) else this.getString(R.string.place_open, "NO")
            tvRate.text = result.rating.toString()
            tvType.text = result.types.toString()
        } else {

        }
    }

}
