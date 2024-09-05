package com.idiotfrogs.myapplication

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            MainScreen()
            TestScreen(context = this)
        }
    }
}

@Composable
fun TestScreen(context: Activity) {
    var text by remember { mutableStateOf("") }
    var requestedPermission  by remember { mutableStateOf("") }
    var onWriteAlertDialog by remember { mutableStateOf(false) }
    var onReadAlertDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            Log.d("test1234", "onResult")
            if (isGranted) {
                Log.d("test12345", "isGranted")
                Toast.makeText(context, "권한 허용됨", Toast.LENGTH_SHORT).show()
//                if (requestedPermission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
//                    Log.d("test123456", "requestedPermission: $requestedPermission")
//                    val deviceId = Storage(context).getDeviceId()
//                    Storage(context).saveDeviceId(deviceId)
//                } else if (requestedPermission == Manifest.permission.READ_EXTERNAL_STORAGE) {
//                    Log.d("test123456", "requestedPermission: $requestedPermission")
//                    text = Storage(context).loadDeviceId() ?: "null"
//                }
            } else {
                Toast.makeText(context, "권한 거부됨", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (onWriteAlertDialog) {
            AlertDialog(
                onDismissRequest = { onWriteAlertDialog = false },
                title = { Text("권한 요청") },
                text = { Text("앱을 사용하려면 외부 저장소 액세스 권한이 필요합니다.") },
                confirmButton = {
                    Button(onClick = {
                        requestedPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
                        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        onWriteAlertDialog = false
                    }) {
                        Text("허용")
                    }
                },
                dismissButton = {
                    Button(onClick = { onWriteAlertDialog = false }) {
                        Text("거부")
                    }
                }
            )
        }

        if (onReadAlertDialog) {
            AlertDialog(
                onDismissRequest = { onReadAlertDialog = false },
                title = { Text("권한 요청") },
                text = { Text("앱을 사용하려면 외부 저장소 액세스 권한이 필요합니다.") },
                confirmButton = {
                    Button(onClick = {
                        requestedPermission = Manifest.permission.READ_EXTERNAL_STORAGE
                        launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        onReadAlertDialog = false
                    }) {
                        Text("허용")
                    }
                },
                dismissButton = {
                    Button(onClick = { onReadAlertDialog = false }) {
                        Text("거부")
                    }
                }
            )
        }

        Button(onClick = {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 권한이 이미 허용된 경우
                val deviceId = Storage(context).getDeviceId()
                Storage(context).saveDeviceId(deviceId)
                Toast.makeText(context, "$deviceId 가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 권한 요청
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    onWriteAlertDialog = true
                } else {
                    requestedPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
                    launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }) {
            Text(text = "저장")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 권한이 이미 허용된 경우
                text = Storage(context).loadDeviceId() ?: "null"
                Toast.makeText(context, "$text 가 조회되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 권한 요청
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    onReadAlertDialog = true
                } else {
                    requestedPermission = Manifest.permission.READ_EXTERNAL_STORAGE
                    Log.d("test123", requestedPermission)
                    launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }) {
            Text(text = "조회")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = text)
    }
}
