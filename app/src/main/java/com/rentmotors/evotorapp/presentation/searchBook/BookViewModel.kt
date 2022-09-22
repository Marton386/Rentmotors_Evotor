package com.rentmotors.evotorapp.presentation.searchBook

import androidx.lifecycle.ViewModel
import com.rentmotors.evotorapp.domain.entities.BookReceipt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
): ViewModel() {
    private var bookReceipt: BookReceipt? = null

    fun getBook(): BookReceipt? {
        return bookReceipt
    }

    fun setBookReceipt(receipt: BookReceipt) {
        bookReceipt = receipt
    }
}