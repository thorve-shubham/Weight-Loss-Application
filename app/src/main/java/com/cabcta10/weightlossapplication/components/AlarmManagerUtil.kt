import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmManagerUtil {

    private const val REQUEST_CODE = 12345 // Unique request code for PendingIntent

    /**
     * Schedule an exact alarm using AlarmManager.
     */
    @SuppressLint("ScheduleExactAlarm")
    fun setExactAlarm(context: Context, triggerTimeMillis: Long, intent: Intent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context, intent)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)
    }

    /**
     * Cancel a previously scheduled alarm.
     */
    fun cancelAlarm(context: Context, intent: Intent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context, intent)
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Get a unique PendingIntent for the specified intent.
     */
    private fun getPendingIntent(context: Context, intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
