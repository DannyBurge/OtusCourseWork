package com.otuscourcework.user_data_keeper

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

class UserDataKeeper @Inject constructor(
    context: Context,
    keys: Keys
) {

    private val sharedPrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context.applicationContext,
            PREFERENCES_FILENAME,
            keys.getMasterKey(MasterKey.KeyScheme.AES256_GCM),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var hashPassword: String?
        get() = sharedPrefs.getString(HASH_PASSWORD, null)
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) { putString(HASH_PASSWORD, value) }

    var passwordKey: String?
        get() = sharedPrefs.getString(CRYPTO_PASS_KEY, "")
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) { putString(CRYPTO_PASS_KEY, value) }

    var apiToken: String?
        get() = sharedPrefs.getString(API_TOKEN, "")
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) { putString(API_TOKEN, value) }

    var address: String
        get() = sharedPrefs.getString(ADDRESS, "") ?: ""
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) { putString(ADDRESS, value) }

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
            putBoolean(IS_FAVOURITE_MODE_ACTIVE, value)
        }

    var isAuthActive: Boolean
        get() = sharedPrefs.getBoolean(IS_AUTH_ACTIVE, false)
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) {
            putBoolean(IS_AUTH_ACTIVE, value)
        }

    var isBiometricAuthActive: Boolean
        get() = sharedPrefs.getBoolean(IS_BIOMETRIC_AUTH_ACTIVE, false)
        @Synchronized
        set(value) = sharedPrefs.edit(commit = COMMIT_PREF) {
            putBoolean(IS_BIOMETRIC_AUTH_ACTIVE, value)
        }


    companion object {
        private const val API_TOKEN = "token"
        private const val ADDRESS = "address"
        private const val TOKENS_AMOUNT = "tokens_amount"
        private const val FAVOURITE_ITEMS_LIST = "favourite_items"
        private const val IS_FAVOURITE_MODE_ACTIVE = "is_favourite_mode_active"
        private const val IS_BIOMETRIC_AUTH_ACTIVE = "is_biometric_auth_active"
        private const val IS_AUTH_ACTIVE = "is_auth_active"

        private const val HASH_PASSWORD = "hash_password"
        private const val CRYPTO_PASS_KEY = "crypto_pass_key"

        private const val COMMIT_PREF = true

        const val STRING_SEPARATOR = ","
        private const val PREFERENCES_FILENAME = "user_preferences"
    }
}