package com.idimi.garage.view.compose.components

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun LocationCheckScreen() {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var relaunchPermissionForLocation by remember {
        mutableStateOf(false)
    }
    // Launcher for requesting location permission
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // if permission granted, immediately check location services
            if (!isLocationEnabled(context)) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Location (GPS) is turned off",
                        actionLabel = "Open settings"
                    ).let { result ->
                        // If user clicked action, open location settings
                        if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                            context.openLocationSettings()
                        }
                    }
                }
            }
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "Location permission is required",
                    actionLabel = "Grant"
                ).let { result ->
                    if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                        // Re-request permission
                        relaunchPermissionForLocation = true
                    }
                }
            }
        }
    }

    if (relaunchPermissionForLocation) {
        permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        relaunchPermissionForLocation = false
    }
    // UI scaffold with Snackbar host
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "GPS & Permission check demo")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Request permission when button pressed
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }) {
                Text("Request location & check GPS")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                // manual quick-check (if permission already granted)
                if (!isPermissionGranted(context)) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Please grant location permission first",
                            actionLabel = "Grant"
                        ).let { res ->
                            if (res == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    }
                    return@Button
                }
                if (!isLocationEnabled(context)) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "GPS is OFF. Enable it in settings.",
                            actionLabel = "Open settings"
                        ).let { result ->
                            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                                context.openLocationSettings()
                            }
                        }
                    }
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("GPS is ON — good to go!")
                    }
                }
            }) {
                Text("Quick check (permission + GPS)")
            }
        }
    }
}
/** Helpers **/
private fun isLocationEnabled(context: Context): Boolean {
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return try {
        lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    } catch (ex: Exception) {
        false
    }
}
private fun isPermissionGranted(context: Context): Boolean {
    val perm = android.Manifest.permission.ACCESS_FINE_LOCATION
    val checked = androidx.core.content.ContextCompat.checkSelfPermission(context, perm)
    return checked == android.content.pm.PackageManager.PERMISSION_GRANTED
}
private fun Context.openLocationSettings() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    // If calling from Compose/Activity context, set flag not required. Use this to be safe:
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}