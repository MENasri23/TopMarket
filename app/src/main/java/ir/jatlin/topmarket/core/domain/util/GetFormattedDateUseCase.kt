package ir.jatlin.topmarket.core.domain.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jatlin.topmarket.R
import timber.log.Timber
import java.time.Clock
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class GetFormattedDateUseCase @Inject constructor(
    @ApplicationContext context: Context,
    private val getInstantFromGmtDateUseCase: GetInstantFromGmtDateUseCase,
    private val clock: Clock
) {

    private val res = context.resources

    operator fun invoke(inputDate: String): String {
        val instant = getInstantFromGmtDateUseCase(inputDate)
        if (instant == null) {
            Timber.d("The formatted date is null for input: $inputDate")
            return ""
        }

        val duration = Duration.between(instant, Instant.now(clock))
        val days = duration.toDays()
        return when {
            days >= 365 -> res.getString(R.string.years_till_due, days / 365)
            days >= 30 -> res.getString(R.string.months_till_due, days / 30)
            days >= 7 -> res.getString(R.string.weeks_till_due, days / 7)
            days >= 1 -> res.getString(R.string.days_till_due, days)
            else -> {
                val seconds = duration.toSeconds()
                if (seconds >= 60) res.getString(R.string.minutes_till_due, seconds / 60)
                else res.getString(R.string.moment_till_due)
            }
        }

    }
}