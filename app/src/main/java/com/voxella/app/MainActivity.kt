
package com.voxella.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Dark = Color(0xFF090D1B)
private val Panel = Color(0xFF171E35)
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
    var loggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1800)
        page = if (loggedIn) "Home" else "Login"
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Blue,
            background = Dark,
            surface = Panel
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Dark
        ) {
            when (page) {
                "Splash" -> SplashScreen()

                "Login" -> LoginScreen(
                    onLogin = {
                        loggedIn = true
                        page = "Home"
                    }
                )

                else -> MainScreen(
                    currentPage = page,
                    navigate = { page = it },
                    onLogout = {
                        loggedIn = false
                        page = "Login"
                    }
                )
            }
        }
    }
}

@Composable
fun VoxellaLogo(size: androidx.compose.ui.unit.Dp = 90.dp) {
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
            color = White,
            fontSize = (size.value * 0.65f).sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun SplashScreen() {
    Box(
        Modifier.fillMaxSize().background(Dark),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            VoxellaLogo(115.dp)

            Spacer(Modifier.height(20.dp))

            Text(
                "VOXELLA",
                color = White,
                fontSize = 35.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp
            )

            Text(
                "CONNECT • CREATE • GO LIVE",
                color = Blue,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(30.dp))
            CircularProgressIndicator(color = Blue)
        }
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Box(
        Modifier.fillMaxSize().verticalScroll(
            androidx.compose.foundation.rememberScrollState()
        ).padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            VoxellaLogo(105.dp)

            Spacer(Modifier.height(18.dp))

            Text(
                "WELCOME TO VOXELLA",
                color = White,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                "Your world. Your voice. Your community.",
                color = Color.LightGray,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(35.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    error = ""
                },
                label = { Text("Email Address") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    error = ""
                },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            if (error.isNotEmpty()) {
                Text(error, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = {
                    if (!email.contains("@") || !email.contains(".")) {
                        error = "Enter a valid email address"
                    } else if (password.length < 6) {
                        error = "Password must have at least 6 characters"
                    } else {
                        onLogin()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    "LOGIN & CONTINUE",
                    color = Dark,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "VOXELLA",
                color = Blue,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Text(
                "Connect with the world.",
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun MainScreen(
    currentPage: String,
    navigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        containerColor = Dark,

        topBar = {
            Row(
                Modifier.fillMaxWidth().padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VoxellaLogo(40.dp)

                Spacer(Modifier.width(10.dp))

                Text(
                    "VOXELLA",
                    color = White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 21.sp
                )

                Spacer(Modifier.weight(1f))

                TextButton(
                    onClick = { navigate("Notifications") }
                ) {
                    Text("🔔", fontSize = 21.sp)
                }
            }
        },

        bottomBar = {
            NavigationBar(containerColor = Panel) {

                val tabs = listOf(
                    "Home",
                    "Videos",
                    "Live",
                    "Messages",
                    "Profile"
                )

                val symbols = listOf("⌂", "▶", "●", "✉", "♙")

                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = currentPage == tab,
                        onClick = { navigate(tab) },
                        icon = {
                            Text(symbols[index], fontSize = 20.sp)
                        },
                        label = {
                            Text(tab, fontSize = 9.sp)
                        }
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
                "Profile" -> ProfilePage(navigate, onLogout)
                "Wallet" -> WalletPage()
                "Notifications" -> SimplePage("Notifications")
                "Settings" -> SimplePage("Settings & Privacy")
                "Business" -> SimplePage("Business Accounts")
                "Followers" -> SimplePage("Followers & Following")
                else -> HomePage(navigate)
            }
        }
    }
}

@Composable
fun HomePage(navigate: (String) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                "Discover Your World",
                color = White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Welcome to your VOXELLA community",
                color = Color.LightGray
            )

            Spacer(Modifier.height(22.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureCard(
                    "Watch Videos",
                    "Explore creators",
                    "▶",
                    Modifier.weight(1f)
                ) { navigate("Videos") }

                FeatureCard(
                    "Go Live",
                    "Video & Audio",
                    "●",
                    Modifier.weight(1f)
                ) { navigate("Live") }
            }

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureCard(
                    "Messages",
                    "Chat with people",
                    "✉",
                    Modifier.weight(1f)
                ) { navigate("Messages") }

                FeatureCard(
                    "My Wallet",
                    "Coins & rewards",
                    "◆",
                    Modifier.weight(1f)
                ) { navigate("Wallet") }
            }

            Spacer(Modifier.height(25.dp))

            Text(
                "Trending on VOXELLA",
                color = White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))
        }

        items((1..8).toList()) { number ->
            VideoPreview("Trending Creator $number")
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    subtitle: String,
    symbol: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(135.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            Modifier.padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(symbol, color = Blue, fontSize = 27.sp)

            Spacer(Modifier.height(8.dp))

            Text(
                title,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Text(
                subtitle,
                color = Color.LightGray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun VideoPage() {
    var liked by remember { mutableStateOf(false) }

    Box(
        Modifier.fillMaxSize().background(Color(0xFF10172A))
    ) {
        Column(
            Modifier.align(Alignment.Center).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("▶", color = Blue, fontSize = 75.sp)

            Text(
                "VOXELLA VIDEO FEED",
                color = White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Discover creators and videos",
                color = Color.LightGray
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Video playback and upload will be connected with the backend.",
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )

            Button(onClick = { liked = !liked }) {
                Text(if (liked) "♥ Liked" else "♡ Like")
            }
        }
    }
}

@Composable
fun VideoPreview(name: String) {
    Card(
        Modifier.fillMaxWidth().padding(bottom = 12.dp).height(145.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(
                "▶",
                color = Blue,
                fontSize = 40.sp,
                modifier = Modifier.align(Alignment.Center)
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
            Text(
                "VOXELLA LIVE",
                color = White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Connect with your audience",
                color = Color.LightGray
            )

            Spacer(Modifier.height(20.dp))

            LiveCard("Video Live", "Start video broadcast", "📹")
            LiveCard("Audio Live", "Create audio room", "🎙")

            Text(
                "Live Rooms",
                color = White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))
        }

        items((1..8).toList()) {
            LiveCard(
                "Creator Room $it",
                "Live room preview",
                "🔴"
            )
        }
    }
}

@Composable
fun LiveCard(
    title: String,
    subtitle: String,
    symbol: String
) {
    Card(
        Modifier.fillMaxWidth().padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(symbol, fontSize = 30.sp)

            Spacer(Modifier.width(14.dp))

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
        Text(
            "Messages",
            color = White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        repeat(8) {
            Card(
                Modifier.fillMaxWidth().padding(bottom = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Panel)
            ) {
                Row(
                    Modifier.padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("👤", fontSize = 30.sp)

                    Spacer(Modifier.width(15.dp))

                    Column {
                        Text(
                            "VOXELLA User ${it + 1}",
                            color = White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Start a new conversation",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WalletPage() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            "My Wallet",
            color = White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Panel),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(25.dp)) {
                Text("COIN BALANCE", color = Color.LightGray)

                Text(
                    "0",
                    color = Blue,
                    fontSize = 45.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text("Demo balance", color = Color.LightGray)
            }
        }

        Spacer(Modifier.height(20.dp))

        ProfileOption("Buy Coins")
        ProfileOption("Gift History")
        ProfileOption("Creator Earnings")
        ProfileOption("Transactions")
    }
}

@Composable
fun ProfilePage(
    navigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(
            androidx.compose.foundation.rememberScrollState()
        ).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VoxellaLogo(90.dp)

        Spacer(Modifier.height(15.dp))

        Text(
            "VOXELLA MEMBER",
            color = White,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "User ID: Assigned by backend",
            color = Blue
        )

        Spacer(Modifier.height(25.dp))

        ProfileOption("Edit Profile")
        ProfileOption("VIP Membership")
        ProfileOption("Frames & Badges")

        ClickableOption("Followers & Following") {
            navigate("Followers")
        }

        ClickableOption("Business Account") {
            navigate("Business")
        }

        ClickableOption("Wallet") {
            navigate("Wallet")
        }

        ClickableOption("Settings & Privacy") {
            navigate("Settings")
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDA3655)
            )
        ) {
            Text("LOGOUT", color = White)
        }
    }
}

@Composable
fun ProfileOption(title: String) {
    Card(
        Modifier.fillMaxWidth().padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Row(Modifier.padding(17.dp)) {
            Text(title, color = White)
        }
    }
}

@Composable
fun ClickableOption(
    title: String,
    onClick: () -> Unit
) {
    Card(
        Modifier.fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Row(Modifier.padding(17.dp)) {
            Text(title, color = White)
            Spacer(Modifier.weight(1f))
            Text("›", color = Blue, fontSize = 20.sp)
        }
    }
}

@Composable
fun SimplePage(title: String) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            color = White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
