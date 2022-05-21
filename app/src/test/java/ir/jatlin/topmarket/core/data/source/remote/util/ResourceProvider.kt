package ir.jatlin.topmarket.core.data.source.remote.util

import java.lang.IllegalArgumentException

object ResourceProvider {
    private val resourceLoader = javaClass.classLoader

    fun readFrom(fileName: String) =
        resourceLoader?.getResource(fileName)?.readText() ?: throw IllegalArgumentException("resource not found")
}