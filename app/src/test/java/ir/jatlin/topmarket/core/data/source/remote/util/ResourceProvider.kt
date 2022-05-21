package ir.jatlin.topmarket.core.data.source.remote.util

object ResourceProvider {
    private val resourceLoader = javaClass.classLoader

    fun readFrom(fileName: String) =
        resourceLoader?.getResource(fileName)?.readText()
}