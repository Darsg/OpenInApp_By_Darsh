package com.example.darsh_practicle_task

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.darsh_practicle_task.model.DashboardResponse
import com.example.darsh_practicle_task.model.OtherLinks
import com.example.darsh_practicle_task.model.TopLink
import com.example.darsh_practicle_task.ui.theme.Darsh_practicle_taskTheme
import com.example.darsh_practicle_task.network.ApiService
import com.example.darsh_practicle_task.repository.DashboardRepository
import com.example.darsh_practicle_task.viewmodel.DashboardViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import android.graphics.Color as AndroidColor

class MainActivity : ComponentActivity() {

    var selectedTab = "Top Links";
    var position = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set status bar color
        val color = Color(0xFF0E6FFF)
        window.statusBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            Darsh_practicle_taskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
                    MainScreen(token)
                }
            }
        }
    }

    @Composable
    fun MainScreen(token: String) {
        val repository = DashboardRepository(ApiService.create())
        val viewModel: DashboardViewModel = viewModel(
            factory = DashboardViewModelFactory(repository)
        )

        val dashboardData by viewModel.dashboardData.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.fetchDashboardData(token)
        }

        dashboardData?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                MyHeader()
                BottomNavCustom(dashboardData!!)
            }

        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading...")
            }
        }
    }

    @Composable
    fun BottomNavCustom(dashboardData: DashboardResponse) {
        val navController = rememberNavController()
        val navBackStackEntry = navController.currentBackStackEntryAsState()

        Scaffold(
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    BottomAppBar(
                        cutoutShape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
                        backgroundColor = Color.Transparent // Transparent background to overlay FAB
                    ) {
                        BottomNavigation(
                            modifier = Modifier
                                .background(colorResource(id = R.color.topBanner))
                                .fillMaxWidth()
                        ) {
                            BottomNavigationItem(
                                icon = { Icon(Icons.Default.Home, contentDescription = "Link") },
                                label = { Text(text = "Home", fontSize = 12.sp) },
                                selected = navBackStackEntry.value?.destination?.route == "Link",
                                onClick = {
                                    navController.navigate("home")
                                }
                            )
                            BottomNavigationItem(
                                icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                                label = { Text(text = "Search", fontSize = 12.sp) },
                                selected = navBackStackEntry.value?.destination?.route == "search",
                                onClick = {
                                    navController.navigate("search")
                                }
                            )
                            // Hidden fifth tab
                            BottomNavigationItem(
                                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                                label = { Spacer(modifier = Modifier.size(0.dp)) }, // No label
                                selected = false,
                                onClick = { /* No action */ },
                                modifier = Modifier
                                    .size(0.dp) // Hide this tab
                            )
                            BottomNavigationItem(
                                icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                                label = { Text(text = "Favorites", fontSize = 12.sp) },
                                selected = navBackStackEntry.value?.destination?.route == "favorites",
                                onClick = {
                                    navController.navigate("favorites")
                                }
                            )
                            BottomNavigationItem(
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                label = { Text(text = "Settings", fontSize = 12.sp) },
                                selected = navBackStackEntry.value?.destination?.route == "settings",
                                onClick = {
                                    navController.navigate("settings")
                                }
                            )
                        }
                    }
                    // FAB positioned above the BottomAppBar
                    FloatingActionButton(
                        onClick = { /* Handle FAB click */ },
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        MainContent(dashboardData)
                    }
                }
                composable("search") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Search Screen")
                    }
                }
                composable("favorites") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Favorites Screen")
                    }
                }
                composable("settings") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Settings Screen")
                    }
                }
            }
        }
    }

    @Composable
    fun MainContent(dashboardData : DashboardResponse) {
        val context = LocalContext.current
        val backGroundColor = Color(ContextCompat.getColor(context, R.color.backGroundColor))

        var selectedTab by remember { mutableStateOf("Top Links") }
        val list = if (selectedTab == "Top Links") {
            dashboardData.data.top_links.map { it }
        } else {
            dashboardData.data.recent_links.map { it }
        }

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .background(backGroundColor)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            item {
                UserIntro()

                Spacer(modifier = Modifier.height(30.dp))

                DrawGraph()

                Spacer(modifier = Modifier.height(15.dp))

                GeneralInformation()

                Spacer(modifier = Modifier.height(30.dp))

                Row {
                    CustomTabs(onTabSelected = { newTab ->
                        selectedTab = newTab
                    })

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(backGroundColor)
                            .align(Alignment.CenterVertically)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_setting),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .graphicsLayer(rotationZ = 90f)
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))
            }

            items(list) { item ->
                ListItem(item = item)
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))

                CustomButton("View all Links", R.drawable.link)

                Spacer(modifier = Modifier.height(15.dp))

                CustomButtonShare("Talk with us", R.drawable.whatsapp, Color(0xFF4AD15F), dashboardData.support_whatsapp_number)

                Spacer(modifier = Modifier.height(15.dp))

                CustomButtonShare("Frequently Asked Question", R.drawable.help, Color(0xFF0E6FFF), dashboardData.support_whatsapp_number)
            }
        }
    }

    @Composable
    fun CustomTabs(onTabSelected: (String) -> Unit) {
        val context = LocalContext.current
        val topBanner = Color(ContextCompat.getColor(context, R.color.topBanner))
        val morningTextColor = Color(ContextCompat.getColor(context, R.color.morningTextColor))
        val backGroundColor = Color(ContextCompat.getColor(context, R.color.backGroundColor))

        var selectedIndex by remember { mutableStateOf(0) }
        val list = listOf("Top Links", "Recent Links")

        selectedIndex = position;

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backGroundColor)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(backGroundColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Set wrap content width so other elements display easily
                TabRow(
                    selectedTabIndex = selectedIndex,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .clip(RoundedCornerShape(50))
                        .padding(1.dp)
                        .background(backGroundColor),
                    indicator = { /* No indicator */ },
                    divider = { /* No divider */ }
                ) {
                    list.forEachIndexed { index, text ->
                        val selected = selectedIndex == index
                        Tab(
                            modifier = Modifier
                                .padding(horizontal = 0.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (selected) topBanner else backGroundColor),
                            selected = selected,
                            onClick = {
                                position = index;
                                selectedIndex = index;
                                onTabSelected(text) // Update the tab selection
                            },
                            text = {
                                Text(
                                    text = text,
                                    style = TextStyle(
                                        color = if (selected) Color.White else morningTextColor,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(backGroundColor)
                        .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                            .padding(4.dp)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun ListItem(item: TopLink) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column (
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    item.original_image.run {
                        Image(
                            painter = rememberImagePainter(data = this),
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier
                            .weight(0.8f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = item.title,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.created_at,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Gray
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.2f)
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = item.total_clicks.toString(),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Clicks",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Gray
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Bottom Row with border and background color
                DottedBorderBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp),
                    borderColor = Color.Blue,
                    borderWidth = 1.dp,
                    dotSize = 4.dp,
                    dotSpacing = 4.dp,
                    bottomCornerRadius = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp))
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.web_link,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color(0xFF0E6FFF)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )

                        val clipboard = LocalClipboardManager.current
                        val context = LocalContext.current

                        Image(
                            painter = painterResource(id = R.drawable.copy),
                            contentDescription = null,
                            modifier = Modifier
                                .size(15.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    clipboard.setText(AnnotatedString(item.web_link))
                                    Toast.makeText(context, "Text Copied!", Toast.LENGTH_SHORT).show()
                                }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun GeneralInformation(){
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(180.dp)
        ){
            PreviewHorizontalScrollableList()
        }

        CustomButton("View Analytics", R.drawable.profit)
    }

    @Composable
    fun CustomButtonShare(text: String, image: Int, color: Color, mobile: String) {
        val context = LocalContext.current
        val morningTextColor = Color(ContextCompat.getColor(context, R.color.morningTextColor))

        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$mobile")
                }
                context.startActivity(intent)
            },
            border = BorderStroke(1.dp, color),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = morningTextColor),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.05f)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }


    @Composable
    fun CustomButton(text: String, image: Int) {
        val context = LocalContext.current
        val morningTextColor = Color(ContextCompat.getColor(context, R.color.morningTextColor))

        OutlinedButton(
            onClick = { },
            border = BorderStroke(2.dp, Color.Gray),
            shape = RoundedCornerShape(16),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = morningTextColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }

    @Composable
    fun CustomListItem(item: OtherLinks) {
        val context = LocalContext.current
        val morningTextColor = Color(ContextCompat.getColor(context, R.color.morningTextColor))

        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            // Image positioned at the top-left corner
            Image(
                painter = painterResource(id = item.imgId),
                contentDescription = item.title,
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            // Column for text views positioned at the bottom-left
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = item.title,
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.description,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = morningTextColor
                    )
                )
            }
        }
    }

    @Composable
    fun DrawGraph(){
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)

        ){
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Overview",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 20.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.CenterStart),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "22 Aug - 23 Sept",
                            )

                            Image(
                                painter = painterResource(id = R.drawable.time),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(25.dp)
                            )
                        }
                    }
                }

                LineGraphScreen()
            }
        }
    }

    @Composable
    fun UserIntro(){
        val context = LocalContext.current
        val morningTextColor = Color(ContextCompat.getColor(context, R.color.morningTextColor))

        Box(
            modifier = Modifier
                .height(65.dp),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Good Morning",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = morningTextColor
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ajay Manva",
                        style = TextStyle(
                            fontSize = 25.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                        ),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .graphicsLayer(
                                rotationY = 180f,
                                alpha = 1f
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hello),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    }

                }
            }
        }
    }

    @Composable
    fun MyHeader() {
        val context = LocalContext.current
        val bannerColor = Color(ContextCompat.getColor(context, R.color.topBanner))
        val settingIconBG = Color(ContextCompat.getColor(context, R.color.settingIconBG))
        val backGroundColor = Color(ContextCompat.getColor(context, R.color.backGroundColor))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(bannerColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header Row
                Row(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dashboard",
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(settingIconBG)
                            .align(Alignment.CenterVertically)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_setting),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .graphicsLayer(rotationZ = 90f)
                                .align(Alignment.Center)
                        )
                    }
                }

                // White Box at the bottom
                Box(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(backGroundColor)
                ) {
                    // Add content here if needed
                }
            }
        }
    }

    @Composable
    fun DottedBorderBox(
        modifier: Modifier = Modifier,
        borderColor: Color = Color.Black,
        borderWidth: Dp = 2.dp,
        dotSize: Dp = 2.dp,
        dotSpacing: Dp = 2.dp,
        bottomCornerRadius: Dp = 16.dp,
        backgroundColor: Color = Color(0xFFE8F1FF),
        content: @Composable () -> Unit
    ) {
        Box(
            modifier = modifier
                .drawBehind {
                    val paint = android.graphics.Paint().apply {
                        color = borderColor.toArgb()
                        style = android.graphics.Paint.Style.STROKE
                        pathEffect = android.graphics.DashPathEffect(floatArrayOf(dotSize.toPx(), dotSpacing.toPx()), 0f)
                        strokeWidth = borderWidth.toPx()
                    }

                    val path = Path().apply {
                        val width = size.width
                        val height = size.height
                        val radiusPx = bottomCornerRadius.toPx()

                        // Move to top-left corner
                        moveTo(0f, 0f)
                        // Line to top-right corner
                        lineTo(width, 0f)
                        // Line to bottom-right corner
                        lineTo(width, height - radiusPx)
                        // Arc to bottom-right corner
                        arcTo(
                            rect = Rect(
                                left = width - 2 * radiusPx,
                                top = height - 2 * radiusPx,
                                right = width,
                                bottom = height
                            ),
                            startAngleDegrees = 0f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        // Line to bottom-left corner
                        lineTo(radiusPx, height)
                        // Arc to bottom-left corner
                        arcTo(
                            rect = Rect(
                                left = 0f,
                                top = height - 2 * radiusPx,
                                right = 2 * radiusPx,
                                bottom = height
                            ),
                            startAngleDegrees = 90f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        // Close the path
                        lineTo(0f, 0f)
                        close()
                    }

                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawPath(path.asAndroidPath(), paint)
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .background(backgroundColor)
                    .padding(borderWidth)
            ) {
                content()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewHorizontalScrollableList() {
        val sampleItems = listOf(
            OtherLinks(R.drawable.pin, "123", "Today's Click"),
            OtherLinks(R.drawable.location, "Ahemdabad", "Top Location"),
            OtherLinks(R.drawable.insta, "Instagram", "Top Source"),
        )
        LazyRow(
            contentPadding = PaddingValues(4.dp)
        ) {
            items(sampleItems) { item ->
                CustomListItem(item)
            }
        }
    }

    @Composable
    fun LineGraphScreen() {
        val xData = listOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f)
        val yData = listOf(2f, 4f, 8f, 6f, 10f, 12f, 14f, 16f, 18f, 20f)
        LineGraph(
            xData = xData,
            yData = yData,
            dataLabel = "Sample Data"
        )
    }

    @Composable
    fun LineGraph(
        xData: List<Float>,
        yData: List<Float>,
        dataLabel: String,
        modifier: Modifier = Modifier,
        lineColor: Color = Color.Transparent,
        axisTextColor: Color = Color.Black,
        backgroundColor: Color = Color.White,
        drawValues: Boolean = false,
        drawMarkers: Boolean = false,
        drawFilled: Boolean = true,
        descriptionEnabled: Boolean = false,
        legendEnabled: Boolean = false,
        yAxisRightEnabled: Boolean = false,
        xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM
    ) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                com.github.mikephil.charting.charts.LineChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    setBackgroundColor(backgroundColor.toArgb())

                    val entries = xData.zip(yData).map { (x, y) -> Entry(x, y) }
                    val dataSet = LineDataSet(entries, dataLabel).apply {
                        color = ContextCompat.getColor(context, R.color.topBanner)
                        setDrawValues(drawValues)
                        setDrawCircles(drawMarkers)
                        setDrawFilled(drawFilled)
                        fillDrawable = GradientDrawable(
                            GradientDrawable.Orientation.TOP_BOTTOM,
                            intArrayOf(
                                ContextCompat.getColor(context, R.color.topBanner),
                                AndroidColor.TRANSPARENT
                            )
                        )
                    }

                    data = LineData(dataSet)

                    description.isEnabled = descriptionEnabled
                    legend.isEnabled = legendEnabled

                    axisLeft.textColor = axisTextColor.toArgb()
                    axisRight.isEnabled = yAxisRightEnabled
                    xAxis.textColor = axisTextColor.toArgb()
                    xAxis.position = xAxisPosition

                    xAxis.granularity = 1f // Customize X-axis granularity
                    xAxis.axisLineColor = lineColor.toArgb() // Customize X-axis line color
                    axisLeft.axisLineColor = lineColor.toArgb() // Customize Y-axis line color

                    invalidate()
                }
            },
            update = { chart ->
                // Update chart if necessary
            }
        )
    }
}

class DashboardViewModelFactory(
    private val repository: DashboardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}