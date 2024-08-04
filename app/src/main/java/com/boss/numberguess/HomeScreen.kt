package com.boss.numberguess

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlin.random.Random
import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: SharedNumberGuessViewModel
) {

    var text by remember {
        mutableStateOf("")
    }
    val answer by viewModel.answer.collectAsState()
    val isGameOver by viewModel.isGameOver.collectAsState()
    val firstSentence by viewModel.firstSentence.collectAsState()
    val tries by viewModel.tries.collectAsState()
    var guessedNumber = ""
    val keyboardController = LocalSoftwareKeyboardController.current

    fun handleSubmit() {
        viewModel.tries.value += 1
        if (
//            true ||
            guessedNumber == answer
            ) {
            viewModel.firstSentence.value = """Bingo! You win!
                |
                |
                |Attempts: ${tries}
            """.trimMargin()
            viewModel.isGameOver.value = true
        } else {
            if ( text.toInt() > answer.toInt()) {
                viewModel.firstSentence.value = "Too high!"
            } else {
                viewModel.firstSentence.value = "Too low!"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFfeffd9))
            .clickable { keyboardController?.hide() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = firstSentence,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3700b3),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
        )
        Text(
            text = "${if (isGameOver) answer else "??"}",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        TextField(
            value = text,
            onValueChange = {newText -> text = newText},
            placeholder = { Text(text = "Guess 1 to 25") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 40.dp)
        )

// Common button modifier
        val buttonModifier = Modifier
            .padding(bottom = 20.dp)
            .height(50.dp)
            .width(150.dp)

        Button(
            onClick = {
                if (text == "") {
                    keyboardController?.hide()
                } else {
                    guessedNumber = text
                    handleSubmit()
                    keyboardController?.hide()
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF3700b3)),
            modifier = buttonModifier
        ) {
            Text(text = "Submit", fontSize = 20.sp)
        }
        Button(
            onClick = { text = "" },
            colors = ButtonDefaults.buttonColors(Color(0xFFff5900)),
            modifier = buttonModifier
        ) {
            Text(text = "Clear", fontSize = 20.sp)
        }
        Button(
            onClick = { viewModel.startOver() },
            colors = ButtonDefaults.buttonColors(Color(0xFF00dec9)),
            modifier = buttonModifier
        ) {
            Text(text = "Start Over", fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        modifier = Modifier,
        viewModel = SharedNumberGuessViewModel()
    )
}

// FOR PUSH NOTIFICATIONS (NOT USED)
//    val context = LocalContext.current
//    val notificationPermissionState = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            // Permission granted, you can send notifications
//        } else {
//            // Permission denied, handle accordingly
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            when {
//                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
//                    // Permission already granted
//                }
//                else -> {
//                    // Request permission
//                    notificationPermissionState.launch(Manifest.permission.POST_NOTIFICATIONS)
//                }
//            }
//        }
//    }
//    fun sendNotification() {
//        // Create a notification channel (required for Android O and higher)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channelId = "guess_game_channel"
//            val channelName = "Guess Game Notifications"
//            val channelDescription = "Notifications for the Guess Game"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
//                description = channelDescription
//            }
//            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//        // Create and show the notification
//        val notificationBuilder = NotificationCompat.Builder(context, "guess_game_channel")
//            .setSmallIcon(androidx.core.R.drawable.notification_icon_background) // Replace with your own icon
//            .setContentTitle("Guess Game")
//            .setContentText("Oops! Try again.")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//
//        with(NotificationManagerCompat.from(context)) {
//            notify(1001, notificationBuilder.build()) // ID for the notification
//        }
//    }