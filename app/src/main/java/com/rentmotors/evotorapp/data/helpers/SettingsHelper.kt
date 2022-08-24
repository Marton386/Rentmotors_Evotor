package com.rentmotors.evotorapp.data.helpers

import android.content.Context
import androidx.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.rentmotors.evotorapp.domain.entities.BookReceipt

class SettingsHelper(context: Context) {
    companion object {
        private const val KKM_NUMBER = "KKM_NUMBER"
        private const val UUID_RECEIPT = "UUID_RECEIPT"
        private const val BOOK_INFO = "BOOK_INFO"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun clearSharedPreferences(){
        preferences.edit().remove(UUID_RECEIPT).apply()
        preferences.edit().remove(BOOK_INFO).apply()
    }
    fun setKkmNumber(kkmNumber: String) {
        preferences.edit()
            .putString(KKM_NUMBER, kkmNumber)
            .apply()
    }

    fun getKkmNumber(): String {
        //setKkmNumber("")
        val kkm = preferences.getString(KKM_NUMBER, "")
        return if (kkm.isNullOrBlank())
            ""
        else
            kkm
    }

    fun setReceiptUuid(uuid: String) {
        preferences.edit()
            .putString(UUID_RECEIPT, uuid)
            .apply()
    }

    fun getReceiptUuid(): String {
        val uuid = preferences.getString(UUID_RECEIPT, "")
        return if (uuid.isNullOrBlank()) // Это условие лишнее, все равно всегда пустая строка будет, можно сразу uuid возвращать
            ""
        else
            uuid
    }

    fun setBookReceipt(bookReceipt: BookReceipt) {
        val gson = Gson()
        val bookReceiptJson = gson.toJson(bookReceipt)
        preferences.edit()
            .putString(BOOK_INFO, bookReceiptJson)
            .apply()
        Log.d("SAVE BOOK", "ok")

    }

    fun getBookReceipt(): BookReceipt? {
        return try {
            val bookReceiptJson = preferences.getString(BOOK_INFO, null)
            val gson = Gson()
            gson.fromJson(bookReceiptJson, BookReceipt::class.java)
        } catch (e: Exception) {
            null
        }
    }

}