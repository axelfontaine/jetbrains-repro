package com.racepartyapp.android

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.racepartyapp.shared.Date
import com.racepartyapp.shared.Location
import com.google.android.gms.location.*
import kotlin.math.round
import kotlin.math.roundToInt


private const val NOTIFICATION_CHANNEL_ID = "LocationTrackingChannel"
private const val NOTIFICATION_ID = 1

class LocationTrackingService : Service() {
    // Android infrastructure
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var locationCallback: LocationCallback

    private val locations = mutableListOf<Location>()
    var locationTrackingCallback: LocationTrackingCallback? = null
    private var _distance = 0.0
    val distance: Int; get() = round(_distance).roundToInt()

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = NotificationManagerCompat.from(this)
        notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.locations?.forEach {
                    val loc = Location(it.latitude, it.longitude, it.accuracy, Date(it.time))
                    if (locations.isNotEmpty()) {
                        _distance += locations.last().distanceTo(loc)
                    }
                    locations.add(loc)
                    updateNotification()
                    locationTrackingCallback?.onLocationUpdate(loc)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.create().apply {
                interval = 1000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            },
            locationCallback,
            Looper.getMainLooper()
        )

        return START_REDELIVER_INTENT
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Location Tracking Channel",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
    }

    private fun createNotification(): Notification {
        return notificationBuilder
            .setContentTitle("Location Tracking Active")
            .setContentText("0 m")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    0
                )
            )
            .build()
    }

    private fun updateNotification() {
        notificationBuilder.setContentText("$distance m (+- ${locations.last().accuracy} m)")
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun stop() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        fun getService(): LocationTrackingService = this@LocationTrackingService
    }

    interface LocationTrackingCallback {
        fun onLocationUpdate(location: Location)
    }
}
