package com.idimi.garage.view

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.idimi.garage.view.compose.BottomBarWithNavHost
import com.idimi.garage.view.ui.theme.GarageTheme
import com.idimi.garage.view.viewmodel.GarageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val garageViewModel: GarageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GarageTheme {
                BottomBarWithNavHost(garageViewModel)
            }
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
}
