package com.app.notification.config

import java.io.File

object SecretReader {
    fun read(envValue: String?, envFile: String?): String? {
        if (!envValue.isNullOrBlank()) return envValue.trim()
        if (!envFile.isNullOrBlank()) {
            val f = File(envFile)
            if (f.exists()) return f.readText().trim()
        }
        return null
    }
}
