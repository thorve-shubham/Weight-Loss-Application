import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import java.util.*

@Composable
fun TimePicker(
    initialTime: Calendar?,
    onTimeSelected: (Calendar) -> Unit
) {
    val context = LocalContext.current
    val selectedTime = remember { initialTime ?: Calendar.getInstance() }

    OutlinedTextField(
        value = selectedTime.formatTime(),
        onValueChange = {}, // This field is read-only
        readOnly = true,
        modifier = Modifier.fillMaxWidth().clickable {
            showTimePickerDialog(context, selectedTime) { newTime ->
                onTimeSelected(newTime)
            }
        }
    )
}

private fun showTimePickerDialog(
    context: Context,
    initialTime: Calendar,
    onTimeSet: (Calendar) -> Unit
) {
    val hour = initialTime.get(Calendar.HOUR_OF_DAY)
    val minute = initialTime.get(Calendar.MINUTE)

    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            onTimeSet(selectedTime)
        },
        hour,
        minute,
        false // 24-hour format
    ).show()
}

private fun Calendar.formatTime(): String {
    return android.text.format.DateFormat.format("hh:mm a", this).toString()
}
