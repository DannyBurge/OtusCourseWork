package com.otuscourcework.user_data_keeper

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.otuscourcework.utils.OtusLogger
import javax.inject.Inject

class UserDataKeeper @Inject constructor(context: Context, otusLogger: OtusLogger) {

    private val sharedPrefs: SharedPreferences = try {
        EncryptedSharedPreferences.create(
            PREFERENCES_FILENAME,
            KEY_ALIAS,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        otusLogger.log(e)
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            EncryptedSharedPreferences.create(
                PREFERENCES_FILENAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ).also {
                otusLogger
                    .log("Retry of EncryptedSharedPreferences.create() success.")
            }
        } catch (e: Exception) {
            otusLogger.log(e)
            otusLogger.log("Retry of EncryptedSharedPreferences.create() fail. Prefs without encryption.")
            context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
        }
    }

    var apiToken: String?
        get() = sharedPrefs.getString(API_TOKEN, "")
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) { putString(API_TOKEN, value) }

    var tokensAmount: Int?
        get() = sharedPrefs.getInt(TOKENS_AMOUNT, 0)
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) { putInt(TOKENS_AMOUNT, value ?: 0) }

    var userFavouriteItemIds: List<Int>?
        get() = sharedPrefs.getString(FAVOURITE_ITEMS_LIST, null)
            ?.split(STRING_SEPARATOR)
            ?.map { it.toInt() }
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) {
            putString(
                FAVOURITE_ITEMS_LIST,
                if (value.isNullOrEmpty()) null else value.joinToString(STRING_SEPARATOR)
            )
        }

    var isFavouriteModeActive: Boolean
        get() = sharedPrefs.getBoolean(IS_FAVOURITE_MODE_ACTIVE, false)
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) {
            putBoolean(
                IS_FAVOURITE_MODE_ACTIVE,
                value
            )
        }

    var address: String
        get() = sharedPrefs.getString(ADDRESS, "") ?: ""
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) { putString(ADDRESS, value) }

    companion object {
        private const val API_TOKEN = "token"
        private const val ADDRESS = "address"
        private const val TOKENS_AMOUNT = "tokens_amount"
        private const val FAVOURITE_ITEMS_LIST = "favourite_items"
        private const val IS_FAVOURITE_MODE_ACTIVE = "is_favourite_mode_active"

        private const val STRING_SEPARATOR = ","
        private const val PREFERENCES_FILENAME = "user_preferences"
        private const val KEY_ALIAS = "Otus_key"
        private const val COMMIT_PREF = true
    }
}