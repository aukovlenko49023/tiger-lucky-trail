package com.Dopxafi4.Wewwem.data.local

import android.content.Context
import android.content.SharedPreferences

class TigerPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun long(key: String, fallback: Long): Long = prefs.getLong(key, fallback)

    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun boolean(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun string(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun stringSet(key: String): Set<String> = prefs.getStringSet(key, emptySet()) ?: emptySet()

    fun putStringSet(key: String, value: Set<String>) {
        prefs.edit().putStringSet(key, value).apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val FILE_NAME = "tiger_lucky_trail"
        const val KEY_LIFETIME_TOKENS = "lifetime_tokens"
        const val KEY_TOTAL_RUNS = "total_runs"
        const val KEY_SHIELDS = "shields"
        const val KEY_BEST_PREFIX = "best_steps_"
        const val KEY_CLAIMED_TIERS = "claimed_tiers"
        const val KEY_UNLOCKED_CHARMS = "unlocked_charms"
        const val KEY_SELECTED_ROUTE = "selected_route"
        const val KEY_DAILY_DATE = "daily_date"
        const val KEY_DAILY_GOAL = "daily_goal"
        const val KEY_DAILY_TARGET = "daily_target"
        const val KEY_DAILY_ROUTE = "daily_route"
        const val KEY_DAILY_PROGRESS = "daily_progress"
        const val KEY_DAILY_CLAIMED = "daily_claimed"
        const val KEY_LAST_ROUTE = "last_route"
        const val KEY_LAST_BANKED = "last_banked"
        const val KEY_LAST_TOKENS = "last_tokens"
        const val KEY_LAST_STEPS = "last_steps"
        const val KEY_LAST_TOTAL = "last_total"
        const val KEY_LAST_BEST = "last_best"
        const val KEY_LAST_PRESENT = "last_present"
        const val KEY_HAPTICS = "pref_haptics"
        const val KEY_REDUCED_MOTION = "pref_reduced_motion"
        const val KEY_HINTS = "pref_hints"
        const val KEY_PROFILE_READY = "profile_ready"
    }
}
