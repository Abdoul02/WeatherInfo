package com.example.weatherinfo.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherinfo.MyApplication
import com.example.weatherinfo.R
import com.example.weatherinfo.model.UserLocation
import com.example.weatherinfo.other.LocationListAdapter
import com.example.weatherinfo.ui.MapsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var locationRecycleView: RecyclerView
    private lateinit var locationListAdapter: LocationListAdapter
    private lateinit var mapFab: FloatingActionButton
    private lateinit var tvNoLocation: TextView
    //lateinit var favoriteFragmentComponent: FavoriteFragmentComponent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.let {
            (it.application as MyApplication).get(it).getApplicationComponent()
                ?.injectFavoriteFragment(this)
        }
        favoriteViewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)

        locationRecycleView = root.findViewById(R.id.locationRecycleView)
        mapFab = root.findViewById(R.id.mapFab)
        tvNoLocation = root.findViewById(R.id.tvNoLocations)
        this.context?.let { mContext ->
            locationListAdapter = LocationListAdapter(mContext)
            locationRecycleView.layoutManager = LinearLayoutManager(mContext)
            locationRecycleView.setHasFixedSize(true)
            locationRecycleView.adapter = locationListAdapter
        }
        favoriteViewModel.locations.observe(viewLifecycleOwner, Observer { it ->
            renderView(it)
        })

        return root
    }

    private fun renderView(locations: List<UserLocation>) {
        locationListAdapter.setData(locations)
        if (locations.isNotEmpty()) {
            tvNoLocation.visibility = View.GONE
            locationRecycleView.visibility = View.VISIBLE
            mapFab.visibility = View.VISIBLE
        }

        locationListAdapter.setOnItemClickListener(object :
            LocationListAdapter.OnItemClickListener {
            override fun onItemClick(userLocation: UserLocation, view: View) {
                view.findNavController().navigate(
                    FavoriteFragmentDirections.avoriteToPlaces(
                        userLocation.latitude.toString(),
                        userLocation.longitude.toString()
                    )
                )
            }
        })
        mapFab.setOnClickListener {
            activity?.let {
                val intent = Intent(it, MapsActivity::class.java)
                val locationBundle = Bundle()
                val listOfLocation = ArrayList(locations)
                locationBundle.putParcelableArrayList("locationList", listOfLocation)
                intent.putExtras(locationBundle)
                it.startActivity(intent)
            }

            //it.findNavController().navigate(FavoriteFragmentDirections.favoriteToMapFragment())
        }
    }
}
