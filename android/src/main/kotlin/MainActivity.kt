package com.racepartyapp.android

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.racepartyapp.shared.HeartRate
import com.racepartyapp.shared.Location
import com.google.android.gms.common.GoogleApiAvailability


private const val REQUEST_GOOGLE_PLAY_SERVICES = 1
private const val REQUEST_LOCATION_PERMISSIONS = 2

class MainActivity : AppCompatActivity() {
    private lateinit var activityManager: ActivityManager
    private lateinit var locationTrackingService: LocationTrackingService
    private val uiHandler = Handler()

    // UI controls
    private lateinit var mainText: TextView
    private lateinit var mainButton: Button
    private lateinit var physicalActivityText: TextView

    private val locationTrackingCallback = object : LocationTrackingService.LocationTrackingCallback {
        override fun onLocationUpdate(location: Location) {
            uiHandler.post {
                mainText.text = location.toString()
                physicalActivityText.text = "${locationTrackingService.distance} m"
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            locationTrackingService = (binder as LocationTrackingService.LocalBinder).getService()
            locationTrackingService.locationTrackingCallback = locationTrackingCallback
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            locationTrackingService.locationTrackingCallback = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager

        setupContentViewAndUIControlReferences()

        mainButton.setOnClickListener {
            if (isLocationTrackingServiceRunning()) {
                stopLocationUpdates()
            } else {
                startLocationUpdates()
            }
        }

        mainText.text = HeartRate(155).toString()
    }

    private fun setupContentViewAndUIControlReferences() {
        // Content View must be set first in order to be able to retrieve the references to the individual UI controls
        setContentView(R.layout.activity_main)

        mainText = findViewById(R.id.main_text)
        mainButton = findViewById(R.id.button)
        physicalActivityText = findViewById(R.id.physicalActivityText)
    }

    override fun onResume() {
        super.onResume()
        if (isLocationTrackingServiceRunning()) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isLocationTrackingServiceRunning()) {
            unbindService(connection)
        }
    }

    @Suppress("DEPRECATION")
    private fun isLocationTrackingServiceRunning() =
        activityManager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == LocationTrackingService::class.java.name }

    private fun startLocationUpdates() {
        checkGooglePlayServicesAvailability()
        checkLocationPermissions()

        Intent(this, LocationTrackingService::class.java).also { intent ->
            ContextCompat.startForegroundService(this, intent)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        mainButton.text = "Stop"
    }

    private fun checkGooglePlayServicesAvailability() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val googlePlayServicesAvailable = googleApiAvailability.isGooglePlayServicesAvailable(applicationContext)
        Log.d("DEV", "Google Play: ${googleApiAvailability.getErrorString(googlePlayServicesAvailable)}")
        if (googleApiAvailability.isUserResolvableError(googlePlayServicesAvailable)) {
            googleApiAvailability.showErrorDialogFragment(
                this,
                googlePlayServicesAvailable,
                REQUEST_GOOGLE_PLAY_SERVICES
            )
        }
    }

    private fun checkLocationPermissions() {
        Log.d("DEV", "Android: ${Build.VERSION.SDK_INT}")

        // Starting with Android M, Location permissions are now obtained at runtime.
        // Prior Android versions got them at installation time.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val fine = Manifest.permission.ACCESS_FINE_LOCATION

            while (ContextCompat.checkSelfPermission(this, fine) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(fine), REQUEST_LOCATION_PERMISSIONS)
            }
        }
    }

    private fun stopLocationUpdates() {
        locationTrackingService.stop()
        unbindService(connection)
        stopService(Intent(this, LocationTrackingService::class.java))
        mainButton.text = "Start"
    }
}
