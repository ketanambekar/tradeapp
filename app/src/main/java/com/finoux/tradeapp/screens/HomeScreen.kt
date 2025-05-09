package com.finoux.tradeapp.ui.screens
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finoux.tradeapp.ui.theme.Pink80
import com.finoux.tradeapp.ui.theme.black
import com.finoux.tradeapp.ui.theme.green
import com.finoux.tradeapp.ui.theme.red
import com.finoux.tradeapp.ui.theme.white
import java.time.LocalTime

data class StockItem(val name: String, val isin: String, val ltp: String, val change: String)
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val sectionTitles = listOf("Top 3 Indices", "Top Gainers", "Top Losers", "Trending")

    val topGainers = listOf(
        StockItem("HDFC", "INE001A01036", "2200.50", "+5.1%"),
        StockItem("BHEL", "INE100A01010", "75.80", "+4.5%"),
        StockItem("LT", "INE018A01030", "1680.90", "+3.8%")
    )

    val topLosers = listOf(
        StockItem("M&M", "INE101A01017", "775.30", "-2.0%"),
        StockItem("Bajaj Finance", "INE296A01024", "7350.50", "-1.8%"),
        StockItem("SBI", "INE062A01020", "487.40", "-1.4%")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "TradeApp",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            MarketStatus()
            Spacer(modifier = Modifier.height(24.dp))
            Section(title = sectionTitles[1], stocks = topGainers)
            Spacer(modifier = Modifier.height(24.dp))
            Section(title = sectionTitles[2], stocks = topLosers)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun Section(title: String, stocks: List<StockItem>) {
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
        Text(
            text = "More >",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn {
        items(stocks) { stock ->
            StockCard(stock)
        }
    }
}


@Composable
fun StockCard(stock: StockItem) {
    val changeColor = if (stock.change.startsWith("+")) green
    else red
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(stock.name, color = white, fontWeight = FontWeight.Bold)
                Text("ISIN: ${stock.isin}", color = Pink80, fontSize = 12.sp)
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
