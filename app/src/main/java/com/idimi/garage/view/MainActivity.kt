package com.idimi.garage.view

import android.app.ComponentCaller
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idimi.garage.util.isNetworkAvailable
import com.idimi.garage.view.compose.BottomBarWithNavHost
import com.idimi.garage.view.ui.theme.GarageTheme
import com.idimi.garage.view.viewmodel.GarageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val garageViewModel: GarageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreenScreen()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

        if (resultCode == RESULT_OK) {
            val uri: Uri? = data?.data//getStringExtra("URI")!!.toUri()
            uri?.let {
                val savedPath = garageViewModel.saveImageToAppStorage(this@MainActivity, it)
            }
        }
    }

    @Composable
    fun MainScreenScreen() {
        val context = LocalContext.current
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        var showErrorScreen by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = Unit, key2 = showErrorScreen) {
            // Get Places from Database or from Server
            garageViewModel.getAllPlaces()

            // If no internet connection
            if (!isNetworkAvailable(context)) {
                // DO NOT Show No Network Screen,
                // if you already have saved some places in the database.
                showErrorScreen = garageViewModel.allPlaces.isEmpty()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "No internet connection",
                        actionLabel = "Open settings",
                        withDismissAction = true
                    ).let { result ->
                        if (result == SnackbarResult.ActionPerformed) {
                            context.openNetworkSettings()
                        }
                    }
                }
            } else {
                showErrorScreen = false
            }
        }
        if (showErrorScreen) {
            // No Internet screen
            GarageTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { padding ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Not Internet Connection.\nCheck your Network Settings.",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "If you already switched ON your Network, press 'Reload'",
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (!isNetworkAvailable(context)) {
                                    coroutineScope.launch {
                                        Toast.makeText(
                                            context,
                                            "You are still NOT connected to the Internet. Check network settings.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    return@Button
                                }
                                showErrorScreen = false
                            }
                        ) {
                            Text(
                                text = "Reload",
                            )
                        }
                    }
                }
            }
        } else {
            GarageTheme {
                BottomBarWithNavHost(
                    snackbarHostState,
                    garageViewModel
                )
            }
        }
    }
}

fun Context.openNetworkSettings() {
    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
