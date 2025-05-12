package com.finoux.tradeapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finoux.tradeapp.ui.theme.Pink80
import com.finoux.tradeapp.ui.theme.green
import com.finoux.tradeapp.ui.theme.red
import com.finoux.tradeapp.ui.theme.white
import com.finoux.tradesdk.TradeSdk
import java.time.LocalTime

// Data model for StockItem
data class StockItem(val name: String, val isin: String, val ltp: String, val change: String)

// HomeScreen Composable which sets up the layout for the stocks
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(onStockClick: (String) -> Unit) {
    val sectionTitles = listOf("Watchlist")

    // Sample stock data
    val topGainers = listOf(
        StockItem("HDFC", "INE001A01036", "2200.50", "+5.1%"),
        StockItem("BHEL", "INE100A01010", "75.80", "+4.5%"),
        StockItem("LT", "INE018A01030", "1680.90", "+3.8%"),
        StockItem("M&M", "INE101A01017", "775.30", "-2.0%"),
        StockItem("Bajaj Finance", "INE296A01024", "7350.50", "-1.8%"),
        StockItem("SBI", "INE062A01020", "487.40", "-1.4%"),
    )


    // Scaffold layout with top bar and main content
    Scaffold(
        containerColor = Color(0xFF0D161F),
        topBar =
            {
                TradeTopBar(
                    onMenuClick = { /* Handle menu */ },
                    onNotificationClick = { /* Handle notification */ }
                )
            },

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            MarketOverviewRow()
            MarketStatus()
            Spacer(modifier = Modifier.height(24.dp))
            Section(title = sectionTitles[0], stocks = topGainers, onStockClick = onStockClick)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Section composable to display stock lists
@Composable
fun Section(title: String, stocks: List<StockItem>, onStockClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Pink80
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn {
        items(stocks) { stock ->
            StockCard(stock, onClick = { onStockClick(stock.isin) })
        }
    }
}

// StockCard composable to display individual stock item
@Composable
fun StockCard(stock: StockItem, onClick: () -> Unit) {
    val changeColor = if (stock.change.startsWith("+")) green else red

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .clickable {
                onClick()
                try {
                    Log.d("TradeSdk", "Opening stock details for ISIN: ${stock.isin}")
                    TradeSdk.openStockDetails(stock.isin)
                } catch (e: Exception) {
                    Log.e("TradeSdk", "Error opening stock details: ${e.message}")
                }
            }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stock icon/avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stock.name.first().uppercase(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(stock.name, color = white, fontWeight = FontWeight.Bold)
                Text("${stock.isin}", color = Pink80, fontSize = 12.sp)
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text("LTP: ₹${stock.ltp}", color = white, fontSize = 12.sp)
                Text("Change: ${stock.change}", color = changeColor, fontSize = 12.sp)
            }
        }
    }
}

// Market status composable to show whether the market is open or closed
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MarketStatus() {
    val currentTime = LocalTime.now()
    val marketOpenTime = LocalTime.of(9, 0)
    val marketCloseTime = LocalTime.of(15, 30)
    val isMarketOpen = currentTime.isAfter(marketOpenTime) && currentTime.isBefore(marketCloseTime)
    val dotColor = if (isMarketOpen) Color.Green else Color.Gray
    val statusText = if (isMarketOpen) "Market Open" else "Market Closed"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Green or Grey dot
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = statusText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
@Composable
fun MarketOverviewRow() {
    val bgColor = MaterialTheme.colorScheme.surfaceVariant
    val positiveColor = Color(0xFF4CAF50) // Green for positive change
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Triple("NIFTY50", "24,907.15", "(+3.75%)"),
            Triple("SENSEX", "82,353.97", "(+3.65%)"),
            Triple("BANKNIFTY", "55,442.85", "(-2.42%)"),
        ).forEach { (title, value, change) ->
            MarketIndexItem(title, value, change, bgColor, if(change.startsWith("(+")) green else red)
        }
    }

}

@Composable
fun MarketIndexItem(
    title: String,
    value: String,
    change: String,
    bgColor: Color,
    changeColor: Color
) {
    Column(
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .widthIn(min = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = change,
            fontSize = 12.sp,
            color = changeColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeTopBar(
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                "TradeApp",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0D161F)
        )
    )
}