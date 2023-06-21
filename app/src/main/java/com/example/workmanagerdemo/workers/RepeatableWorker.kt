package com.example.workmanagerdemo.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class RepeatableWorker(
    context: Context,
    workerParameters: WorkerParameters
) :CoroutineWorker(context,workerParameters){
    override suspend fun doWork(): Result {
        delay(10000)
        Log.d("Custom Worker","Success repeatable work")
        return Result.success()
    }
}