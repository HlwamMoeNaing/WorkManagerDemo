package com.example.workmanagerdemo.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.workmanagerdemo.DemoApi
import com.example.workmanagerdemo.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import java.net.UnknownHostException

@HiltWorker
class CustomWorker @AssistedInject constructor(
    @Assisted private val api: DemoApi,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
):CoroutineWorker(context,workerParameters) {

   //  This is Introduction of WorkManager and Dependicy injection
    override suspend fun doWork(): Result {
//        println("Hello from worker")
        return try {
            val response = api.getPost()
            if(response.isSuccessful){
                Log.d("CustomWorker","Success")
                Log.d("CustomWorker","ID :${response.body()?.id} Title: ${response.body()?.title}")
                Result.success()
            }else{
                Log.d("CustomWorker","Retrying")
                Result.retry()
            }
        }catch (e:Exception){
            if (e is UnknownHostException){
                Log.d("CustomWorker","Retrying")
                Result.retry()
            }else{
                Log.d("CustomWorker","Error")
                Result.failure(Data.Builder().putString("error",e.toString()).build())
            }

        }

    }



}