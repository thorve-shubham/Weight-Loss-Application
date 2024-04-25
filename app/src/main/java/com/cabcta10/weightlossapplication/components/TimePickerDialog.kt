import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SimpleTimePicker(
    timeString: String,
    onTimeChange: (String) -> Unit
) {
    val context = LocalContext.current
    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.time = simpleDateFormat.parse(timeString)!!

    var time by remember { mutableStateOf(timeString) }

    TimePickerDialog(
        context,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            time = simpleDateFormat.format(calendar.time)
            onTimeChange(time)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
}
