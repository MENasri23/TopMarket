package ir.jatlin.topmarket.core.domain.util

interface CharSequenceDistance {


    fun unlimitedCompare(first: CharSequence, second: CharSequence): Int

    fun limitedCompare(first: CharSequence, second: CharSequence, threshold: Int): Int

}