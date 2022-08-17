package com.example.logisticsestimate.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.logisticsestimate.R
import com.example.logisticsestimate.ShipSearchActivity
import com.example.logisticsestimate.data.traffic.ShipInfo
import com.example.logisticsestimate.databinding.FragmentTrackingBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * ShipSearchActivity 통해서 선박 위치를 조회하고, 구글 맵 API를 이용해서 해당 위치를 표시한다.
 */
class TrackingFragment : Fragment(), OnMapReadyCallback {
    private var _binding : FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private var latitude = 37.0
    private var longitude = 127.0
    private var speed = 0.0
    private var heading = 0.0
    private var course = 0.0

    private var shipInfo : ShipInfo? = null
    private var mMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)

        binding.fragmentTrackingBtnSearch.alpha = 0.9F

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
            when(result.resultCode) {
                Activity.RESULT_OK -> {
                    val intent = result.data
                    if (intent != null) {
                        shipInfo = intent.getSerializableExtra("shipInfo") as ShipInfo

                        latitude = shipInfo!!.lat
                        longitude = shipInfo!!.lon
                        speed = shipInfo!!.speed
                        course = shipInfo!!.course
                        heading = shipInfo!!.heading

                        setMarker()
                    } else {
                        Toast.makeText(context, "선박 위치를 불러오는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                Activity.RESULT_CANCELED -> {
                    Toast.makeText(context, "검색된 선박이 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

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

        if(shipInfo != null) {
            setMarker()
        } else {
            val currentLocation = LatLng(latitude, longitude)
            mMap?.setMaxZoomPreference(10.0f)
            mMap?.setMinZoomPreference(1.0f)
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 5.0f))
        }
    }

    private fun setMarker() {
        mMap?.let {
            it.clear()

            it.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                override fun getInfoWindow(p0: Marker): View? {
                    return null
                }

                override fun getInfoContents(p0: Marker): View? {
                    val container = LinearLayout(context)
                    container.orientation =  LinearLayout.VERTICAL

                    val title = TextView(context)
                    title.text = "현재 선박 위치"
                    title.typeface = Typeface.DEFAULT_BOLD
                    title.setTextColor(Color.BLACK)

                    val content1 = TextView(context)
                    val text1 = "위도 : $latitude°, 경도 : $longitude°"
                    content1.text = text1
                    content1.setTextColor(Color.BLACK)

                    val content2 = TextView(context)
                    val text2 = "속도 : $speed knots, 방향 : $heading, 경로 : $course "
                    content2.text = text2
                    content2.setTextColor(Color.BLACK)

                    container.addView(title)
                    container.addView(content1)
                    container.addView(content2)

                    return container
                }
            })

            val currentLocation = LatLng(shipInfo!!.lat, shipInfo!!.lon)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 5.0f))

            val markerOptions = MarkerOptions()
            markerOptions.position(it.cameraPosition.target)
            it.addMarker(markerOptions)
        }
    }
}