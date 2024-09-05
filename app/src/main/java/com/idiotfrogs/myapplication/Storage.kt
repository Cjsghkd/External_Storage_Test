package com.idiotfrogs.myapplication

import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.Settings.Secure
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class Storage(private val context: Context) {
    fun getDeviceId(): String = Secure.getString(context.contentResolver, Secure.ANDROID_ID)

    fun saveDeviceId(deviceId: String) {
        val externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(externalStorageDir, "device_id.txt")

        if (!file.exists()) {
            file.createNewFile()
        }

        FileOutputStream(file).use { output ->
            output.write(deviceId.toByteArray())
        }
    }

    fun loadDeviceId(): String? {
        val externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(externalStorageDir, "device_id.txt")

        if (file.exists()) {
            FileInputStream(file).use { input ->
                val bytes = input.readBytes()
                return String(bytes)
            }
        }

        return null
    }
}