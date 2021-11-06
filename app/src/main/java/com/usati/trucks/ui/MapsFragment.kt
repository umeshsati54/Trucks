package com.usati.trucks.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.usati.trucks.R
import com.usati.trucks.utils.Resource
import com.usati.trucks.utils.Utils.Companion.getSeconds
import com.usati.trucks.viewmodel.TrucksViewModel

class MapsFragment : Fragment() {
    lateinit var viewModel: TrucksViewModel

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        //val sydney = LatLng(-34.0, 151.0)
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        var icon: BitmapDescriptor? = null
        viewModel.trucks.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data.let { trucksResponse ->
                        trucksResponse?.data?.forEach { truck ->
                            if (truck.lastRunningState.truckRunningState == 1) {
                                icon = BitmapDescriptorFactory.fromBitmap(
                                    AppCompatResources.getDrawable(
                                        requireContext(),
                                        R.drawable.truck_green
                                    )
                                        ?.toBitmap()!!
                                )
                            } else if (truck.lastRunningState.truckRunningState == 0 && !truck.lastWaypoint.ignitionOn) {
                                icon = BitmapDescriptorFactory.fromBitmap(
                                    AppCompatResources.getDrawable(
                                        requireContext(),
                                        R.drawable.truck_blue
                                    )
                                        ?.toBitmap()!!
                                )
                            } else if (truck.lastRunningState.truckRunningState == 0 && truck.lastWaypoint.ignitionOn) {
                                icon = BitmapDescriptorFactory.fromBitmap(
                                    AppCompatResources.getDrawable(
                                        requireContext(),
                                        R.drawable.truck_yellow
                                    )
                                        ?.toBitmap()!!
                                )
                            } else if (getSeconds(truck.lastWaypoint.updateTime) > 4 * 60 * 60) {
                                icon = BitmapDescriptorFactory.fromBitmap(
                                    AppCompatResources.getDrawable(
                                        requireContext(),
                                        R.drawable.truck_red
                                    )
                                        ?.toBitmap()!!
                                )
                            }

                            googleMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        truck.lastWaypoint.lat,
                                        truck.lastWaypoint.lng
                                    )
                                ).title(truck.truckNumber)
                            )?.setIcon(icon)

                        }
                    }
                }
                is Resource.Error -> {
                    response.message.let { message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {

                }
            }

        })
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(20.5937, 78.9629)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_map, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list_fragment -> {
                findNavController().navigate(R.id.action_mapsFragment_to_listFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}