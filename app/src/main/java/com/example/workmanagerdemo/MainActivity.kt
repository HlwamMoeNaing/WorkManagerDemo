package com.example.workmanagerdemo

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workmanagerdemo.ui.theme.WorkManagerDemoTheme
import com.example.workmanagerdemo.workers.CustomExpeditedWorker
import com.example.workmanagerdemo.workers.CustomWorker
import com.example.workmanagerdemo.workers.RepeatableWorker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.time.Duration
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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


        // This is Expedited Work
//        setContent {
//            WorkManagerDemoTheme() {
//                val permission =
//                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
//
//                LaunchedEffect(key1 = Unit){
//                    if(permission.status.isGranted){
//                        val workRequest = OneTimeWorkRequestBuilder<CustomExpeditedWorker>()
//                            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) // IF we run as EXPEDITED, need to delete delay time
//                            //.setInitialDelay(Duration.ofSeconds(10))
//                            .setBackoffCriteria(
//                                backoffPolicy = BackoffPolicy.LINEAR,
//                                duration = Duration.ofSeconds(15)
//                            )
//                            .build()
//                        WorkManager.getInstance(applicationContext).enqueue(workRequest)
//                    }else{
//                        permission.launchPermissionRequest()
//                    }
//                }
//
//
//
//
//            }
//        }

        // This is for Repeatable work
        setContent {
            WorkManagerDemoTheme() {
                val lifeCycleOwner = LocalLifecycleOwner.current
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .setRequiresCharging(true)
                    .build()
                LaunchedEffect(key1 = Unit) {
                    val workRequest = PeriodicWorkRequestBuilder<RepeatableWorker>(
                        repeatInterval = 1,
                        repeatIntervalTimeUnit = TimeUnit.HOURS,
                        flexTimeInterval = 15,
                        flexTimeIntervalUnit = TimeUnit.MINUTES
                    ).setBackoffCriteria(
                        backoffPolicy = BackoffPolicy.LINEAR,
                        duration = Duration.ofSeconds(15)
                    ).setConstraints(constraints).build()

                    val workManager = WorkManager.getInstance(applicationContext)
                    workManager.enqueueUniquePeriodicWork(
                        "myWork",
                        ExistingPeriodicWorkPolicy.KEEP,
                        workRequest
                    )
                    workManager.getWorkInfosForUniqueWorkLiveData("myWork")
                        .observe(lifeCycleOwner){
                            it.forEach { workInfo->
                                Log.d("MainActivity", "${workInfo.state}")
                            }
                        }
                    delay(5000)
                    workManager.cancelUniqueWork("myWork")
                }

            }
        }

    }
}

