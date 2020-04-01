package com.example.weatherinfo.ui.places

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherinfo.MyApplication

import com.example.weatherinfo.R
import com.example.weatherinfo.model.places.PlacesModel
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
    lateinit var clMain: ConstraintLayout
    lateinit var clInfo: ConstraintLayout

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
        clMain = root.findViewById(R.id.mainCLayout)
        clInfo = root.findViewById(R.id.clPlaceInfo)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!ReusableData.isOnline(this.context!!)) {
            ReusableData.showAlertDialog(
                this.context!!,
                "Network Error",
                "Please connect to the internet and try again",
                dialogOnClickListener
            )
        } else {
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


    }

    private fun displayInfo(placesModel: PlacesModel) {

        if (placesModel.placesResponse != null) {

            if (placesModel.placesResponse.status == "OK") {
                clMain.visibility = View.VISIBLE
                clInfo.visibility = View.GONE
                val result = placesModel.placesResponse.results.first()
                tvName.text = this.getString(R.string.place_name, result.name)
                tvOpen.text = if (result.opening_hours != null) {
                    if (result.opening_hours.open_now) this.getString(
                        R.string.place_open,
                        "YES"
                    ) else this.getString(R.string.place_open, "NO")
                } else {
                    this.getString(R.string.place_open, "N/A")
                }
                tvRate.text = this.getString(R.string.place_rating, result.rating.toString())
                tvType.text = this.getString(R.string.place_type, getTypeList(result.types))
            } else {
                ReusableData.showAlertDialog(
                    this.context!!,
                    "Location Error",
                    placesModel.placesResponse.error_message,
                    dialogOnClickListener
                )
            }
        } else {
            ReusableData.showAlertDialog(
                this.context!!,
                "Location Error",
                placesModel.error?.message,
                dialogOnClickListener
            )
        }
    }

    private fun getTypeList(types: List<String>): String {
        return types.joinToString(separator = ",")
    }

    private val dialogOnClickListener =
        DialogInterface.OnClickListener() { _: DialogInterface, _: Int ->
            findNavController().navigate(R.id.action_placesFragment_to_nav_favorite)
        }
}
