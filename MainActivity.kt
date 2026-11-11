package com.voxella.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

private val Dark = Color(0xFF090D1B)
private val CardColor = Color(0xFF171E35)
private val Blue = Color(0xFF36B9FF)
private val Purple = Color(0xFF9B5CFF)
private val White = Color.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(9,13,27)
        window.navigationBarColor = android.graphics.Color.rgb(9,13,27)

        setContent {
            VoxellaApp()
        }
    }
}

@Composable
fun VoxellaApp() {
    var page by remember { mutableStateOf("Splash") }

    LaunchedEffect(Unit) {
        delay(1800)
        page = "Login"
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Blue,
            background = Dark,
            surface = CardColor
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Dark
        ) {
            when (page) {
                "Splash" -> SplashScreen()
                "Login" -> LoginScreen { page = "Home" }
                else -> MainScreen(page) { page = it }
            }
        }
    }
}

@Composable
fun VoxellaLogo(size: Dp = 90.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(size / 3))
            .background(
                Brush.linearGradient(
                    listOf(Blue, Purple, Color(0xFFFF4E91))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "V",
            fontSize = size.value.sp * 0.65f,
            fontWeight = FontWeight.Black,
            color = White
        )
    }
}

@Composable
fun SplashScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Dark),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            VoxellaLogo(110.dp)
            Spacer(Modifier.height(20.dp))
            Text(
                "VOXELLA",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = White,
                letterSpacing = 4.sp
            )
            Text(
                "CONNECT • CREATE • GO LIVE",
                color = Blue,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(35.dp))
            CircularProgressIndicator(color = Blue)
        }
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        Modifier.fillMaxSize().padding(25.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            VoxellaLogo(100.dp)
            Spacer(Modifier.height(18.dp))
            Text(
                "Welcome to VOXELLA",
                color = White,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Your world. Your voice. Your community.",
                color = Color.LightGray
            )
            Spacer(Modifier.height(35.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                )
            ) {
                Text("CONTINUE", color = Dark, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(12.dp))
            Text(
                "Email authentication will be connected with Firebase.",
                color = Color.Gray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MainScreen(
    currentPage: String,
    navigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Dark,
        topBar = {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VoxellaLogo(38.dp)
                Spacer(Modifier.width(10.dp))
                Text(
                    "VOXELLA",
                    color = White,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { navigate("Notifications") }) {
                    Icon(Icons.Default.Notifications, null, tint = White)
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = CardColor) {
                val items = listOf(
                    Triple("Home", Icons.Default.Home, "Home"),
                    Triple("Videos", Icons.Default.PlayCircle, "Videos"),
                    Triple("Live", Icons.Default.LiveTv, "Live"),
                    Triple("Messages", Icons.Default.Chat, "Messages"),
                    Triple("Profile", Icons.Default.Person, "Profile")
                )

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentPage == item.first,
                        onClick = { navigate(item.first) },
                        icon = { Icon(item.second, null) },
                        label = { Text(item.third, fontSize = 9.sp) }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            Modifier.fillMaxSize().padding(padding)
        ) {
            when (currentPage) {
                "Home" -> HomePage(navigate)
                "Videos" -> VideoPage()
                "Live" -> LivePage()
                "Messages" -> MessagesPage()
                "Profile" -> ProfilePage()
                "Wallet" -> WalletPage()
                "Notifications" -> SimplePage("Notifications")
                else -> HomePage(navigate)
            }
        }
    }
}

@Composable
fun HomePage(navigate: (String) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        item {
            Text(
                "Discover your world",
                color = White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureCard("Watch Videos", "Vertical video feed", Icons.Default.PlayCircle) {
                    navigate("Videos")
                }
                FeatureCard("Go Live", "Video & Audio", Icons.Default.LiveTv) {
                    navigate("Live")
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureCard("Messages", "Connect with people", Icons.Default.Chat) {
                    navigate("Messages")
                }
                FeatureCard("My Wallet", "Coins & rewards", Icons.Default.AccountBalanceWallet) {
                    navigate("Wallet")
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Trending on VOXELLA", color = White, fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
        }
        items(5) { index ->
            VideoPreview("Trending Creator ${index + 1}")
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.weight(1f).height(125.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CardColor),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = Blue, modifier = Modifier.size(27.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, color = White, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color.LightGray, fontSize = 10.sp)
        }
    }
}

@Composable
fun VideoPage() {
    var liked by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().background(Color(0xFF10172A))) {
        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.PlayCircle, null, tint = Blue, modifier = Modifier.size(75.dp))
            Text("VOXELLA VIDEO FEED", color = White, fontSize = 22.sp,
                fontWeight = FontWeight.Bold)
            Text("Full-screen vertical videos", color = Color.LightGray)
            Spacer(Modifier.height(15.dp))
            Text("Video player and upload service coming in backend integration.",
                color = Color.LightGray, textAlign = TextAlign.Center,
                modifier = Modifier.padding(20.dp))
        }
        Column(
            Modifier.align(Alignment.CenterEnd).padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { liked = !liked }) {
                Icon(
                    Icons.Default.Favorite,
                    null,
                    tint = if (liked) Color.Red else White
                )
            }
            Text(if (liked) "Liked" else "Like", color = White, fontSize = 11.sp)
            Spacer(Modifier.height(15.dp))
            Icon(Icons.Default.Comment, null, tint = White)
            Text("Comment", color = White, fontSize = 11.sp)
            Spacer(Modifier.height(15.dp))
            Icon(Icons.Default.Share, null, tint = White)
            Text("Share", color = White, fontSize = 11.sp)
        }
    }
}

@Composable
fun VideoPreview(name: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Box(Modifier.fillMaxSize()) {
            Icon(
                Icons.Default.PlayCircle,
                null,
                tint = Blue,
                modifier = Modifier.align(Alignment.Center).size(45.dp)
            )
            Text(
                name,
                color = White,
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
            )
        }
    }
}

@Composable
fun LivePage() {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("VOXELLA LIVE", color = White, fontSize = 25.sp,
                fontWeight = FontWeight.Bold)
            Text("Connect with your audience", color = Color.LightGray)
            Spacer(Modifier.height(20.dp))
            LiveCard("Video Live", "Start a video broadcast", Icons.Default.Videocam)
            LiveCard("Audio Live", "Create an audio room", Icons.Default.Mic)
            Text("Live rooms", color = White, fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
        }
        items(5) {
            LiveCard("Creator Room ${it + 1}", "Live room preview", Icons.Default.Podcasts)
        }
    }
}

@Composable
fun LiveCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        Modifier.fillMaxWidth().padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Blue, modifier = Modifier.size(35.dp))
            Spacer(Modifier.width(15.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = White, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.LightGray, fontSize = 12.sp)
            }
            Text("OPEN", color = Blue, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MessagesPage() {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text("Messages", color = White, fontSize = 27.sp,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))
        repeat(6) {
            Row(
                Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(48.dp).clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Blue, Purple))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = White)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("VOXELLA User ${it + 1}", color = White,
                        fontWeight = FontWeight.Bold)
                    Text("Start a new conversation", color = Color.LightGray,
                        fontSize = 12.sp)
                }
            }
            HorizontalDivider(color = Color.DarkGray)
        }
    }
}

@Composable
fun WalletPage() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("My Wallet", color = White, fontSize = 27.sp,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = CardColor),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(25.dp)) {
                Text("COIN BALANCE", color = Color.LightGray)
                Text("0", color = Blue, fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold)
                Text("Demo balance only", color = Color.LightGray)
            }
        }
        Spacer(Modifier.height(18.dp))
        Text("Creator earnings", color = White, fontSize = 19.sp)
        Text("Separate eligible creator ledger will be connected securely.",
            color = Color.LightGray)
        Spacer(Modifier.height(18.dp))
        Text("Reward points", color = White, fontSize = 19.sp)
        Text("Non-cash rewards will be displayed separately.",
            color = Color.LightGray)
    }
}

@Composable
fun ProfilePage() {
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VoxellaLogo(85.dp)
        Spacer(Modifier.height(15.dp))
        Text("VOXELLA MEMBER", color = White, fontSize = 23.sp,
            fontWeight = FontWeight.Bold)
        Text("User ID: Assigned by backend", color = Blue)
        Spacer(Modifier.height(25.dp))
        ProfileOption("Edit Profile")
        ProfileOption("Followers & Following")
        ProfileOption("VIP Membership")
        ProfileOption("Frames & Badges")
        ProfileOption("Business Account")
        ProfileOption("Settings & Privacy")
    }
}

@Composable
fun ProfileOption(title: String) {
    Card(
        Modifier.fillMaxWidth().padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Row(Modifier.padding(17.dp)) {
            Text(title, color = White)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Blue)
        }
    }
}

@Composable
fun SimplePage(title: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title, color = White, fontSize = 25.sp)
    }
}
