package su.elibrio.mobile.utils

import java.io.File
import kotlin.math.round

object Utils {
    private val File.size get() = if (!exists()) 0.0 else length().toDouble()
    private val File.sizeInKb get() = size / 1024
    val File.sizeInMb get() = round((sizeInKb / 1024) * 100) / 100
}