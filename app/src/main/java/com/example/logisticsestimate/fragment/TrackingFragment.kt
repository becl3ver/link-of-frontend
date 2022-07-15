package com.example.logisticsestimate.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.logisticsestimate.R
import com.example.logisticsestimate.ShipSearchActivity
import com.example.logisticsestimate.databinding.FragmentTrackingBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * @author 최재훈
 * @version
 */
class TrackingFragment : Fragment(), OnMapReadyCallback {
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        when(result.resultCode) {
            Activity.RESULT_OK -> {
                val intent = result.data
                if (intent != null) {
                    val tmpLat = intent.getDoubleExtra("latitude", 0.0)
                    val tmpLong = intent.getDoubleExtra("longitude", 0.0)

                    if(tmpLat == 0.0 && tmpLong == 0.0) {
                        Toast.makeText(context, "위치를 불러오는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        currentLat = tmpLat
                        currentLong = tmpLong
                        setMarker()
                    }
                } else {
                    Toast.makeText(context, "선박 위치를 불러오는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            Activity.RESULT_CANCELED -> {
                Toast.makeText(context, "선박 위치를 불러오는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var _binding : FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private var mMap: GoogleMap? = null

    private var currentLat : Double = 37.0
    private var currentLong : Double = 127.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)

        binding.fragmentTrackingBtnSearch.alpha = 0.9F

        binding.fragmentTrackingBtnSearch.setOnClickListener {
            val intent = Intent(context, ShipSearchActivity::class.java)
            getResult.launch(intent)
        }

          val mapFragment = childFragmentManager.findFragmentById(R.id.mmap) as SupportMapFragment?
          mapFragment?.getMapAsync(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap : GoogleMap) {
        mMap = googleMap

        mMap?.let {
            val currentLocation = LatLng(currentLat, currentLong)
            it.setMaxZoomPreference(10.0f)
            it.setMinZoomPreference(1.0f)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 5.0f))
        }
    }

    private fun setMarker() {
        mMap?.let {
            it.clear()

            val currentLocation = LatLng(currentLat, currentLong)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 3.0f))

            val markerOptions = MarkerOptions()
            markerOptions.position(it.cameraPosition.target)
            markerOptions.title("현재 선박 위치")
            markerOptions.snippet("위도 : $currentLat, 경도 : $currentLong")
            it.addMarker(markerOptions)
        }
    }
}