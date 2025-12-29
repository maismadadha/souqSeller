package com.example.souqseller.activities.activities




import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.souqseller.R
import com.example.souqseller.databinding.ActivityAddNewAddressBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class AddNewAddressActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAddNewAddressBinding
    private lateinit var mMap: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var currentCameraPosition: LatLng? = null
    private var isEditMode = false


    private val addAddressFormLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // رجّع النتيجة للـ AddressesActivity
                setResult(RESULT_OK)
                finish()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.getLocationButton.setOnClickListener {
            getCameraCenterLocation()
        }


        binding.back.setOnClickListener {
            finish()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkLocationPermissionAndGetLocation()

        mMap.setOnCameraMoveListener {
            binding.centerMarker.alpha = 0.7f
        }

        mMap.setOnCameraIdleListener {
            binding.centerMarker.alpha = 1.0f
            currentCameraPosition = mMap.cameraPosition.target
            updateLocationText(currentCameraPosition)
        }
        binding.locationCard.postDelayed({
            binding.locationCard.visibility = View.VISIBLE
        }, 3000)

    }

    private fun checkLocationPermissionAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                currentCameraPosition = currentLatLng
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateLocationText(location: LatLng?) {
        location?.let {
            val geocoder = Geocoder(this, Locale("ar"))
            try {
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    binding.locationText.text = address
                    if (!isEditMode) {
//                        RegisterInfoActivity.AREATEXT = address
                    }
                } else {
                    binding.locationText.text = "No address found"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.locationText.text = "Unable to get address"
            }
        }
    }

    private fun getCameraCenterLocation() {
        currentCameraPosition?.let { position ->
            val lat = position.latitude.toString()
            val lng = position.longitude.toString()
            val addressText = binding.locationText.text.toString()
            val intent = Intent(this, AddNewAddress2Activity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lng", lng)
            addAddressFormLauncher.launch(intent)
        } ?: Toast.makeText(this, "Unable to get camera position", Toast.LENGTH_SHORT).show()
    }
}
