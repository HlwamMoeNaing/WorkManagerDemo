package com.example.workmanagerdemo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.workmanagerdemo.workers.CustomExpeditedWorker
import com.example.workmanagerdemo.workers.CustomWorker
import com.example.workmanagerdemo.workers.RepeatableWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication:Application(), Configuration.Provider {
    @Inject
    lateinit var customWorkerFactory: CustomWorkerFactory


    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
          //  .setWorkerFactory(customWorkerFactory)
           // .setWorkerFactory(CustomExpeditedWorker())
            .setWorkerFactory(CustomRepeatableWorker())
            .build()

}
class CustomWorkerFactory @Inject constructor(private val api:DemoApi):WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = CustomWorker(api,appContext,workerParameters)

}

class CustomExpeditedWorker:WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = CustomExpeditedWorker(appContext,workerParameters)

}

class CustomRepeatableWorker:WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = RepeatableWorker(appContext,workerParameters)

}