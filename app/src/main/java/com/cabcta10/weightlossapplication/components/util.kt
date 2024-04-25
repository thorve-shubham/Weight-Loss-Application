import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    private const val TIME_FORMAT_PATTERN = "HH:mm"

    private val timeFormat = SimpleDateFormat(TIME_FORMAT_PATTERN, Locale.UK)

    /**
     * Parses a time string in "HH:mm" format into a Calendar object.
     * @param timeString The time string to parse (e.g., "12:30").
     * @return A Calendar instance representing the parsed time.
     * @throws ParseException if the input string is not in the expected format.
     */
    @Throws(ParseException::class)
    fun parseTime(timeString: String): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = timeFormat.parse(timeString) ?: throw ParseException("Invalid time format", 0)
        return calendar
    }

    /**
     * Formats a Calendar instance to a time string in "HH:mm" format.
     * @param calendar The Calendar instance representing the time.
     * @return The formatted time string (e.g., "12:30").
     */
    fun formatTime(calendar: Calendar): String {
        return timeFormat.format(calendar.time)
    }
}
