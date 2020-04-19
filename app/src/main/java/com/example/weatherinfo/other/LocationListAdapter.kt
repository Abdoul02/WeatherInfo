package com.example.weatherinfo.other

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherinfo.R
import com.example.weatherinfo.model.UserLocation

class LocationListAdapter(private val context: Context) :
    RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {
    private var data: ArrayList<UserLocation> = ArrayList()
    lateinit var listener: OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.location_entry, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return if (!data.isNullOrEmpty()) data.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = data[position]
        holder.tvLocation.text = location.name
        holder.tvLatLong.text = context.getString(
            R.string.lat_long,
            location.latitude.toString(),
            location.longitude.toString()
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLocation: TextView = itemView.findViewById(R.id.tvLocationName)
        var tvLatLong: TextView = itemView.findViewById(R.id.tvLatLong)
        var imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        private val cardView: CardView = itemView.findViewById(R.id.cvLocation)

        init {
            cardView.setOnClickListener {
                listener.onItemClick(
                    data[adapterPosition], it
                )
            }

            imgDelete.setOnClickListener {
                listener.onDeleteIconClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(userLocation: UserLocation, view: View)
        fun onDeleteIconClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setData(locationData: List<UserLocation>) {
        if(data.isNotEmpty()) data.clear()
        this.data.addAll(locationData)
        notifyDataSetChanged()
    }

    fun removeLocation(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }

    fun getLocation(position: Int): UserLocation {
        return data[position]
    }

    fun restoreLocation(userLocation: UserLocation, position: Int) {
        data.add(position, userLocation)
        notifyItemInserted(position)
    }
}