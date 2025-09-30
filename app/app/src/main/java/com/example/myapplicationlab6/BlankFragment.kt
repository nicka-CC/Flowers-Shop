package com.example.myapplicationlab6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.map.CameraPosition


class BlankFragment : Fragment() {

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.mapview)  // MapView из layout
        return view
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()

        mapView.map.move(
            CameraPosition(Point(55.751244, 37.618423), 11.0f, 0.0f, 0.0f)
        )

        val mapObjects = mapView.map.mapObjects
        mapObjects.addPlacemark(Point(55.751244, 37.618423))
        mapObjects.addPlacemark(Point(59.9342802, 30.3350986))
        mapObjects.addPlacemark(Point(56.8389261, 60.6057025))
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}