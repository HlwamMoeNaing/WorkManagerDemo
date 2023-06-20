package com.example.workmanagerdemo

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.example.workmanagerdemo.ui.theme.WorkManagerDemoTheme
import com.example.workmanagerdemo.workers.CustomExpeditedWorker
import com.example.workmanagerdemo.workers.CustomWorker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("PermissionLaunchedDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is introduction
//        val workRequest = OneTimeWorkRequestBuilder<CustomWorker>()
//            .setInitialDelay(java.time.Duration.ofSeconds(10))
//            .setBackoffCriteria(
//                backoffPolicy = BackoffPolicy.LINEAR,
//                duration = java.time.Duration.ofSeconds(15)
//            ).build()
//        WorkManager.getInstance(applicationContext).enqueue(workRequest)
//        setContent {
//            WorkManagerDemoTheme() {
//
//            }
//        }

        //



        setContent {
            WorkManagerDemoTheme() {
                val permission =
                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)


                if(permission.status.isGranted){
                    val workRequest = OneTimeWorkRequestBuilder<CustomExpeditedWorker>()
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) // IF we run as EXPEDITED, need to delete delay time
                        //.setInitialDelay(Duration.ofSeconds(10))
                        .setBackoffCriteria(
                            backoffPolicy = BackoffPolicy.LINEAR,
                            duration = Duration.ofSeconds(15)
                        )
                        .build()
                    WorkManager.getInstance(applicationContext).enqueue(workRequest)
                }else{
                    permission.launchPermissionRequest()
                }

            }
        }

    }
}

