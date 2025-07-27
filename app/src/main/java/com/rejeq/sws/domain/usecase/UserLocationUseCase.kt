package com.rejeq.sws.domain.usecase

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rejeq.sws.domain.entity.LocationPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

interface UserLocationUseCase {
    suspend fun getCurrentLocation(): Result<LocationPoint, LocationErrorKind>
}

sealed interface LocationErrorKind {
    object PermissionDenied : LocationErrorKind
    object LocationUnavailable : LocationErrorKind
    data class Unknown(val exception: Throwable) : LocationErrorKind
}

class DefaultUserLocationUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) : UserLocationUseCase {
    private val fusedLocationClient = LocationServices
        .getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation():
        Result<LocationPoint, LocationErrorKind> {
        if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            !context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            return Err(LocationErrorKind.PermissionDenied)
        }

        return suspendCancellableCoroutine { cont ->
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token,
            )
                .addOnSuccessListener { location ->
                    if (location != null) {
                        cont.resume(
                            Ok(
                                LocationPoint(
                                    location.latitude,
                                    location.longitude,
                                ),
                            ),
                        )
                    } else {
                        cont.resume(
                            Err(LocationErrorKind.LocationUnavailable),
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(
                        Err(LocationErrorKind.Unknown(exception)),
                    )
                }
        }
    }
}

fun Context.hasPermission(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(this, permission) ==
        PackageManager.PERMISSION_GRANTED
