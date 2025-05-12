package com.finoux.tradeapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.finoux.tradeapp.ui.screens.HomeScreen
import com.finoux.tradeapp.ui.theme.TradeappTheme
import com.finoux.tradesdk.TradeSdk

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TradeappTheme {
                val showTradeSdk = remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.systemBars.asPaddingValues())
                ) {
                    // HomeScreen that triggers opening of Stock Details when clicked
                    HomeScreen(
                        onStockClick = { isin ->
                            Log.d("MainActivity", "showTradeSdk.value: ${showTradeSdk.value}")
                            TradeSdk.openStockDetails(isin)
                            showTradeSdk.value = true // Trigger showing the SDK's stock detail screen
                        }
                    )

                    // Show TradeSdk when needed
                    if (showTradeSdk.value) {
                        TradeSdk.Host {
                            // This callback is triggered when SDK is closed
                            showTradeSdk.value = false // Close the SDK view and return to HomeScreen
                        }
                    }
                }
            }
        }
    }
}
