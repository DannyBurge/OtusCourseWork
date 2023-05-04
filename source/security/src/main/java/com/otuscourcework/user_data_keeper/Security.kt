package com.otuscourcework.user_data_keeper

import java.math.BigInteger
import java.security.MessageDigest
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class Security @Inject constructor(
    private val keys: Keys
) {

    fun md5(plaintext: CharSequence): String {
        return createHash(plaintext.toString())
    }

    private fun createHash(plaintext: String): String {
        val md = MessageDigest.getInstance(HASH_TYPE)
        val bigInt = BigInteger(1, md.digest(plaintext.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    fun encryptAes(plainText: String): String {
        // TODO java.lang.NullPointerException: Attempt to get length of null array
//        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
//        cipher.init(Cipher.ENCRYPT_MODE, keys.getAesSecretKey(), getInitializationVector())
//        val encodedBytes = cipher.doFinal(plainText.toByteArray())
//        return Base64.encodeToString(encodedBytes, Base64.NO_WRAP)
        return plainText
    }

    private fun getInitializationVector(): AlgorithmParameterSpec {
        return GCMParameterSpec(128, FIXED_IV)
    }

    fun decryptAes(encrypted: String): String {
//        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
//        cipher.init(Cipher.DECRYPT_MODE, keys.getAesSecretKey(), getInitializationVector())
//        val decodedBytes = Base64.decode(encrypted, Base64.NO_WRAP)
//        val decoded = cipher.doFinal(decodedBytes)
//        return String(decoded, Charsets.UTF_8)
        return encrypted
    }

    companion object {
        private const val HASH_TYPE = "MD5"

        private const val AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"
        private val FIXED_IV = byteArrayOf(55, 54, 53, 52, 51, 50, 49, 48, 47, 46, 45, 44)
    }
}