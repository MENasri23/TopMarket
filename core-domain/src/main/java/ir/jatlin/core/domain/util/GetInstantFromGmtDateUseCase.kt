package ir.jatlin.core.domain.util

import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class GetInstantFromGmtDateUseCase @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter
) {

    operator fun invoke(gmtDate: String): Instant? {
        return try {
            LocalDateTime
                .parse(gmtDate, dateTimeFormatter)
                .atZone(ZoneId.of(GMT))
                .toInstant()
        } catch (e: DateTimeParseException) {
            Timber.d("Unable to parse the date input: $gmtDate, with instant: $dateTimeFormatter")
            null
        }

    }

    companion object {
        private const val GMT = "GMT"
    }
}