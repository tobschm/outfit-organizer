package de.schmelzle.outfitorganizer.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

object ImageStorage {

    fun copyToAppStorage(context: Context, uri: Uri): String {
        val outfitsDir = File(context.filesDir, "outfits").also { it.mkdirs() }
        val destFile = File(outfitsDir, "${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            destFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return destFile.absolutePath
    }

    fun delete(path: String) {
        File(path).delete()
    }
}
