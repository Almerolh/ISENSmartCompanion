package fr.isen.menga.isensmartcompanon.Notif

import android.content.Context
import android.content.SharedPreferences


class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    fun saveNotificationPreference(eventId: String, isNotified: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("event_$eventId", isNotified)
        editor.apply()
    }

    fun getNotificationPreference(eventId: String): Boolean {
        return sharedPreferences.getBoolean("event_$eventId", false)
    }
}